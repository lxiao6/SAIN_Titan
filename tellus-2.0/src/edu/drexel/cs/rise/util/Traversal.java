/*
 * Traversal.java
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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public final class Traversal<T>
{
	public static final int WHITE = 0;
	public static final int GRAY = 1;
	public static final int BLACK = 2;

	private final Digraph<T> g;
	private Map<T, Integer> colors;
	
	private static final class PreOrder<U> implements GraphTraveler<U>
	{
		private final GraphVisitor<U> inner;
		
		public PreOrder(final GraphVisitor<U> inner)
		{
			this.inner = inner;
		}
		
		@Override
		public void open(final U v)
		{
			inner.visit(v);
		}
		
		@Override
		public void close(final U v)
		{
			// nothing to do
		}
	}
	
	private static final class PostOrder<U> implements GraphTraveler<U>
	{
		private final GraphVisitor<U> inner;

		public PostOrder(final GraphVisitor<U> inner)
		{
			this.inner = inner;
		}
		
		@Override
		public void open(final U v)
		{
			// nothing to do
		}
		
		@Override
		public void close(final U v)
		{
			inner.visit(v);
		}
	}

	public static final <T> Traversal<T> of(final Digraph<T> g)
	{
		return new Traversal<T>(g);
	}

	public Traversal(final Digraph<T> g)
	{
		this.g = g;
		reset();
	}

	public void reset()
	{
		colors = createColoring(g);
	}

	public int getColor(final T v)
	{
		return colors.get(v);
	}

	public final void breadthFirst(final T src, final GraphVisitor<T> visitor)
	{
		breadthFirst(src, new PostOrder<T>(visitor));
	}
	
	public void breadthFirst(final T src, final GraphTraveler<T> visitor)
	{
		if (!g.containsVertex(src))
			return;

		final Queue<T> queue = new LinkedList<T>();
		queue.offer(src);
		colors.put(src, GRAY);
		visitor.open(src);

		while (!queue.isEmpty()) {
			T curr = queue.poll();
			colors.put(curr, BLACK);
			visitor.close(curr);

			for (T neighbor : g.getOutgoingNeighbors(curr)) {
				if (colors.get(neighbor) == WHITE) {
					colors.put(neighbor, GRAY);
					queue.offer(neighbor);
					visitor.open(curr);
				}
			}
		}
	}

	public final void depthFirstPreOrder(final T src,
			final GraphVisitor<T> visitor)
	{
		depthFirst(src, new PreOrder<T>(visitor));
	}
	
	public final void depthFirstPostOrder(final T src,
			final GraphVisitor<T> visitor)
	{
		depthFirst(src, new PostOrder<T>(visitor));
	}
	
	public void depthFirst(final T src,	final GraphTraveler<T> visitor)
	{
		if (!g.containsVertex(src) || colors.get(src) == BLACK)
			return;

		colors.put(src, BLACK);
		visitor.open(src);

		for (T neighbor : g.getOutgoingNeighbors(src)) {
			if (colors.get(neighbor) == WHITE) {
				colors.put(neighbor, GRAY);
				depthFirst(neighbor, visitor);
			}
		}

		visitor.close(src);
	}

	private static <T> Map<T, Integer> createColoring(final Graph<T> g)
	{
		final Map<T, Integer> colors = new HashMap<T, Integer>();
		for (T var : g.vertices())
			colors.put(var, WHITE);

		return colors;
	}
}
