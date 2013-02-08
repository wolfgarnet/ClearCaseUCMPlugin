package net.praqma.jenkins.clearcaseucm.strategies.changelog;

import hudson.FilePath;
import hudson.model.BuildListener;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.jenkins.clearcaseucm.model.ChangeLogProducer;

import java.io.IOException;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 21:16
 */
public class EmptyChangeLogProducer extends ChangeLogProducer {

    public EmptyChangeLogProducer( FilePath workspace ) {
        super( workspace );
    }

    @Override
    public String produce( Baseline baseline, BuildListener listener ) throws IOException, InterruptedException {
        return "<changelog />";
    }
}
