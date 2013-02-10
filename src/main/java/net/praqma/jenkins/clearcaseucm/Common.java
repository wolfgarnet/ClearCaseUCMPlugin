package net.praqma.jenkins.clearcaseucm;

import hudson.FilePath;
import hudson.model.TaskListener;
import net.praqma.clearcase.exceptions.ClearCaseException;
import net.praqma.clearcase.exceptions.ViewException;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Project;
import net.praqma.clearcase.ucm.entities.Stream;
import net.praqma.clearcase.ucm.view.SnapshotView;
import net.praqma.clearcase.ucm.view.UCMView;
import net.praqma.hudson.Config;
import net.praqma.hudson.exception.ScmException;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author cwolfgang
 *         Date: 05-02-13
 *         Time: 22:09
 */
public class Common {

    private static Logger logger = Logger.getLogger( Common.class.getName() );

    public static final String NAME = "ClearCaseUCM";
    public static final String PRINTNAME = "[" + NAME + "] ";

    private Common() {

    }


    public static Stream getIntegrationStream( Baseline bl, String buildProject ) throws ScmException {
        Stream stream = null;
        Project project = null;

        /*
           * If the build project was not given as a parameter to the job, try to
           * find hudson, Hudson, jenkins or Jenkins
           */
        if( buildProject == null ) {
            try {
                project = Project.get( "hudson", bl.getPVob() ).load();
            } catch( Exception eh ) {
                logger.log( Level.FINEST, "Failed first attempt", eh );
                try {
                    project = Project.get( "Hudson", bl.getPVob() ).load();
                } catch( Exception eH ) {
                    try {
                        project = Project.get( "jenkins", bl.getPVob() ).load();
                    } catch( Exception ej ) {
                        try {
                            project = Project.get( "Jenkins", bl.getPVob() ).load();
                        } catch( Exception eJ ) {
                            logger.severe( "Using current project as build project" );
                            try {
                                project = bl.getStream().load().getProject();
                            } catch( Exception e ) {
                                throw new ScmException( "Could not get a build Project", null );
                            }
                        }
                    }
                }
            }
        } else {
            try {
                project = Project.get( buildProject, bl.getPVob() );
            } catch( Exception e ) {
                //throw new ScmException( "Could not find project 'hudson' in " + pvob + ". You can install the Poject with: \"cleartool mkproject -c \"The special Hudson Project\" -in rootFolder@\\your_pvob hudson@\\your_pvob\"." );
                logger.warning( "The build Project was not found." );

                project = bl.getStream().getProject();
            }
        }

        try {
            project.load();
        } catch( ClearCaseException e1 ) {
            project = bl.getStream().getProject();
            logger.warning( "The project could not be loaded, using " + project.getNormalizedName() );
        }

        try {
            stream = project.getIntegrationStream();
        } catch( Exception e ) {
            throw new ScmException( "Could not get integration stream from " + project.getShortname(), e );
        }

        return stream;
    }

    public static SnapshotView makeView( Stream stream, TaskListener listener, String loadModule, File viewroot, String viewtag ) throws ScmException {
        return makeView( stream, listener, loadModule, viewroot, viewtag, true );
    }

    public static SnapshotView makeView( Stream stream, TaskListener listener, String loadModule, File viewroot, String viewtag, boolean update ) throws ScmException {

        PrintStream hudsonOut = listener.getLogger();
        SnapshotView snapview = null;

        hudsonOut.println( PRINTNAME + "View root: " + viewroot.getAbsolutePath() );
        hudsonOut.println( PRINTNAME + "View tag : " + viewtag );

        boolean pathExists = false;

        /*
           * Determine if there is a view path if not, create it
           */
        try {
            if( viewroot.exists() ) {
                pathExists = true;
                hudsonOut.println( PRINTNAME + "Reusing view root" );
            } else {
                if( viewroot.mkdir() ) {
                } else {
                    throw new ScmException( "Could not create folder for view root:  " + viewroot.toString(), null );
                }
            }
        } catch( Exception e ) {
            throw new ScmException( "Could not make workspace (for viewroot " + viewroot.toString() + "). Cause: " + e.getMessage(), e );

        }

        hudsonOut.println( PRINTNAME + "Determine if view tag exists" );
        if( UCMView.viewExists( viewtag ) ) {
            hudsonOut.println( PRINTNAME + "Reusing view tag" );
            try {
                String vt = SnapshotView.viewrootIsValid( viewroot );
                hudsonOut.println( PRINTNAME + "UUID resulted in " + vt );
                /* Not the correct view tag given the view */
                if( !vt.equals( viewtag ) && pathExists ) {
                    hudsonOut.println( PRINTNAME + "View tag is not the same as " + vt );
                    /* Delete view */
                    FilePath path = new FilePath( viewroot );
                    hudsonOut.println( PRINTNAME + "Trying to delete " + path );
                    try {
                        path.deleteRecursive();
                    } catch( Exception e ) {
                        throw new ScmException( "Unable to recursively prepare view root", e );
                    }
                    makeView( stream, listener, loadModule, viewroot, viewtag, update );
                }
            } catch( ClearCaseException ucmE ) {
                try {
                    hudsonOut.println( PRINTNAME + "Regenerating invalid view root" );
                    UCMView.end( viewtag );
                    SnapshotView.regenerateViewDotDat( viewroot, viewtag );
                } catch( ClearCaseException ucmEx ) {
                    ucmEx.print( hudsonOut );
                    throw new ScmException( "Could not make workspace - could not regenerate view", ucmEx );
                } catch( IOException e ) {
                    throw new ScmException( "Could not make workspace - could not regenerate view", e );
                }
            } catch( Exception e ) {
                hudsonOut.println( PRINTNAME + "Failed making workspace: " + e.getMessage() );
                throw new ScmException( "Failed making workspace", e );
            }

            hudsonOut.println( PRINTNAME + "Getting snapshotview" );
            try {
                snapview = SnapshotView.get( viewroot );
            } catch( ClearCaseException e ) {
                e.print( hudsonOut );
                throw new ScmException( "Could not get view for workspace", e );
            } catch( IOException e ) {
                throw new ScmException( "Could not get view for workspace", e );
            }
        } else {
            try {
                hudsonOut.println( PRINTNAME + "Creating new view" );
                snapview = SnapshotView.create( stream, viewroot, viewtag );

                hudsonOut.println( PRINTNAME + "Created new view in local workspace: " + viewroot.getAbsolutePath() );
            } catch( ClearCaseException e ) {
                e.print( hudsonOut );
                throw new ScmException( "View not found in this region, but views with viewtag '" + viewtag + "' might exist in the other regions. Try changing the region Hudson or the slave runs in.", e );
            } catch( IOException e ) {
                throw new ScmException( "Unable to create view: " + e.getMessage(), e );
            }
        }

        if( update ) {
            try {
                hudsonOut.println( PRINTNAME + "Updating view using " + loadModule.toLowerCase() + " modules." );
                snapview.Update( true, true, true, false, new SnapshotView.LoadRules( snapview, SnapshotView.Components.valueOf( loadModule.toUpperCase() ) ) );
            } catch( ClearCaseException e ) {
                e.print( hudsonOut );
                if( e instanceof ViewException ) {
                    if( ((ViewException)e).getType().equals( ViewException.Type.REBASING ) ) {
                        hudsonOut.println( PRINTNAME + "The view is currently being used to rebase another stream" );
                    }
                }
                throw new ScmException( "Could not update snapshot view", e );
            }
        }

        return snapview;
    }

}
