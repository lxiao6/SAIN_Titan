/*
 * Digraph.java
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
package edu.drexel.cs.rise.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @author Sunny Wong
 * @author Kanwarpreet Sethi
 * @since 0.1
 */
public class Digraph<T> extends AbstractGraph<T>
{
	protected final Map<T, Set<T>> incoming;
	protected final Map<T, Set<T>> outgoing;

	public Digraph()
	{
		incoming = new HashMap<T, Set<T>>();
		outgoing = new HashMap<T, Set<T>>();
	}
	
	public Digraph(final Graph<T> g)
	{
		this();
		for (T v : g.vertices())
			addVertex(v);
		for (Edge<T> e : g.edges())
			addEdge(e);
	}
	
	protected Set<T> createAdjacencyList()
	{
		return new HashSet<T>();
	}
	
	protected Set<Edge<T>> createEdgeSet()
	{
		return new HashSet<Edge<T>>();
	}
	
	/**
	 * Returns the order (number of vertices) of the graph. This operation is
	 * guaranteed to run in constant time, &Theta;(1).
	 */
	@Override
	public final int order()
	{
		return outgoing.size();
	}

	@Override
	public final Set<Edge<T>> edges()
	{
		final Set<Edge<T>> es = createEdgeSet();
		for (Map.Entry<T, Set<T>> entry : outgoing.entrySet()) {
			final T src = entry.getKey();
			for (T dest : entry.getValue())
				es.add(super.createEdge(src, dest));
		}

		return Collections.unmodifiableSet(es);
	}

	@Override
	public final Set<T> vertices()
	{
		return Collections.unmodifiableSet(outgoing.keySet());
	}

	@Override
	public void addVertex(final T v)
	{
		if (v == null)
			throw new NullPointerException();
			
		if (!outgoing.containsKey(v)) {
			outgoing.put(v, createAdjacencyList());
			incoming.put(v, createAdjacencyList());
		}
	}

	@Override
	public void removeVertex(final T v)
	{
		if (!containsVertex(v))
			return;

		incoming.remove(v);
		for (Set<T> vars : incoming.values())
			vars.remove(v);

		outgoing.remove(v);
		for (Set<T> vars : outgoing.values())
			vars.remove(v);
	}

	@Override
	public final boolean containsVertex(final T v)
	{
		return outgoing.containsKey(v);
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
		else if (first.equals(second))
			return;

		outgoing.get(first).add(second);
		incoming.get(second).add(first);
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
		outgoing.get(first).remove(second);
		incoming.get(second).remove(first);
	}

	@Override
	public final boolean containsEdge(final Edge<T> e)
	{
		return containsEdge(e.first(), e.second());
	}

	@Override
	public boolean containsEdge(final T first, final T second)
	{
		return containsVertex(first) && outgoing.get(first).contains(second);
	}

	@Override
	public final Set<T> getNeighbors(final T v)
	{
		final Set<T> vs = createAdjacencyList();
		vs.addAll(getIncomingNeighbors(v));
		vs.addAll(getOutgoingNeighbors(v));
		return Collections.unmodifiableSet(vs);
	}

	public Set<T> getIncomingNeighbors(final T v)
	{
		return Collections.unmodifiableSet(incoming.get(v));
	}

	public Set<T> getOutgoingNeighbors(final T v)
	{
		return Collections.unmodifiableSet(outgoing.get(v));
	}

	@Override
	public final Set<Edge<T>> getEdges(final T v)
	{
		if (!containsVertex(v))
			return null;

		final Set<Edge<T>> es = createEdgeSet();
		es.addAll(getIncomingEdges(v));
		es.addAll(getOutgoingEdges(v));
		return es;
	}

	public Set<Edge<T>> getIncomingEdges(final T v)
	{
		if (!containsVertex(v))
			return null;
		
		final Set<Edge<T>> es = createEdgeSet();
		for (T var : getIncomingNeighbors(v))
			es.add(super.createEdge(var, v));

		return es;
	}

	public Set<Edge<T>> getOutgoingEdges(final T v)
	{
		if (!containsVertex(v))
			return null;
		
		final Set<Edge<T>> es = createEdgeSet();
		for (T var : getOutgoingNeighbors(v))
			es.add(super.createEdge(var, v));

		return es;
	}

	@Override
	public final int getDegree(final T v)
	{
		return getInDegree(v) + getOutDegree(v);
	}

	public int getInDegree(final T v)
	{
		final Set<T> neighbors = getIncomingNeighbors(v);
		if (neighbors == null)
			return -1;
		else
			return neighbors.size();
	}

	public int getOutDegree(final T v)
	{
		final Set<T> neighbors = getOutgoingNeighbors(v);
		if (neighbors == null)
			return -1;
		else
			return neighbors.size();
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

		for (T v : getIncomingNeighbors(first))
			addEdge(v, elem);
	
		for (T v : getIncomingNeighbors(second))
			addEdge(v, elem);
	
		for (T v : getOutgoingNeighbors(first))
			addEdge(elem, v);
	
		for (T v : getOutgoingNeighbors(second))
			addEdge(elem, v);

		removeVertex(first);
		removeVertex(second);
	}

	public Digraph<T> union(final Graph<T> g)
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
	
	public Digraph<T> subtract(final Graph<T> g)
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
		else if (obj == null || !(obj instanceof Digraph<?>))
			return false;
		else
			return equals((Digraph<?>)obj);
	}

	public boolean equals(final Digraph<?> g)
	{
		if (g == this)
			return true;
		else if (g == null)
			return false;
		else
			return outgoing.equals(g.outgoing);
	}
	
	@Override
	public int hashCode()
	{
		return outgoing.hashCode();
	}

	@Override
	public Digraph<T> clone()
	{
		return new Digraph<T>(this);
	}

	@Override
	public String toString()
	{
		return "Digraph: " + outgoing.toString();
	}
	
	public Digraph<T> subset(final Collection<T> subset)
	{
		/*Digraph<T> subgraph = new Digraph<T>();
		for (T v : this.vertices())
			if(subset.contains(v))
				subgraph.addVertex(v);
		
		for(T src:subset){
			for(T dest:subset){
				if(this.containsEdge(src, dest)){
					subgraph.addEdge(src,dest);
				}
			}
		}
		System.out.println(subgraph.size);
		return subgraph;*/
		Digraph<T> ret = this.clone();
		Set<T> names = this.vertices();
		
		for(T name : names){
			if(!subset.contains(name))
				ret.removeVertex(name);
		}
		
		return ret;
	}
	
	public Digraph<T> fastsubset(final Collection<T> subset)
	{
		Digraph<T> subgraph = new Digraph<T>();
		
		for(T v:subset){
			subgraph.addVertex(v);
			
			Set<T> in = incoming.get(v);
			if(in != null){
				in.retainAll(subset);
				if(!in.isEmpty()){
					subgraph.incoming.put(v, in);
				}
			}
			
			Set<T> out = outgoing.get(v);			
			if(out != null){
				out.retainAll(subset);
				if(!out.isEmpty()){
					subgraph.outgoing.put(v, out);
				}
			}
			
			
			
			
			incoming.remove(v);
			outgoing.remove(v);
			
		}
		return subgraph;
		
	}


}
