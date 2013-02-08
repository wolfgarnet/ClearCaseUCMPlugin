package net.praqma.jenkins.clearcaseucm.remote;

import hudson.FilePath.FileCallable;
import hudson.model.BuildListener;
import hudson.remoting.VirtualChannel;
import net.praqma.clearcase.ucm.entities.Baseline;
import net.praqma.clearcase.ucm.entities.Baseline.LabelBehaviour;
import net.praqma.clearcase.ucm.entities.Component;
import net.praqma.jenkins.clearcaseucm.utils.FileUtilities;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

public class CreateRemoteBaseline implements FileCallable<Baseline> {

	private String baseName;
	private Component component;
	private String subPath;
	private BuildListener listener;
	private String username;

	public CreateRemoteBaseline( String baseName, Component component, String subPath, String username, BuildListener listener ) {
		this.baseName = baseName;
		this.component = component;
		this.subPath = subPath;
		this.listener = listener;
		this.username = username;
	}

	@Override
	public Baseline invoke( File f, VirtualChannel channel ) throws IOException, InterruptedException {

        File view = FileUtilities.getViewRoot( f, subPath );

		Baseline bl = null;
		try {
			bl = Baseline.create( baseName, component, view, LabelBehaviour.INCREMENTAL, false );
		} catch( Exception e ) {
			throw new IOException( "Unable to create remote Baseline:" + e.getMessage(), e );
		}

		return bl;
	}

}
