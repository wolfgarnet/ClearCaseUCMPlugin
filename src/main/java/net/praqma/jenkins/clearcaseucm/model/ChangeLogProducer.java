package net.praqma.jenkins.clearcaseucm.model;

import hudson.FilePath;
import net.praqma.clearcase.ucm.entities.Baseline;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 14:40
 */
public abstract class ChangeLogProducer {

    protected FilePath workspace;

    public ChangeLogProducer( FilePath workspace ) {
        this.workspace = workspace;
    }

    public abstract String produce( Baseline baseline ) throws IOException, InterruptedException;
}
