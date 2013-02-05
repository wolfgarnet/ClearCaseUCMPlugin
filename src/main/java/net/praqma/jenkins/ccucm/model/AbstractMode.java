package net.praqma.jenkins.ccucm.model;

import hudson.FilePath;
import hudson.model.Action;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:04
 */
public abstract class AbstractMode implements Action {

    protected Component component;
    protected Stream stream;
    protected Project.PromotionLevel level;

    /**
     * This is the {@link Baseline} found by the {@link BaselineSelector}
     */
    protected Baseline baseline;

    public AbstractMode( Component component, Stream stream, Project.PromotionLevel level ) {
        this.component = component;
        this.stream = stream;
        this.level = level;
    }

    @Override
    public String getUrlName() {
        return "ccucm";
    }

    @Override
    public String getDisplayName() {
        return "ClearCase UCM";
    }

    @Override
    public String getIconFileName() {
        return null;
    }

    public void setBaseline( Baseline baseline ) {
        this.baseline = baseline;
    }

    public Baseline getBaseline() {
        return baseline;
    }

    public abstract Poller getPoller();

    public abstract BaselineSelector getBaselineSelector( FilePath workspace );

    public abstract void getWorkspaceInitializer();
}
