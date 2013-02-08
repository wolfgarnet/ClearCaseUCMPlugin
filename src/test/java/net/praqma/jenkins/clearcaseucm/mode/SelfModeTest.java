package net.praqma.jenkins.clearcaseucm.mode;

import hudson.model.AbstractBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import net.praqma.clearcase.test.annotations.ClearCaseUniqueVobName;
import net.praqma.clearcase.test.junit.ClearCaseRule;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.jenkins.clearcaseucm.ClearCaseUCMScm;
import net.praqma.jenkins.clearcaseucm.junit.BaseTestClass;
import net.praqma.jenkins.clearcaseucm.junit.ClearCaseUCMRule;
import net.praqma.jenkins.clearcaseucm.junit.SystemValidator;
import net.praqma.jenkins.clearcaseucm.model.AbstractMode;
import net.praqma.jenkins.clearcaseucm.modes.SelfMode;
import org.junit.Rule;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author cwolfgang
 *         Date: 08-02-13
 *         Time: 23:37
 */
public class SelfModeTest extends BaseTestClass {

    @Rule
    public static ClearCaseRule ccenv = new ClearCaseRule( "clearcaseucm" );

    @Test
    @ClearCaseUniqueVobName( name = "none" )
    public void test() throws IOException, ExecutionException, InterruptedException {
        Stream stream = ccenv.context.streams.get( "one_int" );
        Component component = ccenv.context.components.get( "_System" );
        Project.PromotionLevel level = Project.PromotionLevel.INITIAL;

        AbstractMode mode = new SelfMode( component, stream, level );

        ClearCaseUCMScm scm = new ClearCaseUCMScm( mode );

        FreeStyleProject project = jenkins.createProject( "self-text", scm );

        AbstractBuild build = new ClearCaseUCMRule.ProjectBuilder( project ).build();

        Baseline baseline = ccenv.context.baselines.get( "model-1" );

        new SystemValidator( build ).validateBuild( Result.SUCCESS ).validateBuiltBaseline( Project.PromotionLevel.INITIAL, baseline );
    }
}
