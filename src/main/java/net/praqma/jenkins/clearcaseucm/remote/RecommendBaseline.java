package net.praqma.jenkins.clearcaseucm.remote;

import hudson.FilePath;
import hudson.model.Result;
import hudson.remoting.VirtualChannel;
import net.praqma.clearcase.ucm.entities.Baseline;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:34
 */
public class RecommendBaseline implements FilePath.FileCallable<Boolean> {

    private Baseline baseline;

    public RecommendBaseline( Baseline baseline ) {
        this.baseline = baseline;
    }

    @Override
    public Boolean invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {
        return null;
    }
}
