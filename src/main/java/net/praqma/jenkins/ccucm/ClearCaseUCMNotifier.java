package net.praqma.jenkins.ccucm;

import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 22:01
 */
public class ClearCaseUCMNotifier extends Notifier {

    private ClearCaseUCMScm scm;

    public ClearCaseUCMNotifier( ClearCaseUCMScm scm ) {
        this.scm = scm;
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return null;
    }
}
