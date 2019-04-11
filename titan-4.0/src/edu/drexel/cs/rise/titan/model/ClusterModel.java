/*
 * ClusterModel.java
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
package edu.drexel.cs.rise.titan.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;

/**
 *
 * @author Sunny Wong
 * @author Kanwarpreet Sethi
 * @since 0.2
 */
public class ClusterModel implements TreeModel
{
	private Clustering root;
	private final List<TreeModelListener> listeners =
			Collections.synchronizedList(new ArrayList<TreeModelListener>());

	public ClusterModel()
	{
		this(null);
	}

	public ClusterModel(final Clustering root)
	{
		this.root = root;
	}

	@Override
	public Clustering getRoot()
	{
		return root;
	}

	public void setRoot(final Clustering cls)
	{
		root = cls;
		fireChangeEvent(new TreePath(root));
	}

	@Override
	public Clustering getChild(final Object parent, int index)
	{
		final ClusterSet set = castToClusterSet(parent);
		return set.getCluster(index);
	}

	@Override
	public int getChildCount(final Object parent)
	{
		final ClusterSet set = castToClusterSet(parent);
		return set.clusters().size();
	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child)
	{
		final ClusterSet set = castToClusterSet(parent);
		final Clustering cls = castToClustering(child);
		return set.getClusterIndex(cls);
	}

	@Override
	public boolean isLeaf(final Object node)
	{
		if (node == null)
			throw new NullPointerException();
		else if (node instanceof ClusterItem)
			return true;
		else if (node instanceof ClusterSet)
			return false;
		else
			throw new IllegalArgumentException();
	}

	@Override
	public void valueForPathChanged(final TreePath path, final Object value)
	{
		final Object last = path.getLastPathComponent();
		final Clustering cls = castToClustering(last);

		if (value instanceof String)
			cls.setName(value.toString());
		else if (value instanceof Clustering) {
			final ClusterSet parent = castToClusterSet(path.getParentPath()
					.getLastPathComponent());
			parent.removeCluster(cls.getName());
			parent.addCluster(castToClustering(value));
		}
	}

	protected static final Clustering castToClustering(final Object node)
	{
		if (node == null)
			throw new NullPointerException();
		else if (node instanceof Clustering)
			return (Clustering) node;
		else
			throw new IllegalArgumentException();
	}

	protected static final ClusterSet castToClusterSet(final Object node)
	{
		if (node == null)
			throw new NullPointerException();
		else if (node instanceof ClusterSet)
			return (ClusterSet) node;
		else
			throw new IllegalArgumentException();
	}

	public void fireChangeEvent(final TreePath source)
	{
		final List<TreeModelListener> listers = getCurrentListeners(listeners);
		
		final TreeModelEvent event = new TreeModelEvent(this, source);
		for (TreeModelListener list : listers)
			list.treeStructureChanged(event);
	}
	
	public void fireChangeEvent(final TreePath parent, int[] indices,
			Clustering[] children)
	{
		final List<TreeModelListener> listers = getCurrentListeners(listeners);
		
		final TreeModelEvent event = new TreeModelEvent(this, parent, indices,
				children);
		for (TreeModelListener list : listers)
			list.treeStructureChanged(event);
	}

	@Override
	public synchronized void addTreeModelListener(
			final TreeModelListener listener)
	{
		listeners.add(listener);
	}

	@Override
	public synchronized void removeTreeModelListener(
			final TreeModelListener listener)
	{
		listeners.remove(listener);
	}

	protected static final <T> List<T> getCurrentListeners(final List<T> list)
	{
		synchronized (list) {
			return new ArrayList<T>(list);
		}
	}
}
