package net.praqma.jenkins.clearcaseucm.junit;

import net.praqma.util.test.junit.LoggingRule;
import org.junit.ClassRule;

/**
 * User: cwolfgang
 * Date: 08-11-12
 * Time: 11:11
 */
public class BaseTestClass {
    @ClassRule
    public static ClearCaseUCMRule jenkins = new ClearCaseUCMRule();

    @ClassRule
    public static LoggingRule lrule = new LoggingRule( "net.praqma" );

}
