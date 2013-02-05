package net.praqma.hudson.mode;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 14:04
 */
public abstract class AbstractMode {
    public abstract Poller getPoller();

    public abstract BaselineSelector getBaselineSelector();

    public abstract void getWorkspaceInitializer();
}
