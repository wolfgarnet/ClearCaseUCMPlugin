package net.praqma.jenkins.clearcaseucm;

import hudson.AbortException;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.scm.*;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.hudson.exception.CCUCMException;
import net.praqma.jenkins.clearcaseucm.changelog.ClearCaseChangeLogParser;
import net.praqma.jenkins.clearcaseucm.changelog.ClearCaseUCMRevisionState;
import net.praqma.jenkins.clearcaseucm.model.*;

import net.praqma.jenkins.clearcaseucm.utils.FileUtilities;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.System.out;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 21:41
 */
public class ClearCaseUCMScm extends SCM {

    private static Logger logger = Logger.getLogger( ClearCaseUCMScm.class.getName() );

    private AbstractMode mode;

    @DataBoundConstructor
    public ClearCaseUCMScm( AbstractMode mode ) {
        this.mode = mode;
    }

    public AbstractMode getMode() {
        return mode;
    }

    @Override
    public SCMRevisionState calcRevisionsFromBuild( AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener ) throws IOException, InterruptedException {
        SCMRevisionState scmRS = null;

        ClearCaseUCMAction action = build.getAction( ClearCaseUCMAction.class );

        if( action != null && action.getBaseline() != null ) {
            scmRS = new ClearCaseUCMRevisionState();
        }
        return scmRS;
    }

    @Override
    protected PollingResult compareRemoteRevisionWith( AbstractProject<?, ?> project, Launcher launcher, FilePath workspace, TaskListener listener, SCMRevisionState baseline ) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public boolean checkout( AbstractBuild<?, ?> build, Launcher launcher, FilePath workspace, BuildListener listener, File changelogFile ) throws IOException, InterruptedException {

        mode.printConfiguration( listener.getLogger() );

        ClearCaseUCMAction action = mode.createAction( build );

        /* Selecting a baseline */
        BaselineSelector selector = mode.getBaselineSelector( workspace );
        PrintStream out = listener.getLogger();

        List<Baseline> baselines = selector.getValidBaselines();
        printBaselines( baselines, out );

        Baseline baseline = null;
        try {
            baseline = selector.selectBaseline( baselines );
            action.setBaseline( baseline );
            out.println( Common.PRINTNAME + "Selecting the Baseline " + baseline.getNormalizedName() );
        } catch( CCUCMException e ) {
            throw new AbortException( e.getMessage() );
        }

        /* Initializing the workspace */
        WorkspaceInitializer initializer = mode.getWorkspaceInitializer( build, baseline );

        logger.fine( "Initializing workspace" );
        initializer.initialize( workspace, listener );

        logger.fine( "Getting change log" );
        ChangeLogProducer changeLogProducer = mode.getChangeLogProducer( workspace );
        String changelog = changeLogProducer.produce( action.getBaseline(), listener );

        logger.fine( "Writing change log" );
        FileUtilities.writeToFile( changelogFile, changelog );

        /* Adding */
        build.getProject().getPublishersList().remove( ClearCaseUCMNotifier.class );
        logger.info( "Adding notifier to project with " + mode );
        build.getParent().getPublishersList().add( new ClearCaseUCMNotifier( mode ) );

        return true;
    }


    private void printBaselines( List<Baseline> baselines, PrintStream ps ) {
        if( baselines != null ) {
            ps.println( Common.PRINTNAME + "Retrieved " + baselines.size() + " baseline" + ( baselines.size() == 1 ? "" : "s" ) + ":" );
            if( !( baselines.size() > 8 ) ) {
                for( Baseline b : baselines ) {
                    ps.println( Common.PRINTNAME + " + " + b.getShortname() + "(" + b.getDate() + ")" );
                }
            } else {
                int i = baselines.size();
                ps.println( Common.PRINTNAME + " + " + baselines.get( 0 ).getShortname() + "(" + baselines.get( 0 ).getDate() + ")" );
                ps.println( Common.PRINTNAME + " + " + baselines.get( 1 ).getShortname() + "(" + baselines.get( 1 ).getDate() + ")" );
                ps.println( Common.PRINTNAME + " + " + baselines.get( 2 ).getShortname() + "(" + baselines.get( 2 ).getDate() + ")" );
                ps.println( Common.PRINTNAME + "   ..." );
                ps.println( Common.PRINTNAME + " + " + baselines.get( i - 3 ).getShortname() + "(" + baselines.get( i - 3 ).getDate() + ")" );
                ps.println( Common.PRINTNAME + " + " + baselines.get( i - 2 ).getShortname() + "(" + baselines.get( i - 2 ).getDate() + ")" );
                ps.println( Common.PRINTNAME + " + " + baselines.get( i - 1 ).getShortname() + "(" + baselines.get( i - 1 ).getDate() + ")" );
            }
        }
    }

    @Override
    public ChangeLogParser createChangeLogParser() {
        return new ClearCaseChangeLogParser();
    }


    @Extension
    public static final class ClearCaseUCMDescriptor extends SCMDescriptor<ClearCaseUCMScm> {

        public ClearCaseUCMDescriptor() {
            super( ClearCaseUCMScm.class, null );
        }

        @Override
        public String getDisplayName() {
            return "ClearCase UCM";
        }

        @Override
        public SCM newInstance( StaplerRequest req, JSONObject formData ) throws FormException {
            out.println( "FORM: " + formData.toString( 2 ) );
            ClearCaseUCMScm r = (ClearCaseUCMScm) super.newInstance( req, formData );
            ClearCaseUCMModeDescriptor<AbstractMode> d = (ClearCaseUCMModeDescriptor<AbstractMode>) r.getMode().getDescriptor();
            r.mode = d.newInstance( req, formData );
            save();
            return r;
        }

        public List<ClearCaseUCMModeDescriptor<?>> getModes() {
            return AbstractMode.getDescriptors();
        }
    }
}
