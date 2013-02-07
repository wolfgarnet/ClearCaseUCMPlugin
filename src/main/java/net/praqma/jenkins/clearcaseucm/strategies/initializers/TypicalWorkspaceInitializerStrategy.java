package net.praqma.jenkins.clearcaseucm.strategies.initializers;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.jenkins.clearcaseucm.model.WorkspaceInitializer;
import net.praqma.jenkins.clearcaseucm.remote.PreviousBaselineDiff;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 22:15
 */
public class TypicalWorkspaceInitializerStrategy extends WorkspaceInitializer {

    public TypicalWorkspaceInitializerStrategy( AbstractBuild<?, ?> build, FilePath workspace ) {
        super( build, workspace );
    }

    @Override
    public void initialize() {
    }
}
