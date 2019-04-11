/*
 * ExportMatrixAction.java
 * Copyright (c) 2009, Drexel University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Drexel University nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.drexel.cs.rise.titan.action.export;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.minos.dsm.FileWriter;
import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.ClusterVisitor;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.action.FileAction;
import edu.drexel.cs.rise.titan.ui.MatrixViewer;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.titan.util.IconFactory;
import edu.drexel.cs.rise.util.Digraph;
import edu.drexel.cs.rise.util.Matrix;
import edu.drexel.cs.rise.util.WeightedDigraph;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class ExportWeightedStructureMatrixAction extends FileAction
{
	private static final long serialVersionUID = 10L;
	
	protected final Project proj;

	private static final String extension = ".dsm";
	private final MatrixViewer viewer;
	
	public ExportWeightedStructureMatrixAction(final Project proj,final Component parent, final MatrixViewer viewer)
	{
		super(proj, parent);
		this.viewer = viewer;
		this.proj = proj;
		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Export Structure to Weighted DSM...");
		putValue(MNEMONIC_KEY, (int)'D');
		putValue(SHORT_DESCRIPTION, "Export Structure DSM File");
		putValue(SMALL_ICON, IconFactory.load("dsm.png"));

		final FileFilter filter = new FileNameExtensionFilter(
				"DSM file (*.dsm)", "dsm");
		chooser.setFileFilter(filter);
		chooser.setDialogTitle("Export DSM File");
		chooser.setMultiSelectionEnabled(false);
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		prepare();
		if (showSaveDialog() != JFileChooser.CANCEL_OPTION) {
			final File path = getPathWithExtension(chooser.getSelectedFile(),
					extension);
			if (!confirmOverwrite(path))
				return;

			export(path);
		}
	}

	protected void export(final File path)
	{
		final WeightedDigraph<String> dsm =  ClusterUtilities.getStructureGraph(proj);
		
		WeightedDigraph<String> ex_dsm = proj.getDPControl().filter(dsm);
		String[] selected = proj.getSelectedDpTypes();
		
		

		
			try {
				FileWriter.saveweighted(selected,ex_dsm, null, path);
			} catch (MinosException e) {
				JOptionPane.showMessageDialog(parent,
						"Unable to export DSM file: " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
	}

	protected WeightedDigraph<String> buildGraph(final Matrix<Integer, String> matrix)
	{
		final List<String> variables = new ArrayList<String>();
		final WeightedDigraph<String> dsm = new WeightedDigraph<String>();

		for (String var : matrix.getLabels()) {
			final String name = renameLabel(var);
			variables.add(name);
			dsm.addVertex(name);
		}

		for (int row = 0; row < matrix.length(); ++row) {
			for (int col = 0; col < matrix.length(); ++col) {
				if (row == col)
					continue;

				final Integer dep = matrix.get(row, col);
				if (!(dep == null || dep.intValue() == 0))
					dsm.addEdge(variables.get(col), variables.get(row),matrix.get(row, col).toString());
			}
		}
		
		return dsm;
	}

	protected Clustering buildCluster(final WeightedDigraph<String> dsm)
	{
		final Clustering orig = proj.getCluster();
		final ClusterSet root = new ClusterSet("$root");

		orig.visit(new ClusterVisitor() {
			@Override
			public void visit(final ClusterSet set)
			{
				final String name = set.getName();
				if (dsm.containsVertex(name))
					root.addCluster(new ClusterItem(name, root));
				else {
					for (Clustering cls : set)
						cls.visit(this);
				}
			}

			@Override
			public void visit(final ClusterItem item)
			{
				final String name = item.getName();
				if (dsm.containsVertex(name))
					root.addCluster(new ClusterItem(name, root));
			}
		});

		return root;
	}

	protected String renameLabel(final String label)
	{
		return label.replaceFirst("^\\d+\\s+", "");
	}
}
