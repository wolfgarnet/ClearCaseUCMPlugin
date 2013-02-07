package net.praqma.jenkins.clearcaseucm.runners;

import hudson.FilePath;
import hudson.model.BuildListener;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.jenkins.clearcaseucm.model.Runner;
import net.praqma.jenkins.clearcaseucm.remote.RecommendBaseline;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:58
 */
public class RecommedBaseline implements Runner {

    private Baseline baseline;

    public RecommedBaseline( Baseline baseline ) {
        this.baseline = baseline;
    }

    @Override
    public boolean runOnFailure() {
        return false;
    }

    @Override
    public void run( FilePath workspace, BuildListener listener ) throws IOException, InterruptedException {
        workspace.act( new RecommendBaseline( baseline ) );
    }

    @Override
    public String getName() {
        return "Recommending " + baseline.getNormalizedName();
    }
}
