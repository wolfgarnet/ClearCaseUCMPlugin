package net.praqma.jenkins.ccucm;

import hudson.AbortException;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.BuildListener;
import hudson.model.TaskListener;
import hudson.scm.ChangeLogParser;
import hudson.scm.PollingResult;
import hudson.scm.SCM;
import hudson.scm.SCMRevisionState;
import hudson.tasks.Publisher;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.hudson.exception.CCUCMException;
import net.praqma.hudson.notifier.CCUCMNotifier;
import net.praqma.jenkins.ccucm.model.AbstractMode;
import net.praqma.jenkins.ccucm.model.BaselineSelector;

import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;

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

    @Override
    public SCMRevisionState calcRevisionsFromBuild( AbstractBuild<?, ?> build, Launcher launcher, TaskListener listener ) throws IOException, InterruptedException {
        return null;
    }

    @Override
    protected PollingResult compareRemoteRevisionWith( AbstractProject<?, ?> project, Launcher launcher, FilePath workspace, TaskListener listener, SCMRevisionState baseline ) throws IOException, InterruptedException {
        return null;
    }

    @Override
    public boolean checkout( AbstractBuild<?, ?> build, Launcher launcher, FilePath workspace, BuildListener listener, File changelogFile ) throws IOException, InterruptedException {
        BaselineSelector selector = mode.getBaselineSelector( workspace );
        PrintStream out = listener.getLogger();

        List<Baseline> baselines = selector.getValidBaselines();
        printBaselines( baselines, out );

        try {
            Baseline baseline = selector.selectBaseline( baselines );
            mode.setBaseline( baseline );
            out.println( Common.PRINTNAME + "Found " + baseline.getNormalizedName() );
        } catch( CCUCMException e ) {
            throw new AbortException( e.getMessage() );
        }

        /* Adding */
        build.getProject().getPublishersList().remove( ClearCaseUCMNotifier.class );
        logger.info( "Adding notifier to project" );
        build.getParent().getPublishersList().add( new ClearCaseUCMNotifier( this ) );

        return false;
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
        return null;
    }
}
