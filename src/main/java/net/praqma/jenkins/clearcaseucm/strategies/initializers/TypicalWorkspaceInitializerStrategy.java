package net.praqma.jenkins.clearcaseucm.strategies.initializers;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import hudson.model.BuildListener;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.clearcaseucm.Common;
import net.praqma.jenkins.clearcaseucm.model.WorkspaceInitializer;
import net.praqma.jenkins.clearcaseucm.remote.MakeWorkspace;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 22:15
 */
public class TypicalWorkspaceInitializerStrategy extends WorkspaceInitializer {

    private String subPath;
    private String viewTag;
    private String buildProject;
    private Stream targetStream;
    private String loadModules;

    private Baseline baseline;

    public TypicalWorkspaceInitializerStrategy( AbstractBuild<?, ?> build, String subPath, String viewTag, String buildProject, Baseline baseline, Stream targetStream, String loadModules ) {
        super( build );
        this.subPath = subPath;
        this.viewTag = viewTag;
        this.buildProject = buildProject;
        this.targetStream = targetStream;
        this.loadModules = loadModules;
    }

    @Override
    public void initialize( FilePath workspace, BuildListener listener ) throws IOException, InterruptedException {
        listener.getLogger().println( Common.PRINTNAME + "Initializing workspace in " + subPath );
        workspace.act( new MakeWorkspace( listener, viewTag, subPath, buildProject, baseline, targetStream, loadModules ) );
    }
}
