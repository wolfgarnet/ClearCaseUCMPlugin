package net.praqma.jenkins.clearcaseucm.remote;

import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import net.praqma.clearcase.exceptions.*;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Tag;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author cwolfgang
 *         Date: 08-02-13
 *         Time: 22:56
 */
public class RemoteTagBaseline implements FilePath.FileCallable<Boolean> {

    private Baseline baseline;
    private String name;
    private String buildNumber;
    private Map<String, String> keyValues;

    public RemoteTagBaseline( Baseline baseline, String name, String buildNumber, Map<String, String> keyValues ) {
        this.baseline = baseline;
        this.name = name;
        this.buildNumber = buildNumber;
        this.keyValues = keyValues;
    }

    @Override
    public Boolean invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {
        try {
            Tag tag = baseline.getTag( name, buildNumber );
            for( String key : keyValues.keySet() ) {
                tag.setEntry( key, keyValues.get( key ) );
            }

            tag.persist();
        } catch( ClearCaseException e ) {
            throw new IOException( e );
        }

        return true;
    }
}
