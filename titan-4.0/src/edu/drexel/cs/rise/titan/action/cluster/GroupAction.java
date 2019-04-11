/*
 * GroupAction.java
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
package edu.drexel.cs.rise.titan.action.cluster;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.ui.ClusterViewer;
import edu.drexel.cs.rise.titan.util.ActionUtilities;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.titan.util.IconFactory;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class GroupAction extends AbstractAction
{
	private static final long serialVersionUID = 10L;

	protected final ClusterViewer viewer;
	protected final Project proj;
	
	public GroupAction(final Project proj,final ClusterViewer viewer)
	{
		this.proj = proj;
		this.viewer = viewer;

		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Group");
		putValue(SHORT_DESCRIPTION, "Group");
		putValue(SMALL_ICON, IconFactory.load("group.png"));
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionUtilities.CMD_KEY));
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		final TreeSelectionModel select = viewer.getSelectionModel();
		if (select.isSelectionEmpty())
			return;

		final TreePath parentPath = select.getSelectionPath().getParentPath();
		final ClusterSet common = parentPath != null ?
				(ClusterSet) parentPath.getLastPathComponent() : null;

		if (common == null)
			return;

		final String name = queryName();
		if (name == null)
			return;
		else if (!ClusterUtilities.isValidName(name)) {
			JOptionPane.showMessageDialog(null, "Invalid group name.",
					"Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}

		final JTree tree = viewer.getTree();
		final ClusterSet set = new ClusterSet(name, common);
		final int[] rows = select.getSelectionRows();
		Arrays.sort(rows);

		int index = tree.getRowCount();
		for (int i = rows.length - 1; i >= 0; --i) {
			final TreePath path = tree.getPathForRow(rows[i]);
			final TreePath parent = path.getParentPath();
			if (parent == null || parent.getLastPathComponent() != common)
				return;

			final Clustering cls = (Clustering) path.getLastPathComponent();

			final int k = common.getClusterIndex(cls);
			if (k < index)
				index = k;

			common.removeCluster(cls.getName());
			cls.setParent(set);
			set.addCluster(cls);
		}

		common.addCluster(index, set);
		viewer.getModel().fireChangeEvent(parentPath);
		select.setSelectionPath(parentPath.pathByAddingChild(set));
		proj.setModified(true);
	}

	protected String queryName()
	{
		return JOptionPane.showInputDialog(null, "Enter group name:",
				"Group Name", JOptionPane.PLAIN_MESSAGE);
	}
}
