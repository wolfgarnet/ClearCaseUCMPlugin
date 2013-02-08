package net.praqma.jenkins.clearcaseucm.strategies.initializers;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import net.praqma.jenkins.clearcaseucm.model.WorkspaceInitializer;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 22:15
 */
public class TypicalWorkspaceInitializerStrategy extends WorkspaceInitializer {

    public TypicalWorkspaceInitializerStrategy( AbstractBuild<?, ?> build ) {
        super( build );
    }

    @Override
    public void initialize( FilePath workspace) {

    }
}
