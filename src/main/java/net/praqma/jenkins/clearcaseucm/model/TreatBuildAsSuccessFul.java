package net.praqma.jenkins.clearcaseucm.model;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 12:36
 */
public class TreatBuildAsSuccessFul {
    public static TreatBuildAsSuccessFul asSuccess = new TreatBuildAsSuccessFul( true );

    private boolean successful;

    public TreatBuildAsSuccessFul( boolean b ) {
        this.successful = b;
    }

}
