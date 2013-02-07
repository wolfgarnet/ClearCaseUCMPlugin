package net.praqma.jenkins.clearcaseucm.model;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import net.praqma.clearcase.ucm.entities.Baseline;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:23
 */
public abstract class WorkspaceInitializer {

    protected AbstractBuild build;
    protected FilePath workspace;

    public WorkspaceInitializer( AbstractBuild<?, ?> build, FilePath workspace ) {
        this.build = build;
        this.workspace = workspace;
    }

    public abstract void initialize();
}
