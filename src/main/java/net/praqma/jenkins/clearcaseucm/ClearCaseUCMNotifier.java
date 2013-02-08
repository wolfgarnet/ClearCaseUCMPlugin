package net.praqma.jenkins.clearcaseucm;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import net.praqma.jenkins.clearcaseucm.model.AbstractMode;
import net.praqma.jenkins.clearcaseucm.model.Runner;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 22:01
 */
public class ClearCaseUCMNotifier extends Notifier {

    private static Logger logger = Logger.getLogger( ClearCaseUCMNotifier.class.getName() );

    private AbstractMode mode;

    @DataBoundConstructor
    public ClearCaseUCMNotifier() {

    }

    public ClearCaseUCMNotifier( AbstractMode mode ) {
        this.mode = mode;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform( AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener ) throws InterruptedException, IOException {
        listener.getLogger().println( Common.PRINTNAME + "Running ClearCaseUCM post build steps" );
        logger.info( "RESULT: " + build.getResult() );

        ClearCaseUCMAction action = mode.getAction( build );

        for( Runner runner : mode.getRunners( action ) ) {
            listener.getLogger().println( Common.PRINTNAME + "Running " + runner.getName() );
            logger.info( "Current RESULT: " + build.getResult() );

            boolean success = mode.doTreatUnstableAsSuccessful() ? build.getResult().isBetterOrEqualTo( Result.UNSTABLE ) : build.getResult().isBetterThan( Result.UNSTABLE );
            logger.info( "Treating build as successful: " + success + " for " + runner.getName() );
            if( runner.runOnFailure() || success ) {
                Result newResult = runner.run( build.getWorkspace(), listener, build.getResult() );
                if( newResult.isWorseThan( build.getResult() ) ) {
                    listener.getLogger().println( Common.PRINTNAME + "Changing result to " + newResult );
                    build.setResult( newResult );
                }
            }
        }

        listener.getLogger().println( Common.PRINTNAME + "Final result is " + build.getResult() );

        return true;
    }

    @Extension
    public static class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        @Override
        public boolean isApplicable( Class<? extends AbstractProject> jobType ) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return "JAJA";
        }
    }
}
