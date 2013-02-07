package net.praqma.jenkins.clearcaseucm.remote;

import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import net.praqma.clearcase.exceptions.CleartoolException;
import net.praqma.clearcase.exceptions.UCMEntityNotFoundException;
import net.praqma.clearcase.exceptions.UnableToInitializeEntityException;
import net.praqma.clearcase.exceptions.UnableToLoadEntityException;
import net.praqma.clearcase.ucm.entities.Activity;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Version;
import net.praqma.jenkins.clearcaseucm.utils.ChangeLogUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:00
 */
public class PreviousBaselineDiff implements FilePath.FileCallable<String> {

    private Baseline baseline;
    private String subpath;

    public PreviousBaselineDiff( Baseline baseline ) {
        this.baseline = baseline;
    }

    public PreviousBaselineDiff( Baseline baseline, String subpath ) {
        this.baseline = baseline;
        this.subpath = subpath;
    }

    @Override
    public String invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {

        File viewroot = f;
        if( subpath != null && !subpath.isEmpty() ) {
            viewroot = new File( f, subpath );
        }

        List<Activity> bldiff = null;
        try {
            bldiff = Version.getBaselineDiff( baseline, null, true, viewroot );
        } catch( Exception e ) {
            throw new IOException( e );
        }

        return ChangeLogUtils.createChangelog( bldiff, baseline );
    }
}
