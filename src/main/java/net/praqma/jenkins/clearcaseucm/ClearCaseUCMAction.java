package net.praqma.jenkins.clearcaseucm;

import hudson.model.Action;
import net.praqma.clearcase.ucm.entities.Baseline;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:08
 */
public class ClearCaseUCMAction implements Action {

    /**
     * The {@link Baseline} found for processing
     */
    private Baseline baseline;

    /**
     * The {@link Baseline} created by a post build {@link net.praqma.jenkins.clearcaseucm.model.Runner}
     */
    private Baseline createdBaseline;

    /* Post build actions */
    private boolean tagBaseline = false;
    private boolean recommendBaseline = false;
    private boolean setBuildDescription = false;

    @Override
    public String getIconFileName() {
        return null;
    }

    @Override
    public String getDisplayName() {
        return "ClearCase UCM";
    }

    @Override
    public String getUrlName() {
        return "clearcaseucm";
    }

    public Baseline getBaseline() {
        return baseline;
    }

    public void setBaseline( Baseline baseline ) {
        this.baseline = baseline;
    }

    public Baseline getCreatedBaseline() {
        return createdBaseline;
    }

    public void setCreatedBaseline( Baseline createdBaseline ) {
        this.createdBaseline = createdBaseline;
    }
}
