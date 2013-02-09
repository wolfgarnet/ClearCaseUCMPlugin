package net.praqma.jenkins.clearcaseucm;

import hudson.Extension;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import net.praqma.logging.LoggingUtil;

import java.util.logging.Level;

@Extension
public class LoggerListener extends RunListener<Run> {

    public LoggerListener() {
        super( Run.class );
    }

    @Override
    public void onStarted( Run run, TaskListener listener ) {
        LoggingUtil.setPraqmaticHandler( Level.ALL, "net.praqma" );
    }
}
