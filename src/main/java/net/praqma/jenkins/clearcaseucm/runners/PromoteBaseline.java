package net.praqma.jenkins.clearcaseucm.runners;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.jenkins.clearcaseucm.Common;
import net.praqma.jenkins.clearcaseucm.model.Runner;
import net.praqma.jenkins.clearcaseucm.remote.RemoteBaselinePromotion;

/**
 * @author cwolfgang
 *         Date: 08-02-13
 *         Time: 22:27
 */
public class PromoteBaseline implements Runner {

    private Baseline baseline;
    private boolean treatUnstableAsSuccessful;

    public PromoteBaseline( Baseline baseline, boolean treatUnstableAsSuccessful ) {
        this.baseline = baseline;
        this.treatUnstableAsSuccessful = treatUnstableAsSuccessful;
    }

    @Override
    public Result run( FilePath workspace, BuildListener listener, Result result ) {
        try {
            Project.PromotionLevel level = baseline.getPromotionLevel( false );
            listener.getLogger().println( "LEVEL IS " + level );
            if( result.isBetterOrEqualTo( Result.SUCCESS ) || ( treatUnstableAsSuccessful && result.isBetterOrEqualTo( Result.UNSTABLE ) ) ) {
                level = Project.promoteFrom( level );
            } else {
                level = Project.PromotionLevel.REJECTED;
            }

            listener.getLogger().println( Common.PRINTNAME + "Setting promotion level of " + baseline.getNormalizedName() + " to " + level );

            workspace.act( new RemoteBaselinePromotion( baseline, level ) );

            return result;
        } catch( Exception e ) {
            listener.getLogger().println( Common.PRINTNAME + "Failed to set promotion level" );
            return Result.UNSTABLE;
        }
    }

    @Override
    public boolean runOnFailure() {
        return true;
    }

    @Override
    public String getName() {
        return "Promote baseline";
    }
}
