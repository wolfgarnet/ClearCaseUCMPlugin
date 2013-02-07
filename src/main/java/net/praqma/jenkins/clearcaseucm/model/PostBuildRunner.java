package net.praqma.jenkins.clearcaseucm.model;

import java.util.List;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:30
 */
public abstract class PostBuildRunner {

    public abstract List<Runner> getRunners();
}
