/*
 * RedrawAction.java
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
package edu.drexel.cs.rise.titan.action;

import java.awt.event.ActionEvent;
import java.util.Set;
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import javax.swing.tree.TreePath;

import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.ClusterVisitor;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.ui.ClusterViewer;
import edu.drexel.cs.rise.titan.ui.MatrixViewer;
import edu.drexel.cs.rise.titan.util.IconFactory;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class RedrawAction extends AbstractAction
{
	private static final long serialVersionUID = 10L;

	protected final ClusterViewer cluster;
	protected final MatrixViewer matrix;
	protected final Project proj;
	
	public RedrawAction(final Project proj,final ClusterViewer cluster, final MatrixViewer matrix)
	{
		this.cluster = cluster;
		this.matrix = matrix;
		this.proj = proj;
		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Redraw");
		putValue(MNEMONIC_KEY, (int)'R');
		putValue(SHORT_DESCRIPTION, "Redraw");
		putValue(SMALL_ICON, IconFactory.load("redraw.png"));
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5"));
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		//final Project proj = Project.getInstance();
		final Set<Clustering> collapsed = proj.getCollapsed();
		collapsed.clear();
		
		proj.getCluster().visit(new ClusterVisitor() {
			private Stack<ClusterSet> path = new Stack<ClusterSet>();

			@Override
			public void visit(final ClusterSet set)
			{
				path.push(set);
				if (!cluster.isExpanded(new TreePath(path.toArray())))
					collapsed.add(set);
				else {
					for (Clustering cls : set)
						cls.visit(this);
				}
				path.pop();
			}

			@Override
			public void visit(final ClusterItem item)
			{
				// ignore
			}
		});

		matrix.update(proj);
	}
}
