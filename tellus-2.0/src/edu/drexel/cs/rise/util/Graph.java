/*
 * Graph.java
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

import java.util.Set;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public interface Graph<T> extends Cloneable
{
	interface Edge<T> extends Cloneable
	{
		T first();
		T second();
		String weight();
		void setWeight(String w);

		Edge<T> clone();
		boolean equals(Object obj);
		
	}
	
	/**
	 * Returns the order (number of vertices) of the graph.
	 * @return order of the graph
	 */
	int order();
	
	/**
	 * Returns the size (number of edges) of the graph.
	 * @return size of the graph
	 */
	int size();

	Set<Edge<T>> edges();
	Set<T> vertices();

	void addVertex(T v);
	void removeVertex(T v);
	boolean containsVertex(T v);

	void addEdge(T first, T second);
	void removeEdge(Edge<T> e);
	void removeEdge(T first, T second);
	boolean containsEdge(Edge<T> e);
	boolean containsEdge(T first, T second);

	Set<T> getNeighbors(T v);
	Set<Edge<T>> getEdges(T v);
	int getDegree(T v);
	
	void contract(Edge<T> e, T elem);
	void contract(T first, T second, T elem);

	Graph<T> union(Graph<T> g);
	Graph<T> subtract(Graph<T> g);

	Graph<T> clone();
}
