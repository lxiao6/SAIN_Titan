/*
 * UndirectedGraph.java
 * Copyright (c) 2009, Drexel University.
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
package edu.drexel.cs.rise.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public class UndirectedGraph<T> implements Graph<T>
{
	protected static class DefaultEdge<V> implements Edge<V>
	{
		private V first, second;
		private String weight;

		public DefaultEdge(final V f, final V s)
		{
			if (f == null || s == null)
				throw new NullPointerException();

			first = f;
			second = s;
		}
		
		public DefaultEdge(final V f, final V s, final String w)
		{
			if (f == null || s == null || w == null)
				throw new NullPointerException();

			first = f;
			second = s;
			weight = w;
			
		}
		
		public V first()
		{
			return first;
		}
		
		public V second()
		{
			return second;
		}
		
		@Override
		public boolean equals(final Object obj)
		{
			if (obj == this)
				return true;
			else if (obj == null || obj.getClass() != this.getClass())
				return false;

			return equals((DefaultEdge<?>) obj);
		}

		public boolean equals(final DefaultEdge<?> e)
		{
			if (e == this)
				return true;
			else if (e == null || e.getClass() != this.getClass())
				return false;
			else
				return (Hasher.equals(e.first, first) &&
						Hasher.equals(e.second, second)) ||
						(Hasher.equals(e.first, second) &&
						Hasher.equals(e.second, first));
		}

		@Override
		@SuppressWarnings("unchecked")
		public Edge<V> clone()
		{
			try {
				return (Edge<V>) super.clone();
			}
			catch (CloneNotSupportedException ex) {
				throw new AssertionError(ex);
			}
		}
		
		@Override
		public int hashCode()
		{
			return Hasher.hashAll(first, second);
		}

		@Override
		public String weight() {
			return weight;
		}

		@Override
		public void setWeight(String w) {
			this.weight = w;
		}
	}

	private final Map<T, Set<T>> neighbors;
	protected int size;
	
	public UndirectedGraph()
	{
		neighbors = new HashMap<T, Set<T>>();
	}

	public UndirectedGraph(final Graph<T> g)
	{
		this();
		for (T v : g.vertices())
			addVertex(v);
		for (Edge<T> e : g.edges())
			addEdge(e);
	}
	
	protected Set<Edge<T>> createEdgeSet()
	{
		return new HashSet<Edge<T>>();
	}

	public static <T> Edge<T> createEdge(final T first, final T second)
	{
		return new DefaultEdge<T>(first, second);
	}

	protected Set<T> createAdjacencyList()
	{
		return new HashSet<T>();
	}
	
	protected Set<T> createVertexSet()
	{
		return new HashSet<T>();
	}
	
	/**
	 * Returns the size (number of edges) of the graph. This operation is
	 * guaranteed to run in constant time, &Theta;(1).
	 */
	public int size()
	{
		return size;
	}
	/**
	 * Returns the order (number of vertices) of the graph. This operation is
	 * guaranteed to run in constant time, &Theta;(1).
	 */
	@Override
	public final int order()
	{
		return neighbors.size();
	}

	@Override
	public Set<T> vertices()
	{
		return Collections.unmodifiableSet(neighbors.keySet());
	}

	@Override
	public Set<Edge<T>> edges()
	{
		final Set<Edge<T>> es = createEdgeSet();
		for (Map.Entry<T, Set<T>> entry : neighbors.entrySet()) {
			final T src = entry.getKey();
			for (T dest : entry.getValue())
				es.add(createEdge(src, dest));
		}

		return Collections.unmodifiableSet(es);
	}

	@Override
	public void addVertex(final T v)
	{
		if (v == null)
			throw new NullPointerException();
			
		if (!neighbors.containsKey(v))
			neighbors.put(v, createAdjacencyList());
	}

	@Override
	public void removeVertex(final T v)
	{
		if (!containsVertex(v))
			return;

		neighbors.remove(v);
		for (Set<T> vars : neighbors.values())
			vars.remove(v);
	}

	@Override
	public final boolean containsVertex(final T v)
	{
		return neighbors.containsKey(v);
	}

	protected final void addEdge(final Edge<T> e)
	{
		addEdge(e.first(), e.second());
	}

	@Override
	public void addEdge(final T first, final T second)
	{
		if (!(containsVertex(first) && containsVertex(second)))
			throw new NoSuchElementException();

		neighbors.get(first).add(second);
		neighbors.get(second).add(first);
		++size;
	}

	@Override
	public final void removeEdge(final Edge<T> e)
	{
		removeEdge(e.first(), e.second());
	}

	@Override
	public void removeEdge(final T first, final T second)
	{
		if (!(containsVertex(first) && containsVertex(second)))
			return;

		--size;
		neighbors.get(first).remove(second);
		neighbors.get(second).remove(first);
	}

	@Override
	public final boolean containsEdge(final Edge<T> e)
	{
		return containsEdge(e.first(), e.second());
	}

	@Override
	public boolean containsEdge(final T first, final T second)
	{
		return containsVertex(first) && neighbors.get(first).contains(second);
	}

	@Override
	public final Set<T> getNeighbors(final T v)
	{
		return Collections.unmodifiableSet(neighbors.get(v));
	}

	@Override
	public final Set<Edge<T>> getEdges(final T v)
	{
		if (!containsVertex(v))
			return null;

		final Set<Edge<T>> es = createEdgeSet();
		for (T u : neighbors.get(v))
			es.add(createEdge(v, u));

		return es;
	}

	@Override
	public final int getDegree(final T v)
	{
		return neighbors.get(v).size();
	}

	@Override
	public final void contract(final Edge<T> e, final T elem)
	{
		contract(e.first(), e.second(), elem);
	}
	
	@Override
	public void contract(final T first, final T second, final T elem)
	{
		addVertex(elem);

		for (T v : getNeighbors(first))
			addEdge(v, elem);
	
		for (T v : getNeighbors(second))
			addEdge(elem, v);

		removeVertex(first);
		removeVertex(second);
	}

	public UndirectedGraph<T> union(final Graph<T> g)
	{
		if (g == null || g == this)
			return this;

		for (T v : g.vertices()) {
			if (!containsVertex(v))
				addVertex(v);
		}

		for (Edge<T> e : g.edges()) {
			if (!containsEdge(e))
				addEdge(e);
		}

		return this;
	}
	
	public UndirectedGraph<T> subtract(final Graph<T> g)
	{
		if (g == null)
			return this;

		for (Edge<T> e : g.edges()) {
			if (containsEdge(e))
				removeEdge(e);
		}

		return this;
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || obj.getClass() != this.getClass())
			return false;
		else
			return equals((UndirectedGraph<?>) obj);
	}

	public boolean equals(final UndirectedGraph<?> g)
	{
		if (g == this)
			return true;
		else if (g == null || g.getClass() != this.getClass())
			return false;
		else
			return neighbors.equals(g.neighbors);
	}
	
	@Override
	public int hashCode()
	{
		return neighbors.hashCode();
	}

	@Override
	public UndirectedGraph<T> clone()
	{
		return new UndirectedGraph<T>(this);
	}

	@Override
	public String toString()
	{
		return "Graph: " + neighbors.toString();
	}
}
