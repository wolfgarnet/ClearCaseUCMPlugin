package net.praqma.jenkins.clearcaseucm.changelog;

import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogParser;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;
import hudson.util.Digester2;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * @author Troels Selch
 * @author Margit Bennetzen
 * 
 */
public class ClearCaseChangeLogParser extends ChangeLogParser {

	protected static Logger logger = Logger.getLogger( ClearCaseChangeLogParser.class.getName() );

	@Override
	public ChangeLogSet<? extends Entry> parse( AbstractBuild build, File changelogFile ) throws IOException, SAXException {
		List<ClearCaseUCMChangeLogEntry> entries = new ArrayList<ClearCaseUCMChangeLogEntry>();

		// Source: http://wiki.hudson-ci.org/display/HUDSON/Change+log

		Digester digester = new Digester2();
		digester.push( entries );
		digester.addObjectCreate( "*/entry/activity", ClearCaseUCMChangeLogEntry.class );
		digester.addSetProperties( "*/entry/activity" );
		digester.addBeanPropertySetter( "*/entry/activity/file", "nextFilepath" );
		digester.addBeanPropertySetter( "*/entry/activity/actName" );
		digester.addBeanPropertySetter( "*/entry/activity/actHeadline" );
		digester.addBeanPropertySetter( "*/entry/activity/author", "myAuthor" );
		digester.addSetNext( "*/entry/activity", "add" );
		FileReader reader = new FileReader( changelogFile );
		try {
			digester.parse( reader );
		} catch( Exception e ) {
			System.out.println( "Whoops, unable to digest. " + e.getMessage() );
		} finally {
			reader.close();
		}
			
		return new ClearCaseUCMChangeLogSet( build, entries );
	}

}
