package net.praqma.jenkins.ccucm.modes;

import hudson.FilePath;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.ccucm.model.AbstractMode;
import net.praqma.jenkins.ccucm.model.BaselineSelector;
import net.praqma.jenkins.ccucm.model.Poller;
import net.praqma.jenkins.ccucm.strategies.selectors.*;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:57
 */
public class SelfMode extends AbstractMode {

    public SelfMode( Component component, Stream stream, Project.PromotionLevel level ) {
        super( component, stream, level );
    }

    @Override
    public Poller getPoller() {
        return null;
    }

    @Override
    public BaselineSelector getBaselineSelector( FilePath workspace ) {
        return new SingleStreamSelector( workspace, component, stream, level );
    }

    @Override
    public void getWorkspaceInitializer() {
    }
}
