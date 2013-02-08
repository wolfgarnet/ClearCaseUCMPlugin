package net.praqma.jenkins.clearcaseucm.remote;

import hudson.FilePath;
import hudson.remoting.VirtualChannel;
import net.praqma.clearcase.exceptions.UnableToPromoteBaselineException;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Project;

import java.io.File;
import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 08-02-13
 *         Time: 22:29
 */
public class RemoteBaselinePromotion implements FilePath.FileCallable<Boolean> {

    private Baseline baseline;
    private Project.PromotionLevel level;

    public RemoteBaselinePromotion( Baseline baseline, Project.PromotionLevel level ) {
        this.baseline = baseline;
        this.level = level;
    }

    @Override
    public Boolean invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {

        try {
            baseline.setPromotionLevel( level );
        } catch( UnableToPromoteBaselineException e ) {
            throw new IOException( e );
        }

        return true;
    }
}
