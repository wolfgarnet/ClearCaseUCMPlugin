package net.praqma.jenkins.clearcaseucm.runners;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;
import net.praqma.jenkins.clearcaseucm.model.Runner;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 13:12
 */
public class PrintRunner implements Runner {

    private String message;

    public PrintRunner( String message ) {
        this.message = message;
    }

    @Override
    public Result run( FilePath workspace, BuildListener listener, Result result ) {
        listener.getLogger().println( message );

        return result;
    }

    @Override
    public boolean runOnFailure() {
        return false;
    }

    @Override
    public String getName() {
        return "Print runner";
    }
}
