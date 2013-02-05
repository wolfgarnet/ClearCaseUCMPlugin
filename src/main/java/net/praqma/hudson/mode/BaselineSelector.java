package net.praqma.hudson.mode;

import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.hudson.exception.CCUCMException;

import java.util.Collections;
import java.util.List;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:20
 */
public abstract class BaselineSelector {
    public abstract List<Baseline> getValidBaselines();

    public Baseline selectBaseline( List<Baseline> baselines ) throws CCUCMException {
        if( baselines.size() > 0 ) {
            return baselines.get( 0 );
        } else {
            throw new CCUCMException( "No valid baseline to select" );
        }
    }
}
