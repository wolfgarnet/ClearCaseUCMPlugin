package net.praqma.jenkins.clearcaseucm.mode;

import hudson.model.AbstractBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Hudson;
import hudson.model.Result;
import jenkins.model.Jenkins;
import net.praqma.clearcase.exceptions.ClearCaseException;
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
    public static ClearCaseRule ccenv = new ClearCaseRule( "clearcaseucm", "setup.xml" );

    @Test
    @ClearCaseUniqueVobName( name = "none" )
    public void test() throws IOException, ExecutionException, InterruptedException, ClearCaseException {
        Stream stream = ccenv.context.streams.get( "one_int" );
        Component component = ccenv.context.components.get( "_System" );
        Project.PromotionLevel level = Project.PromotionLevel.INITIAL;

        AbstractMode mode = new SelfMode( component, stream, level );

        ClearCaseUCMScm scm = new ClearCaseUCMScm( mode );

        FreeStyleProject project = jenkins.createProject( "self-test", scm );

        AbstractBuild build = new ClearCaseUCMRule.ProjectBuilder( project ).build();

        Baseline baseline = ccenv.context.baselines.get( "model-1" );
        System.out.println( "BASELINE IS " + baseline );

        new SystemValidator( build ).validateBuild( Result.SUCCESS ).validateBuiltBaseline( Project.PromotionLevel.INITIAL, baseline ).validate();
    }

    @Test
    @ClearCaseUniqueVobName( name = "recommend" )
    public void recommend() throws IOException, ExecutionException, InterruptedException, ClearCaseException {
        Stream stream = ccenv.context.streams.get( "one_int" );
        Component component = ccenv.context.components.get( "_System" );
        Project.PromotionLevel level = Project.PromotionLevel.INITIAL;

        AbstractMode mode = new SelfMode( component, stream, level );
        mode.setRecommendBaseline( true );

        ClearCaseUCMScm scm = new ClearCaseUCMScm( mode );

        FreeStyleProject project = jenkins.createProject( "self-test-recommend", scm );

        AbstractBuild build = new ClearCaseUCMRule.ProjectBuilder( project ).build();

        Baseline baseline = ccenv.context.baselines.get( "model-1" );

        new SystemValidator( build ).validateBuild( Result.SUCCESS ).validateBuiltBaseline( Project.PromotionLevel.INITIAL, baseline, true ).validate();
    }


    @Test
    @ClearCaseUniqueVobName( name = "promote" )
    public void promote() throws IOException, ExecutionException, InterruptedException, ClearCaseException {
        Stream stream = ccenv.context.streams.get( "one_int" );
        Component component = ccenv.context.components.get( "_System" );
        Project.PromotionLevel level = Project.PromotionLevel.INITIAL;

        AbstractMode mode = new SelfMode( component, stream, level );
        mode.setPromoteBaseline( true );

        ClearCaseUCMScm scm = new ClearCaseUCMScm( mode );

        FreeStyleProject project = jenkins.createProject( "self-test-promote", scm );

        AbstractBuild build = new ClearCaseUCMRule.ProjectBuilder( project ).build();

        Baseline baseline = ccenv.context.baselines.get( "model-1" );

        new SystemValidator( build ).validateBuild( Result.SUCCESS ).validateBuiltBaseline( Project.PromotionLevel.BUILT, baseline ).validate();
    }
}
