/*
 * ClusterSet.java
 * Copyright (c) 2008, Drexel University.
 * All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package edu.drexel.cs.rise.tellus.cluster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Sunny Wong
 * @author Kanwarpreet Sethi
 * @since 0.1
 */
public class ClusterSet extends Clustering
{
	private final List<Clustering> inner = new LinkedList<Clustering>();
	
	public ClusterSet(final String name)
	{
		this(name, null);
	}
	
	public ClusterSet(final String name, final ClusterSet parent)
	{
		super(name, parent);
	}

	@Override
	public Iterator<Clustering> iterator()
	{
		return inner.iterator();
	}

	public void addCluster(final Clustering cls)
	{
		assert cls != null : cls;
		inner.add(cls);
	}

	public void addCluster(int pos, final Clustering cls)
	{
		assert cls != null : cls;
		inner.add(pos, cls);
	}

	public void removeCluster(final String name)
	{
		assert name != null : name;
		Clustering target = null;
		for (Clustering cls : inner) {
			if (cls.getName().equals(name)) {
				target = cls;
				break;
			}
		}
		
		if (target != null)
			inner.remove(target);
	}
	
	public Clustering getCluster(int i)
	{
		return inner.get(i);
	}

	public int getClusterIndex (Clustering cls)
	{
		return inner.indexOf(cls);
	}
	
	public List<String> names()
	{
		final List<String> result = new ArrayList<String>(inner.size());
		for (Clustering cls : inner)
			result.add(cls.getName());

		return Collections.unmodifiableList(result);
	}

	public Collection<Clustering> clusters()
	{
		return inner;
	}

	public void clear()
	{
		inner.clear();
	}

	@Override
	public void visit(final ClusterVisitor visitor)
	{
		visitor.visit(this);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || obj.getClass() != this.getClass())
			return false;
		else {
			final ClusterSet set = (ClusterSet) obj;
			return super.equals(set) && inner.equals(set.inner);
		}
	}

	@Override
	public int hashCode()
	{
		return 5 * super.hashCode();
	}
}
