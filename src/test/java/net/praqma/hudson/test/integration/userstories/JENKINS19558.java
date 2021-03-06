package net.praqma.hudson.test.integration.userstories;

import hudson.model.AbstractBuild;
import hudson.model.Project;
import hudson.model.Result;
import net.praqma.clearcase.Deliver;
import net.praqma.clearcase.test.junit.ClearCaseRule;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.hudson.CCUCMBuildAction;
import net.praqma.hudson.test.BaseTestClass;
import net.praqma.hudson.test.CCUCMRule;
import net.praqma.hudson.test.SystemValidator;
import net.praqma.util.test.junit.DescriptionRule;
import net.praqma.util.test.junit.TestDescription;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author cwolfgang
 */
public class JENKINS19558 extends BaseTestClass {

    @Rule
    public static ClearCaseRule ccenv = new ClearCaseRule( "JENKINS-19558", "setup-bl-on-dev.xml" );

    @Rule
    public static DescriptionRule desc = new DescriptionRule();

    //@Test
    @TestDescription( title = "JENKINS-19558", text = "Failed deliver looses changeset, no changelog written" )
    public void jenkins19558() throws Exception {
        Project project = new CCUCMRule.ProjectCreator( "JENKINS-19558", "_System@" + ccenv.getPVob(), "one_int@" + ccenv.getPVob() ).setSwipe( false ).setType( CCUCMRule.ProjectCreator.Type.child ).getProject();

        /* I need a first build */
        AbstractBuild build1 = jenkins.getProjectBuilder( project ).build();
        CCUCMBuildAction action1 = build1.getAction( CCUCMBuildAction.class );
        action1.getViewPath();

        Stream target = ccenv.context.streams.get( "one_int" );
        Stream source = ccenv.context.streams.get( "one_dev" );

        System.out.println( "SOURCE: " + source );
        System.out.println( "TARGET: " + target );
        System.out.println( "PATH: " + action1.getViewPath() );
        System.out.println( "TAG: " + action1.getViewTag() );
        Deliver deliver = new Deliver( source, target, action1.getViewPath(), action1.getViewTag() );
        boolean b = deliver.deliver( true, false, false, false );
        System.out.println( "DELIVER: " + b );


        AbstractBuild build2 = jenkins.getProjectBuilder( project ).build();

        System.out.println( "1CHANGE LOG: " + build2.getChangeSet() );
        System.out.println( "1CHANGE LOG: " + build2.getChangeSet().getClass() );

        new SystemValidator( build2 ).
                validateBuild( Result.FAILURE ).
                checkChangeset( 1 ).
                validate();
    }


}
