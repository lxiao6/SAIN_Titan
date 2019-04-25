/*
 * GraphUtilities.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import edu.drexel.cs.rise.tellus.ConstraintNetwork;
import edu.drexel.cs.rise.tellus.cn.BinaryExpr;
import edu.drexel.cs.rise.tellus.cn.Expr;
import edu.drexel.cs.rise.tellus.cn.ExprVisitor;
import edu.drexel.cs.rise.tellus.cn.Token;
import edu.drexel.cs.rise.tellus.cn.UnaryExpr;
import edu.drexel.cs.rise.tellus.cn.BinaryExpr.Operator;

/**
 *
 * @author Kanwarpreet Sethi
 * @author Sunny Wong
 * @since 0.1
 */
public final class GraphUtilities
{
	public static class GraphBuilder implements ExprVisitor
	{
		protected final List<String> vertices = new ArrayList<String>();
		protected final Graph<String> g;

		public GraphBuilder()
		{
			g = new Digraph<String>();
		}
		
		public GraphBuilder(final Graph<String> g)
		{
			this.g = g;
		}

		public final Graph<String> getGraph()
		{
			return g;
		}

		public void initialize()
		{
			vertices.clear();
		}
		
		public List<String> variables()
		{
			return Collections.unmodifiableList(vertices);
		}
		
		public void populateEdges()
		{
			for (int i = 0; i < vertices.size(); ++i) {
				for (int j = i + 1; j < vertices.size(); ++j) {
					final String u = vertices.get(i);
					final String v = vertices.get(j);

					g.addEdge(u, v);
					g.addEdge(v, u);
				}
			}
		}

		@Override
		public void visit(final BinaryExpr expr)
		{
			expr.getExpr1().visit(this);

			if (expr.getOperator() != Operator.EQUALS &&
					expr.getOperator() != Operator.NEQUALS) {
				expr.getExpr2().visit(this);
			}
		}

		@Override
		public void visit(final UnaryExpr expr)
		{
			expr.getExpr().visit(this);
		}

		@Override
		public void visit(final Token expr)
		{
			g.addVertex(expr.getIdentifier());
			vertices.add(expr.getIdentifier());
		}
	}

	private GraphUtilities()
	{
	}

	public static <T> Digraph<T> transpose(final Digraph<T> g)
	{
		final Digraph<T> tr = new Digraph<T>();
		for (T v : g.vertices())
			tr.addVertex(v);

		for (Graph.Edge<T> e : g.edges())
			tr.addEdge(e.second(), e.first());

		return tr;
	}

	public static <T> Digraph<Set<T>> condense(final Digraph<T> g)
	{
		final Stack<T> stack = new Stack<T>();
		final Set<T> counter = new HashSet<T>(g.vertices());
		Traversal<T> search = Traversal.of(g);

		while (counter.size() > 0) {
			final T root = counter.iterator().next();
			search.depthFirstPostOrder(root, new GraphVisitor<T>() {
					@Override
					public void visit(final T v)
					{
						stack.push(v);
						counter.remove(v);
					}
				});
		}

		// find strongly connected components
		final List<Set<T>> strongs = new ArrayList<Set<T>>();
		final Map<T, Set<T>> mappings = new HashMap<T, Set<T>>();
		final Digraph<T> gt = GraphUtilities.transpose(g);
		search = Traversal.of(gt);

		while (!stack.empty()) {
			final T root = stack.pop();
			if (search.getColor(root) == Traversal.WHITE) {
				final Set<T> curr = new HashSet<T>();

				search.depthFirstPreOrder(root, new GraphVisitor<T>() {
						@Override
						public void visit(final T v)
						{
							curr.add(v);
							mappings.put(v, curr);
						}
					});

				strongs.add(curr);
			}
		}

		// set strongly connected components as vertices
		final Digraph<Set<T>> co = new Digraph<Set<T>>();
		for (Set<T> v : strongs)
			co.addVertex(v);

		// create edges between strongly connected components
		for (Set<T> scc : strongs) {
			for (T v : scc) {
				final Set<T> neighbors = g.getOutgoingNeighbors(v);
				for (T n : neighbors)
					co.addEdge(scc, mappings.get(n));
			}
		}

		return co;
	}

	public static <T> Set<T> getMinimalElements(final Digraph<T> g)
	{
		final Set<T> set = new HashSet<T>();
		for (T v : g.vertices()) {
			if (g.getOutDegree(v) == 0)
				set.add(v);
		}

		return set;
	}

	public static <T> Set<T> getMaximalElements(final Digraph<T> g)
	{
		return getMinimalElements(transpose(g));
	}

	public static Digraph<String> buildGraph(final ConstraintNetwork cn)
	{
		final Digraph<String> g = new Digraph<String>();

		for (String var : cn.getVariableNames())
			g.addVertex(var);
		
		final GraphBuilder builder = new GraphBuilder(g);
		for (Expr expr : cn.getConstraints()) {
			builder.initialize();
			expr.visit(builder);
			builder.populateEdges();
		}

		return g;
	}

	/**
	 * Based on algorithm described in Boost library documentation.
	 * 
	 */
	public static <T> void transitiveClosure(final Digraph<T> g)
	{
		final Digraph<Set<T>> condense = GraphUtilities.condense(g);
		final Traversal<Set<T>> search = Traversal.of(condense);
		final Set<Set<T>> counter = new HashSet<Set<T>>(condense.vertices());
		final Deque<Set<T>> topologic = new LinkedList<Set<T>>();

		while (counter.size() > 0) {
			final Set<T> root = counter.iterator().next();
			search.depthFirstPostOrder(root, new GraphVisitor<Set<T>>() {
					@Override
					public void visit(final Set<T> v)
					{
						topologic.addLast(v);
						counter.remove(v);
					}
				});
		}

		final Map<Set<T>, Set<Set<T>>> succ =
				new HashMap<Set<T>, Set<Set<T>>>();
		for (Set<T> src : topologic) {
			final Set<Set<T>> curr = new HashSet<Set<T>>();

			for (Set<T> dest : condense.getOutgoingNeighbors(src)) {
				if (!curr.contains(dest)) {
					curr.add(dest);

					if (succ.containsKey(dest))
						curr.addAll(succ.get(dest));
				}
			}

			succ.put(src, curr);
		}

		for (Map.Entry<Set<T>, Set<Set<T>>> entry : succ.entrySet()) {
			for (T u : entry.getKey()) {
				for (Set<T> dest : entry.getValue()) {
					for (T v : dest)
						g.addEdge(u, v);
				}
			}
		}
	}
}
