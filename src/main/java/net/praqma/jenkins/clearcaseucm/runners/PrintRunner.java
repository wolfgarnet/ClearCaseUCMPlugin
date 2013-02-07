package net.praqma.jenkins.clearcaseucm.runners;

import hudson.FilePath;
import hudson.model.BuildListener;
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
    public void run( FilePath workspace, BuildListener listener ) throws IOException, InterruptedException {
        listener.getLogger().println( message );
    }

    @Override
    public boolean runOnFailure() {
        return false;
    }

    @Override
    public String getName() {
        return "Dumb runner";
    }
}
