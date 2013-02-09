package net.praqma.jenkins.clearcaseucm.remote;

import hudson.FilePath;
import hudson.model.Result;
import hudson.remoting.VirtualChannel;
import net.praqma.clearcase.exceptions.CleartoolException;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Stream;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:34
 */
public class RecommendBaseline implements FilePath.FileCallable<Boolean> {

    private Baseline baseline;
    private Stream stream;

    public RecommendBaseline( Baseline baseline, Stream stream ) {
        this.baseline = baseline;
        this.stream = stream;
    }

    @Override
    public Boolean invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {
        try {
            stream.recommendBaseline( baseline );
        } catch( CleartoolException e ) {
            throw new IOException( e );
        }
        return true;
    }
}
