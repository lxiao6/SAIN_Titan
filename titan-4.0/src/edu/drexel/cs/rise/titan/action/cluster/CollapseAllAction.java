/*
 * CollapseAllAction.java
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
import java.util.Stack;

import javax.swing.AbstractAction;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.ClusterVisitor;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.util.IconFactory;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class CollapseAllAction extends AbstractAction
{
	private static final long serialVersionUID = 10L;

	private final JTree tree;
	protected final Project proj;

	public CollapseAllAction(final Project proj,final JTree tree)
	{
		this.tree = tree;
		this.proj = proj;
		initialize();
	}

	private void initialize()
	{
		putValue(NAME, "Collapse All");
		putValue(SHORT_DESCRIPTION, "Collapse All");
		putValue(SMALL_ICON, IconFactory.load("collapse.png"));
	}

	@Override
	public void actionPerformed(final ActionEvent event)
	{
		execute(tree, proj.getCluster());
	}

	public static void execute(final JTree tree, final Clustering root)
	{
		if (root == null)
			return;

		root.visit(new ClusterVisitor() {
			final Stack<Clustering> path = new Stack<Clustering>();

			@Override
			public void visit(final ClusterSet set)
			{
				path.add(set);
				for (Clustering cls : set)
					cls.visit(this);

				final TreePath p = new TreePath(path.toArray());
				tree.collapsePath(p);
				path.pop();
			}

			@Override
			public void visit(final ClusterItem item)
			{
				// ignore
			}
		});
	}
}
