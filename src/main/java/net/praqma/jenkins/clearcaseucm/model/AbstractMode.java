package net.praqma.jenkins.clearcaseucm.model;

import hudson.DescriptorExtensionList;
import hudson.ExtensionPoint;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.Describable;
import hudson.model.Descriptor;
import hudson.model.Hudson;
import jenkins.model.Jenkins;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.clearcaseucm.ClearCaseUCMAction;
import net.praqma.jenkins.clearcaseucm.Common;
import net.praqma.jenkins.clearcaseucm.runners.PromoteBaseline;
import net.praqma.jenkins.clearcaseucm.runners.RecommendBaseline;
import net.praqma.jenkins.clearcaseucm.runners.TagBaseline;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:04
 */
public abstract class AbstractMode implements Describable<AbstractMode>, ExtensionPoint {
    private static Logger logger = Logger.getLogger( AbstractMode.class.getName() );

    protected Component component;
    protected Stream stream;
    protected Project.PromotionLevel level;

    protected String subPath = null;

    protected boolean treatUnstableAsSuccessful = false;

    /* Post build actions */
    private boolean tagBaseline = false;
    private boolean recommendBaseline = false;
    private boolean promoteBaseline = false;
    private boolean setBuildDescription = false;

    public AbstractMode( Component component, Stream stream, Project.PromotionLevel level ) {
        this.component = component;
        this.stream = stream;
        this.level = level;
    }

    public boolean doTreatUnstableAsSuccessful() {
        return treatUnstableAsSuccessful;
    }

    public void setTreatUnstableAsSuccessful( boolean treatUnstableAsSuccessful ) {
        this.treatUnstableAsSuccessful = treatUnstableAsSuccessful;
    }

    public String getSubPath() {
        return subPath;
    }

    public void setSubPath( String subPath ) {
        this.subPath = subPath;
    }

    public boolean isTagBaseline() {
        return tagBaseline;
    }

    public void setTagBaseline( boolean tagBaseline ) {
        this.tagBaseline = tagBaseline;
    }

    public boolean isRecommendBaseline() {
        return recommendBaseline;
    }

    public void setRecommendBaseline( boolean recommendBaseline ) {
        this.recommendBaseline = recommendBaseline;
    }

    public boolean isPromoteBaseline() {
        return promoteBaseline;
    }

    public void setPromoteBaseline( boolean promoteBaseline ) {
        this.promoteBaseline = promoteBaseline;
    }

    public boolean isSetBuildDescription() {
        return setBuildDescription;
    }

    public void setSetBuildDescription( boolean setBuildDescription ) {
        this.setBuildDescription = setBuildDescription;
    }

    public abstract String getName();

    public abstract Poller getPoller();

    public abstract BaselineSelector getBaselineSelector( FilePath workspace );

    public abstract WorkspaceInitializer getWorkspaceInitializer( AbstractBuild<?, ?> build, Baseline baseline );

    public abstract ChangeLogProducer getChangeLogProducer( FilePath workspace );

    public List<Runner> getRunners( ClearCaseUCMAction action ) {
        List<Runner> runners = new ArrayList<Runner>();

        /* Remember this is only valid when polling self.
         * When polling childs, the stream value is NOT valid */
        if( recommendBaseline ) {
            runners.add( new RecommendBaseline( action.getBaseline(), stream ) );
        }

        if( promoteBaseline ) {
            runners.add( new PromoteBaseline( action.getBaseline(), treatUnstableAsSuccessful ) );
        }

        if( tagBaseline ) {
            runners.add( new TagBaseline( action.getBaseline(), action.getBuild().getProject().getDisplayName(), Integer.toString( action.getBuild().getNumber() ) ) );
        }

        return runners;
    }

    public ClearCaseUCMAction createAction( AbstractBuild<?, ?> build ) {
        ClearCaseUCMAction action = new ClearCaseUCMAction( build );
        build.addAction( action );
        return action;
    }

    public ClearCaseUCMAction getAction( AbstractBuild<?, ?> build ) {
        return build.getAction( ClearCaseUCMAction.class );
    }

    @Override
    public Descriptor<AbstractMode> getDescriptor() {
        return (ClearCaseUCMModeDescriptor) Jenkins.getInstance().getDescriptorOrDie( getClass() );
    }

    public static DescriptorExtensionList<AbstractMode, ClearCaseUCMModeDescriptor<AbstractMode>> all() {
        return Jenkins.getInstance().<AbstractMode, ClearCaseUCMModeDescriptor<AbstractMode>> getDescriptorList( AbstractMode.class );
    }


    public static List<ClearCaseUCMModeDescriptor<?>> getDescriptors() {
        List<ClearCaseUCMModeDescriptor<?>> list = new ArrayList<ClearCaseUCMModeDescriptor<?>>();
        for( ClearCaseUCMModeDescriptor<?> d : all() ) {
            list.add( d );
        }

        return list;
    }

    public void printConfiguration( PrintStream out ) {

        String version = Hudson.getInstance().getPlugin( "clearcase-ucm-plugin" ).getWrapper().getVersion();
        out.println( Common.PRINTNAME + "ClearCase UCM Plugin version " + version );

        out.println( Common.PRINTNAME + "Configuration for build :" );
        out.println( Common.PRINTNAME + " * Stream:          " + stream.getNormalizedName() );
        out.println( Common.PRINTNAME + " * Component:       " + component.getNormalizedName() );
        out.println( Common.PRINTNAME + " * Promotion level: " + level );

        out.println( "" );

        logger.info( "ClearCase UCM Plugin version: " + version );
    }

    @Override
    public String toString() {
        return getName() + "[" + component.getNormalizedName() + ", " + stream.getNormalizedName() + ", " + level.name() + "]";
    }
}
