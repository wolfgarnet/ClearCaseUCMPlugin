package net.praqma.jenkins.clearcaseucm.strategies.initializers;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.jenkins.clearcaseucm.model.WorkspaceInitializer;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 13:15
 */
public class EmptyWorkspaceInitializer extends WorkspaceInitializer {

    public EmptyWorkspaceInitializer( AbstractBuild<?, ?> build, FilePath workspace ) {
        super( build, workspace );
    }

    @Override
    public void initialize() {
    }
}
