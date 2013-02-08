package net.praqma.jenkins.clearcaseucm.modes;

import hudson.FilePath;
import hudson.model.AbstractBuild;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.clearcaseucm.model.*;
import net.praqma.jenkins.clearcaseucm.strategies.changelog.PreviousBaseline;
import net.praqma.jenkins.clearcaseucm.strategies.initializers.TypicalWorkspaceInitializerStrategy;
import net.praqma.jenkins.clearcaseucm.strategies.selectors.*;

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
    public String getName() {
        return "Self mode";
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
    public WorkspaceInitializer getWorkspaceInitializer( AbstractBuild<?, ?> build ) {
        return new TypicalWorkspaceInitializerStrategy( build );
    }

    @Override
    public ChangeLogProducer getChangeLogProducer( FilePath workspace ) {
        return new PreviousBaseline( workspace );
    }
}
