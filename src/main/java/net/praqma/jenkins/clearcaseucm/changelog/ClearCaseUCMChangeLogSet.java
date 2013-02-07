package net.praqma.jenkins.clearcaseucm.changelog;

import hudson.model.AbstractBuild;
import hudson.scm.ChangeLogSet;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class represents a ChangeLogSet. This is the accumulation of all the
 * entries on a baseline
 * 
 * @author Troels Selch
 * @author Margit Bennetzen
 * 
 */

public class ClearCaseUCMChangeLogSet extends ChangeLogSet<ClearCaseUCMChangeLogEntry> {

	protected static Logger logger = Logger.getLogger( ClearCaseUCMChangeLogSet.class.getName()  );
	private List<ClearCaseUCMChangeLogEntry> entries = null;
	private String baselineName;

	protected ClearCaseUCMChangeLogSet( AbstractBuild<?, ?> build, List<ClearCaseUCMChangeLogEntry> entries ) {
		super( build );

		this.entries = Collections.unmodifiableList( entries );
		for( ClearCaseUCMChangeLogEntry entry : entries ) {
			entry.setParent( this );
		}
	}

	public Iterator<ClearCaseUCMChangeLogEntry> iterator() {
		return entries.iterator();
	}

	@Override
	public boolean isEmptySet() {
		return entries.isEmpty();
	}

	/**
	 * Used by index.jelly to display list of entries
	 * 
	 * @return
	 */
	public List<ClearCaseUCMChangeLogEntry> getEntries() {
		return entries;
	}

	public void setBaselineName( String baselineName ) {
		this.baselineName = baselineName;
	}

	public String getBaselineName() {
		return baselineName;
	}
}
