package net.praqma.jenkins.clearcaseucm.modes;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.clearcaseucm.model.*;
import net.praqma.jenkins.clearcaseucm.runners.PrintRunner;
import net.praqma.jenkins.clearcaseucm.strategies.initializers.EmptyWorkspaceInitializer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 13:10
 */
public class DumbMode extends AbstractMode {

    public DumbMode( Component component, Stream stream, Project.PromotionLevel level ) {
        super( component, stream, level );
    }

    @Override
    public String getName() {
        return "DUMB!";
    }

    @Override
    public Poller getPoller() {
        return null;
    }

    @Override
    public BaselineSelector getBaselineSelector( FilePath workspace ) {
        return null;
    }

    @Override
    public WorkspaceInitializer getWorkspaceInitializer( AbstractBuild<?, ?> build, FilePath workspace ) {
        return new EmptyWorkspaceInitializer( build, workspace );
    }

    @Override
    public ChangeLogProducer getChangeLogProducer( FilePath workspace ) {
        return null;
    }

    @Override
    public List<Runner> getRunners( Baseline baseline ) {
        List<Runner> runners = new ArrayList<Runner>();

        runners.add( new PrintRunner( "PRINTING: " + baseline.getNormalizedName() ) );

        return runners;
    }

    @Extension
    public static class DumbDescriptor extends ClearCaseUCMModeDescriptor<DumbMode> {

        @Override
        public String getDisplayName() {
            return "Dumb mode";
        }
    }
}
