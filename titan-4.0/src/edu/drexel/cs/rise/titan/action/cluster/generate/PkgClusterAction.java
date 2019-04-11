package edu.drexel.cs.rise.titan.action.cluster.generate;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JOptionPane;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.minos.cluster.FileParser;
import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.util.ClusterBuilder;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.util.WeightedDigraph;

public class PkgClusterAction extends AbstractAction {

	private static final long serialVersionUID = 10L;
	protected final Component parent;

	protected final Project proj;
	
	public PkgClusterAction(final Project proj,final Component parent) {
		this.parent = parent;
		this.proj = proj;
		initialize();
	}
	
	private void initialize()
	{
		putValue(NAME, "Package Cluster");
		putValue(MNEMONIC_KEY, (int)'F');
		putValue(SHORT_DESCRIPTION, "Generate clusters based on the packages structure");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		ClusterBuilder cb = new ClusterBuilder();		
		//final Project proj = Project.getInstance();
		cb.buildClusters(proj.getStructureDependency().vertices());
		
		final Clustering cls = ClusterUtilities.trim(cb.getCluster(), proj.getStructureDependency().vertices());
		proj.clearCollapsed();
		proj.getCollapsed().add(cls);
		proj.setCluster(cb.getCluster());
		proj.setModified(false);
		
		JOptionPane.showMessageDialog(parent,
			    "Package Clustering is done.");

	}
	


}
