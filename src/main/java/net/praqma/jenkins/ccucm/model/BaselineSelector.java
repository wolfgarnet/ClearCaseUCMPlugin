package net.praqma.jenkins.ccucm.model;

import hudson.FilePath;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.hudson.exception.CCUCMException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:20
 */
public abstract class BaselineSelector {

    protected FilePath workspace;
    protected Component component;
    protected Stream stream;
    protected Project.PromotionLevel level;

    public BaselineSelector( FilePath workspace, Component component, Stream stream, Project.PromotionLevel level ) {
        this.workspace = workspace;
        this.component = component;
        this.stream = stream;
        this.level = level;
    }

    public abstract List<Baseline> getValidBaselines() throws IOException, InterruptedException;

    public Baseline selectBaseline( List<Baseline> baselines ) throws CCUCMException {
        if( baselines.size() > 0 ) {
            return baselines.get( 0 );
        } else {
            throw new CCUCMException( "No valid baseline to select" );
        }
    }
}
