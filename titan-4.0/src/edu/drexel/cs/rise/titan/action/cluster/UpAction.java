/*
 * UpAction.java
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.ui.ClusterViewer;
import edu.drexel.cs.rise.titan.util.ActionUtilities;
import edu.drexel.cs.rise.titan.util.IconFactory;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class UpAction extends AbstractAction
{
	private static final long serialVersionUID = 10L;

	protected final ClusterViewer viewer;
	protected final Project proj;
	public UpAction(final Project proj,final ClusterViewer viewer)
	{
		this.proj = proj;
		this.viewer = viewer;

		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Move Up");
		putValue(SHORT_DESCRIPTION, "Move Up");
		putValue(SMALL_ICON, IconFactory.load("up.png"));
		putValue(ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_UP,
						ActionUtilities.CMD_KEY));
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

		final JTree tree = viewer.getTree();
		final List<TreePath> changed = new ArrayList<TreePath>();
		final int[] rows = select.getSelectionRows();
		Arrays.sort(rows);

		for (int i : rows) {
			final TreePath path = tree.getPathForRow(i);
			final TreePath parent = path.getParentPath();
			if (parent == null || parent.getLastPathComponent() != common)
				return;

			final Clustering cls = (Clustering) path.getLastPathComponent();
			final int index = common.getClusterIndex(cls);

			if (index == 0)
				continue;

			common.removeCluster(cls.getName());
			common.addCluster(index - 1, cls);
			changed.add(path);
		}

		viewer.getModel().fireChangeEvent(parentPath);
		select.setSelectionPaths(changed.toArray(new TreePath[changed.size()]));
		proj.setModified(true);
	}
}
