package net.praqma.jenkins.clearcaseucm.runners;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.clearcaseucm.Common;
import net.praqma.jenkins.clearcaseucm.model.Runner;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:58
 */
public class RecommendBaseline implements Runner {

    private static Logger logger = Logger.getLogger( RecommendBaseline.class.getName() );

    private Baseline baseline;
    private Stream stream;

    public RecommendBaseline( Baseline baseline, Stream stream ) {
        this.baseline = baseline;
        this.stream = stream;
    }

    @Override
    public boolean runOnFailure() {
        return false;
    }

    @Override
    public Result run( FilePath workspace, BuildListener listener, Result result ) {
        listener.getLogger().println( Common.PRINTNAME + "Recommending " + baseline.getNormalizedName() );
        try {
            workspace.act( new net.praqma.jenkins.clearcaseucm.remote.RecommendBaseline( baseline, stream ) );
            return result;
        } catch( Exception e ) {
            listener.getLogger().println( Common.PRINTNAME + "Error: Unable to recommend baseline" );
            logger.log( Level.WARNING, "Error while recommending " + baseline.getNormalizedName(), e );
            return Result.UNSTABLE;
        }
    }

    @Override
    public String getName() {
        return "Recommending " + baseline.getNormalizedName();
    }
}
