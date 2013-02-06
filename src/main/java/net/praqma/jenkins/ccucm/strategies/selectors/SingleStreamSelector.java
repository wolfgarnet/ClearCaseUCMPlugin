package net.praqma.jenkins.ccucm.strategies.selectors;

import hudson.FilePath;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.hudson.remoting.GetRemoteBaselineFromStream;
import net.praqma.jenkins.ccucm.model.BaselineSelector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 15:05
 */
public class SingleStreamSelector extends BaselineSelector {

    public SingleStreamSelector( FilePath workspace, Component component, Stream stream, Project.PromotionLevel level ) {
        super( workspace, component, stream, level );
    }

    @Override
    public List<Baseline> getValidBaselines() throws IOException, InterruptedException {
        List<Baseline> baselines = new ArrayList<Baseline>();

        return workspace.act( new GetRemoteBaselineFromStream( component, stream, level, false, null ) );
    }
}
