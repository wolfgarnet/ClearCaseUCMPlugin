package net.praqma.jenkins.clearcaseucm.model;

import hudson.FilePath;
import hudson.model.AbstractBuild;

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

    public abstract void initialize( FilePath workspace );
}
