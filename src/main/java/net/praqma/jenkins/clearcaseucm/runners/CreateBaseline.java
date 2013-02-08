package net.praqma.jenkins.clearcaseucm.runners;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.jenkins.clearcaseucm.model.Runner;
import net.praqma.jenkins.clearcaseucm.remote.CreateRemoteBaseline;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 08-02-13
 *         Time: 20:30
 */
public class CreateBaseline implements Runner {

    private String baseName;
    private String subPath;
    private String username;
    private Component component;

    public CreateBaseline( String baseName, String subPath, String username, Component component ) {
        this.baseName = baseName;
        this.subPath = subPath;
        this.username = username;
        this.component = component;
    }

    @Override
    public Result run( FilePath workspace, BuildListener listener, Result result ) {
        try {
            workspace.act( new CreateRemoteBaseline( baseName, component, subPath, username, listener ) );
            return result;
        } catch( Exception e ) {
            return Result.FAILURE;
        }
    }

    @Override
    public boolean runOnFailure() {
        return false;
    }

    @Override
    public String getName() {
        return "Create baseline";
    }
}
