package net.praqma.jenkins.clearcaseucm.model;

import hudson.FilePath;
import hudson.model.BuildListener;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:31
 */
public interface Runner {
    public void run( FilePath workspace, BuildListener listener ) throws IOException, InterruptedException;
    public boolean runOnFailure();
    public String getName();
}
