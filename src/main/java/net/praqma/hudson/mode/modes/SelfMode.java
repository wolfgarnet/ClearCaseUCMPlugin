package net.praqma.hudson.mode.modes;

import net.praqma.hudson.mode.AbstractMode;
import net.praqma.hudson.mode.BaselineSelector;
import net.praqma.hudson.mode.Poller;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:57
 */
public class SelfMode extends AbstractMode {

    @Override
    public Poller getPoller() {
        return null;
    }

    @Override
    public BaselineSelector getBaselineSelector() {
        return null;
    }

    @Override
    public void getWorkspaceInitializer() {
    }
}
