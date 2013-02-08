package net.praqma.jenkins.clearcaseucm.runners;

import hudson.FilePath;
import hudson.model.BuildListener;
import hudson.model.Result;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.jenkins.clearcaseucm.Common;
import net.praqma.jenkins.clearcaseucm.model.Runner;
import net.praqma.jenkins.clearcaseucm.remote.RemoteTagBaseline;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cwolfgang
 *         Date: 08-02-13
 *         Time: 19:31
 */
public class TagBaseline implements Runner {

    private Baseline baseline;
    private String name;
    private String number;

    public TagBaseline( Baseline baseline, String name, String number ) {
        this.baseline = baseline;
        this.name = name;
        this.number = number;
    }

    @Override
    public Result run( FilePath workspace, BuildListener listener, Result result ) {

        Map<String, String> keyValues = new HashMap<String, String>();

        keyValues.put( "buildstatus", result.toString() );

        try {
            listener.getLogger().println( Common.PRINTNAME + "Tagging " + baseline.getNormalizedName() );
            workspace.act( new RemoteTagBaseline( baseline, name, number, keyValues ) );
        } catch( Exception e ) {
            listener.getLogger().println( Common.PRINTNAME + "Failed to tag " + baseline.getNormalizedName() );
            return Result.UNSTABLE;
        }

        return result;
    }

    @Override
    public boolean runOnFailure() {
        return true;
    }

    @Override
    public String getName() {
        return "Tag baseline";
    }
}
