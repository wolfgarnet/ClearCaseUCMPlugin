package net.praqma.jenkins.clearcaseucm;

import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import hudson.model.Result;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import net.praqma.jenkins.clearcaseucm.model.AbstractMode;
import net.praqma.jenkins.clearcaseucm.model.Runner;

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

    public ClearCaseUCMNotifier( AbstractMode mode ) {
        this.mode = mode;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return null;
    }

    @Override
    public boolean perform( AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener ) throws InterruptedException, IOException {

        ClearCaseUCMAction action = mode.getAction( build );

        for( Runner runner : mode.getRunners( action.getBaseline() ) ) {
            boolean success = mode.doTreatUnstableAsSuccessful() ? build.getResult().isBetterOrEqualTo( Result.UNSTABLE ) : build.getResult().isBetterThan( Result.UNSTABLE );
            logger.finer( "Treating build as successful: " + success + " for " + runner.getName() );
            if( runner.runOnFailure() || success ) {
                runner.run( build.getWorkspace(), listener );
            }
        }

        return true;
    }
}
