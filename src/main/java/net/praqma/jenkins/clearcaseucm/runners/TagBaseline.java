package net.praqma.jenkins.clearcaseucm.runners;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.jenkins.clearcaseucm.model.Runner;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 08-02-13
 *         Time: 19:31
 */
public class TagBaseline implements Runner {

    private Baseline baseline;

    public TagBaseline( Baseline baseline ) {
        this.baseline = baseline;
    }

    @Override
    public void run( FilePath workspace, BuildListener listener ) throws IOException, InterruptedException {
    }

    @Override
    public boolean runOnFailure() {
        return true;
    }

    @Override
    public String getName() {
        return "Tag baseline";
    }
}
