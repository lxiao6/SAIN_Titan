/*
 * FileWriter.java
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
package edu.drexel.cs.rise.minos.dsm;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.ClusterVisitor;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.util.Digraph;
import edu.drexel.cs.rise.util.WeightedDigraph;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public class FileWriter
{
	public static void save(final Digraph<String> g, final File path)
			throws MinosException
	{
		save(g, null, path);
	}
	
	public static void save(final Digraph<String> g, final Clustering cluster,
			final File path) throws MinosException
	{
		try {
			final PrintWriter out = new PrintWriter(path);
			save(g, cluster, out);
			out.flush();
			out.close();
		}
		catch (IOException ex) {
			throw new MinosException(ex);
		}
	}
	
	public static void save(String[] uniform, final Digraph<String> g, final Clustering cluster,
			final File path) throws MinosException
	{
		try {
			final PrintWriter out = new PrintWriter(path);
			save(uniform,g, cluster, out);
			out.flush();
			out.close();
		}
		catch (IOException ex) {
			throw new MinosException(ex);
		}
	}
	
	public static void saveweighted(final WeightedDigraph<String> g, final Clustering cluster,
			final File path) throws MinosException
	{
		try {
			final PrintWriter out = new PrintWriter(path);
			save(g, cluster, out);
			out.flush();
			out.close();
		}
		catch (IOException ex) {
			throw new MinosException(ex);
		}
	}
	
	public static void saveweightednew(final WeightedDigraph<String> g, final Clustering cluster,
			final File path) throws MinosException
	{
		try {
			final PrintWriter out = new PrintWriter(path);
			savenew(g, cluster, out);
			out.flush();
			out.close();
		}
		catch (IOException ex) {
			throw new MinosException(ex);
		}
	}

	public static void save(final Digraph<String> g, final PrintWriter out)
			throws MinosException
	{
		save(g, null, out);
	}
	
	public static void save(final Digraph<String> g, final Clustering cluster,
			final PrintWriter out) throws MinosException
	{
		final Collection<String> vars = arrange(g, cluster);

		out.println(vars.size());
		for (String u : vars) {
			for (String v : vars) {
				if (g.containsEdge(v, u))
					out.print(1);
				else
					out.print(0);

				out.print(' ');
			}
			out.println();
		}

		for (String v : vars)
			out.println(v);
	}
	
	public static void save(final String[] uniform, final Digraph<String> g, final Clustering cluster,
			final PrintWriter out) throws MinosException
	{
		final Collection<String> vars = arrange(g, cluster);

		out.println(uniform[0]);
		out.println(vars.size());
		for (String u : vars) {
			for (String v : vars) {
				if (g.containsEdge(v, u))
					out.print(1);
				else
					out.print(0);

				out.print(' ');
			}
			out.println();
		}

		for (String v : vars)
			out.println(v);
	}
	
	
	public static void save(final WeightedDigraph<String> g, final Clustering cluster,
			final PrintWriter out) throws MinosException
	{
		final Collection<String> vars = arrange(g, cluster);

		out.println(vars.size());
		for (String u : vars) {
			for (String v : vars) {
				if (g.containsEdge(v, u))
					out.print(g.getEdge(v, u).weight());
				else
					out.print(0);

				out.print(' ');
			}
			out.println();
		}

		for (String v : vars)
			out.println(v);
	}
	
	
	public static void saveweighted(final String[] dp, final WeightedDigraph<String> g, final Clustering cluster,
			final File path) throws MinosException
	{
		try {
			final PrintWriter out = new PrintWriter(path);
			save(dp,g, cluster, out);
			out.flush();
			out.close();
		}
		catch (IOException ex) {
			throw new MinosException(ex);
		}
	}
	
	public static void save(final String[] dp, final WeightedDigraph<String> g, final Clustering cluster,
			final PrintWriter out) throws MinosException
	{
		
		String line = "";
		for(int index=0;index<dp.length;index++){
			String s = dp[index];
			line = line + s+",";
		}
		line = line.substring(0, line.length()-1);
		out.println("["+line+"]");
		
		final Collection<String> vars = arrange(g, cluster);

		out.println(vars.size());
		for (String u : vars) {
			for (String v : vars) {
				if (g.containsEdge(v, u))
					out.print(g.getEdge(v, u).weight());
				else
					out.print(0);

				out.print(' ');
			}
			out.println();
		}

		for (String v : vars)
			out.println(v);
	}
	
	public static void savenew(final WeightedDigraph<String> g, final Clustering cluster,
			final PrintWriter out) throws MinosException
	{
		final Collection<String> vars = arrange(g, cluster);

		out.println(vars.size());
		for (String u : vars) {
			for (String v : vars) {
				/*if (g.containsEdge(v, u))
					out.print(g.getEdge(v, u).weight());
				else
					out.print(0);*/
				out.print(g.getEdge(v, u).weight());
				out.print(' ');
			}
			out.println();
		}

		for (String v : vars)
			out.println(v);
	}


	protected static Collection<String> arrange(final Digraph<String> g,
			final Clustering cluster) throws MinosException
	{
		if (cluster == null)
			return g.vertices();

		final List<String> vars = new ArrayList<String>();
		cluster.visit(new ClusterVisitor() {
			@Override
			public void visit(final ClusterItem item)
			{
				vars.add(item.getName());
			}

			@Override
			public void visit(final ClusterSet set)
			{
				for (Clustering cls : set)
					cls.visit(this);
			}
		});

		return vars;
	}
	
	protected static Collection<String> arrange(final WeightedDigraph<String> g,
			final Clustering cluster) throws MinosException
	{
		if (cluster == null)
			return g.vertices();

		final List<String> vars = new ArrayList<String>();
		cluster.visit(new ClusterVisitor() {
			@Override
			public void visit(final ClusterItem item)
			{
				vars.add(item.getName());
			}

			@Override
			public void visit(final ClusterSet set)
			{
				for (Clustering cls : set)
					cls.visit(this);
			}
		});

		return vars;
	}
}

