package net.praqma.jenkins.clearcaseucm.utils;

import net.praqma.clearcase.exceptions.ClearCaseException;
import net.praqma.clearcase.ucm.entities.Activity;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Version;
import net.praqma.clearcase.ucm.utils.VersionList;

import java.util.List;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 *         Date: 07-02-13
 *         Time: 11:57
 */
public class ChangeLogUtils {

    private static Logger logger = Logger.getLogger( ChangeLogUtils.class.getName() );

    private ChangeLogUtils() {

    }

    public static String createChangelog( List<Activity> changes, Baseline bl ) {
        StringBuffer buffer = new StringBuffer();

        buffer.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
        buffer.append( "<changelog>" );
        buffer.append( "<changeset>" );
        buffer.append( "<entry>" );
        buffer.append( ( "<blName>" + bl.getShortname() + "</blName>" ) );
        for( Activity act : changes ) {
            try {
                act.load();
                buffer.append( "<activity>" );
                buffer.append( ( "<actName>" + act.getShortname() + "</actName>" ) );
                buffer.append( ( "<actHeadline>" + act.getHeadline() + "</actHeadline>" ) );
                buffer.append( ( "<author>" + act.getUser() + "</author>" ) );
                VersionList versions = new VersionList( act.changeset.versions ).getLatest();
                String temp = null;
                for( Version v : versions ) {
                    try {
                        temp = "<file>" + v.getSFile() + " (" + v.getVersion() + ") user: " + v.blame() + "</file>";
                    } catch( ClearCaseException e ) {
                        logger.warning( "Could not generate log" );
                    }
                    buffer.append( temp );
                }
                buffer.append( "</activity>" );
            } catch( ClearCaseException e ) {
                logger.warning( "Unable to use activity \"" + act.getNormalizedName() + "\": " + e.getMessage() );
            }
        }
        buffer.append( "</entry>" );
        buffer.append( "</changeset>" );

        buffer.append( "</changelog>" );

        return buffer.toString();
    }
}
