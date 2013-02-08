package net.praqma.jenkins.clearcaseucm.modes;

import hudson.Extension;
import hudson.FilePath;
import hudson.model.AbstractBuild;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.clearcaseucm.ClearCaseUCMAction;
import net.praqma.jenkins.clearcaseucm.model.*;
import net.praqma.jenkins.clearcaseucm.runners.PrintRunner;
import net.praqma.jenkins.clearcaseucm.strategies.changelog.EmptyChangeLogProducer;
import net.praqma.jenkins.clearcaseucm.strategies.initializers.EmptyWorkspaceInitializer;
import net.praqma.jenkins.clearcaseucm.strategies.selectors.NoStreamSelector;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 13:10
 */
public class DumbMode extends AbstractMode {

    @DataBoundConstructor
    public DumbMode() {
        super( null, null, null );
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
        return new NoStreamSelector( workspace, component, stream, level );
    }

    @Override
    public WorkspaceInitializer getWorkspaceInitializer( AbstractBuild<?, ?> build ) {
        return new EmptyWorkspaceInitializer( build );
    }

    @Override
    public ChangeLogProducer getChangeLogProducer( FilePath workspace ) {
        return new EmptyChangeLogProducer( workspace );
    }

    @Override
    public List<Runner> getRunners( ClearCaseUCMAction action ) {
        List<Runner> runners = new ArrayList<Runner>();

        runners.add( new PrintRunner( "PRINTING: " + action.getBaseline().getNormalizedName() ) );

        return runners;
    }

    @Extension
    public static class DumbDescriptor extends ClearCaseUCMModeDescriptor<DumbMode> {

        @Override
        public String getDisplayName() {
            return "Dumb mode";
        }
    }

    @Override
    public String toString() {
        return "DUMB!!!";
    }
}
