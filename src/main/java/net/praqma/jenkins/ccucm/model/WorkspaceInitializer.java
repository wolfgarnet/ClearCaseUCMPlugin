package net.praqma.jenkins.ccucm.model;

import hudson.FilePath;
import hudson.model.AbstractBuild;

import java.io.File;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:23
 */
public abstract class WorkspaceInitializer {

    protected AbstractBuild build;
    protected FilePath workspace;
    protected File changelogFile;

    public WorkspaceInitializer( AbstractBuild<?, ?> build, FilePath workspace, File changelogFile ) {
        this.build = build;
        this.workspace = workspace;
        this.changelogFile = changelogFile;
    }

    public abstract void initialize();
}
