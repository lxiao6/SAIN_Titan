/*
 * RenameAction.java
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

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.ui.ClusterViewer;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class RenameAction extends AbstractAction
{
	private static final long serialVersionUID = 10L;

	private final ClusterViewer viewer;
	protected final Project proj;
	public RenameAction(final Project proj,final ClusterViewer viewer)
	{
		this.viewer = viewer;
		this.proj = proj;
		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Rename");
		putValue(SHORT_DESCRIPTION, "Rename");
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		final TreeSelectionModel select = viewer.getSelectionModel();
		if (select.isSelectionEmpty())
			return;

		final TreePath path = select.getSelectionPath();

		final String name = queryName();
		if (name == null)
			return;
		else if (!ClusterUtilities.isValidName(name)) {
			JOptionPane.showMessageDialog(null, "Invalid group name.",
					"Invalid Input", JOptionPane.WARNING_MESSAGE);
			return;
		}

		final Clustering cls = (Clustering) path.getLastPathComponent();
		cls.setName(name);
		viewer.getModel().fireChangeEvent(path);
		proj.setModified(true);
	}

	protected String queryName()
	{
		return JOptionPane.showInputDialog(null, "Enter new group name:",
				"Group Name", JOptionPane.PLAIN_MESSAGE);
	}
}
