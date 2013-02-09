package net.praqma.jenkins.clearcaseucm.junit;

import hudson.Extension;
import hudson.Launcher;
import hudson.model.*;
import hudson.model.listeners.RunListener;
import net.praqma.jenkins.clearcaseucm.ClearCaseUCMScm;
import net.praqma.logging.LoggingUtil;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.TestBuilder;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 *         Date: 08-02-13
 *         Time: 23:34
 */
public class ClearCaseUCMRule extends JenkinsRule {

    private static Logger logger = Logger.getLogger( ClearCaseUCMRule.class.getName() );

    public static class ProjectBuilder {
        Project<?, ?> project;

        boolean fail = false;

        boolean displayOutput = true;

        public ProjectBuilder( Project<?, ?> project ) {
            this.project = project;
        }

        public ProjectBuilder failBuild( boolean cancel ) {
            this.fail = cancel;
            return this;
        }

        public AbstractBuild build() throws ExecutionException, InterruptedException, IOException {
            if( fail ) {
                logger.info( "Failing " + project );
                project.getBuildersList().add(new Failer() );
            } else {
                /* Should remove fail task */
                project.getBuildersList().remove( Failer.class );
            }

            Future<? extends Build<?, ?>> futureBuild = project.scheduleBuild2( 0, new Cause.UserCause() );

            AbstractBuild build = futureBuild.get();

            if( displayOutput ) {
                logger.info( "Build info for: " + build );
                logger.info( "Workspace: " + build.getWorkspace() );
                logger.info( "Logfile: " + build.getLogFile() );
                logger.info( "DESCRIPTION: " + build.getDescription() );

                logger.info( "-------------------------------------------------\nJENKINS LOG: " );
                logger.info( getLog( build ) );
                logger.info( "\n-------------------------------------------------\n" );
            }

            return build;
        }
    }

    public static class Failer extends TestBuilder {

        @Override
        public boolean perform( AbstractBuild<?, ?> build, Launcher launcher, BuildListener listener ) throws InterruptedException, IOException {
            return false;
        }
    }

    public FreeStyleProject createProject( String name, ClearCaseUCMScm ccucm ) throws IOException {
        FreeStyleProject project = createFreeStyleProject( name );
        project.setScm( ccucm );

        return project;
    }

}
