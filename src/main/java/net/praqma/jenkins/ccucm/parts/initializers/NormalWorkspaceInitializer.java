package net.praqma.jenkins.ccucm.parts.initializers;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import net.praqma.jenkins.ccucm.model.WorkspaceInitializer;

import java.io.File;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 22:15
 */
public class NormalWorkspaceInitializer extends WorkspaceInitializer {

    public NormalWorkspaceInitializer( AbstractBuild<?, ?> build, FilePath workspace, File changelogFile ) {
        super( build, workspace, changelogFile );
    }

    @Override
    public void initialize() {
    }
}
