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
    public Result run( FilePath workspace, BuildListener listener, Result result ) {

        return result;
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
