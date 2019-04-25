/*
 * Context.java
 * Copyright (c) 2010, Drexel University.
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
package edu.drexel.cs.rise.tellus;

import java.util.List;

import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.tellus.da.Assign;
import edu.drexel.cs.rise.tellus.da.Automaton;
import edu.drexel.cs.rise.tellus.da.Solution;
import edu.drexel.cs.rise.util.Digraph;

/**
 * 
 * @author Sunny Wong
 * @since 1.0
 */
public class Context
{
	private ConstraintNetwork constraintNetwork;
	private Clustering clustering;
	private Digraph<String> dominance;
	private Digraph<String> dependency;
	private List<Solution> solutions;
	private Automaton<Solution, Assign> automaton;

	public final String getName()
	{
		return constraintNetwork.getName();
	}

	public final ConstraintNetwork getConstraintNetwork()
	{
		return constraintNetwork;
	}

	public final void setConstraintNetwork(final ConstraintNetwork cn)
	{
		this.constraintNetwork = cn;
	}

	public final Clustering getClustering()
	{
		return clustering;
	}

	public final void setClustering(final Clustering clustering)
	{
		this.clustering = clustering;
	}

	public final Digraph<String> getDominance()
	{
		return dominance;
	}

	public final void setDominance(final Digraph<String> dominance)
	{
		this.dominance = dominance;
	}

	public final Digraph<String> getDependency()
	{
		return dependency;
	}

	public final void setDependency(final Digraph<String> dependency)
	{
		this.dependency = dependency;
	}

	public final List<Solution> getSolutions()
	{
		return solutions;
	}

	public final void setSolutions(final List<Solution> solutions)
	{
		this.solutions = solutions;
	}

	public final Automaton<Solution, Assign> getAutomaton()
	{
		return automaton;
	}

	public final void setAutomaton(final Automaton<Solution, Assign> da)
	{
		this.automaton = da;
	}
}
