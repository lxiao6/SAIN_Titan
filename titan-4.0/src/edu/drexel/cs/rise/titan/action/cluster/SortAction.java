/*
 * SortAction.java
 * Copyright (c) 2010, Drexel University
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.ui.ClusterViewer;

/**
 *
 * @author Sunny Wong
 * @since 1.0
 */
public class SortAction extends AbstractAction
{
	private static final long serialVersionUID = 10L;

	protected final ClusterViewer viewer;
	protected final Project proj;
	
	public SortAction(final Project proj,final ClusterViewer viewer)
	{
		this.proj = proj;
		this.viewer = viewer;

		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Sort");
		putValue(SHORT_DESCRIPTION, "Sort children");
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		final TreeSelectionModel select = viewer.getSelectionModel();
		if (select.isSelectionEmpty())
			return;

		final TreePath path = select.getLeadSelectionPath();
		if (!(path.getLastPathComponent() instanceof ClusterSet))
			return;

		final ClusterSet set = (ClusterSet) path.getLastPathComponent();

		final List<Clustering> children = new ArrayList<Clustering>();
		for (Clustering child : set)
			children.add(child);
		set.clear();

		Collections.sort(children, new Comparator<Clustering>() {
			@Override
			public int compare(final Clustering a, final Clustering b)
			{
				return a.getName().compareToIgnoreCase(b.getName());
			}
		});
		
		for (Clustering child : children)
			set.addCluster(child);

		viewer.getModel().fireChangeEvent(path);
		proj.setModified(true);
	}
}
