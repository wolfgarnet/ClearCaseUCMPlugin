package net.praqma.jenkins.clearcaseucm.model;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:31
 */
public interface Runner {
    public Result run( FilePath workspace, BuildListener listener, Result result );
    public boolean runOnFailure();
    public String getName();
}
