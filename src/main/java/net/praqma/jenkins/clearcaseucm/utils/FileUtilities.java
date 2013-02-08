package net.praqma.jenkins.clearcaseucm.utils;

import net.praqma.hudson.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 *         Date: 06-02-13
 *         Time: 22:45
 */
public class FileUtilities {

    private static Logger logger = Logger.getLogger( FileUtilities.class.getName() );

    private FileUtilities() {
    }

    public static void writeToFile( File file, String content ) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream( file );
            fos.write( content.getBytes() );
            fos.close();
        } finally {
            if( fos != null ) {
                fos.close();
            }
        }
    }

    public static File getViewRoot( File workspace, String subPath ) {
        if( subPath != null && !subPath.isEmpty() ) {
            return new File( workspace, subPath );
        } else {
            return workspace;
        }
    }
}
