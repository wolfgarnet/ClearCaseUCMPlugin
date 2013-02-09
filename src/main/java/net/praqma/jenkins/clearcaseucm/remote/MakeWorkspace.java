package net.praqma.jenkins.clearcaseucm.remote;

import hudson.FilePath;
import hudson.model.TaskListener;
import hudson.remoting.VirtualChannel;
import net.praqma.clearcase.PVob;
import net.praqma.clearcase.Rebase;
import net.praqma.clearcase.exceptions.ClearCaseException;
import net.praqma.clearcase.exceptions.RebaseException;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.clearcase.ucm.view.SnapshotView;
import net.praqma.hudson.Config;
import net.praqma.hudson.exception.ScmException;
import net.praqma.jenkins.clearcaseucm.Common;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 22:34
 */
public class MakeWorkspace implements FilePath.FileCallable<Boolean> {
    private static Logger logger = Logger.getLogger( MakeWorkspace.class.getName() );

    private String viewtag;
    private String subPath;

    private String loadModule;
    private TaskListener listener;

    private String buildProject;

    private Baseline selectedBaseline;
    private Stream targetStream;

    public MakeWorkspace( TaskListener listener, String viewtag, String subPath, String buildProject, Baseline selectedBaseline, Stream targetStream, String loadModule ) {
        this.listener = listener;
        this.viewtag = viewtag;
        this.subPath = subPath;
        this.buildProject = buildProject;
        this.selectedBaseline = selectedBaseline;
        this.targetStream = targetStream;
        this.loadModule = loadModule;
    }

    @Override
    public Boolean invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {
        File viewroot = null;
        if( subPath != null && !subPath.isEmpty() ) {
            viewroot = new File( f, subPath );
        } else {
            viewroot = f;
        }
        try {
            makeWorkspace( viewroot, viewtag );
        } catch( Exception e ) {
            throw new IOException( e );
        }

        return null;
    }

    private void makeWorkspace( File viewroot, String viewtag ) throws ScmException, ClearCaseException {

        PrintStream out = listener.getLogger();

        logger.fine( "Creating dev stream" );
        Stream devstream = getDeveloperStream( "stream:" + viewtag, targetStream.getPVob() );

        logger.fine( "Making view" );
        SnapshotView sv = Common.makeView( devstream, listener, loadModule, viewroot, viewtag, false );


        // Now we have to rebase - if a rebase is in progress, the
        // old one must be stopped and the new started instead
        logger.fine( "Checking rebasing" );
        if( Rebase.isInProgress( devstream ) ) {
            out.print( "[" + Config.nameShort + "] Cancelling previous rebase." );
            Rebase.cancelRebase( devstream );
            out.println( " Done" );
        }

        out.print( "[" + Config.nameShort + "] Rebasing development stream (" + devstream.getShortname() + ") against parent stream (" + targetStream.getShortname() + ")" );
        try {
            logger.fine( "Rebasing" );
            Rebase rebase = new Rebase( devstream, sv, selectedBaseline );
            rebase.rebase( true );
            logger.fine( "Rebasing done" );
        } catch( RebaseException e1 ) {
            logger.fine( "Rebasing failed: " + e1.getMessage() );
            out.println( " Failed" );
            throw e1;
        }

        out.println( " Done" );

        try {
            out.println("[" + Config.nameShort + "] Updating view using " + loadModule.toLowerCase() + " modules");
            logger.fine( "Updating stream" );
            //sv.Update(true, true, true, false, Components.valueOf(loadModule.toUpperCase()), null);
            sv.Update(true, true, true, false, new SnapshotView.LoadRules( sv, SnapshotView.Components.valueOf( loadModule.toUpperCase() ) ));
            logger.fine( "Updating done" );
        } catch (ClearCaseException e) {
            e.print( out );
            throw new ScmException("Could not update snapshot view", e );
        }
    }


    private Stream getDeveloperStream( String streamname, PVob pvob ) throws ScmException {
        Stream devstream = null;

        try {
            if( Stream.streamExists( streamname + "@" + pvob ) ) {
                devstream = Stream.get( streamname, pvob );
            } else {
                if( buildProject != null && buildProject.isEmpty() ) {
                    buildProject = null;
                }
                devstream = Stream.create( Common.getIntegrationStream( selectedBaseline, buildProject ), streamname + "@" + pvob, true, selectedBaseline );
            }
        }
        /*
           * This tries to handle the issue where the project hudson is not
           * available
           */
        catch (ScmException se) {
            throw se;

        } catch (Exception e) {
            throw new ScmException( "Could not get stream", e );
        }

        return devstream;
    }

}
