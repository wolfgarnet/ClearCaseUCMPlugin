package net.praqma.jenkins.clearcaseucm.model;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:23
 */
public abstract class WorkspaceInitializer {

    protected AbstractBuild build;

    public WorkspaceInitializer( AbstractBuild<?, ?> build ) {
        this.build = build;
    }

    public abstract void initialize( FilePath workspace, BuildListener listener ) throws IOException, InterruptedException;

    public static String makeViewtag( String jobname ) {
        String newJobName = jobname.replaceAll("\\s", "_");
        String viewtag = "CCUCM_" + newJobName + "_" + System.getenv( "COMPUTERNAME" );

        return viewtag;
    }
}
