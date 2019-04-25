/*
 * ConstraintNetwork.java
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
package edu.drexel.cs.rise.tellus;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.drexel.cs.rise.tellus.cn.Expr;

/**
 * 
 * @author Sunny Wong
 * @since 0.1
 */
public class ConstraintNetwork implements Serializable
{
	private static final long serialVersionUID = 10L;

	private String name;
	private final Map<String, Set<String>> variables;
	private final Set<Expr> constraints;

	public ConstraintNetwork()
	{
		name = "";
		variables = new HashMap<String, Set<String>>();
		constraints = new HashSet<Expr>();
	}

	public ConstraintNetwork(final String name) {
		this.name = name;
		variables = new HashMap<String, Set<String>>();
		constraints = new HashSet<Expr>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(final String name)
	{
		this.name = name;
	}
	
	public Set<String> getVariableNames()
	{
		return Collections.unmodifiableSet(variables.keySet());
	}

	public Set<String> getVariableDomain(final String name)
	{
		final Set<String> domain = variables.get(name);
		if (domain == null)
			return null;

		return Collections.unmodifiableSet(domain);
	}
	public Set<Expr> getConstraints()
	{
		return Collections.unmodifiableSet(constraints);
	}

	public void addConstraint(final Expr e)
	{
		constraints.add(e);
	}

	public void addVariable(final String var, final Collection<String> domain)
	{
		variables.put(var, new HashSet<String>(domain));
	}

	public void addVariable(final String var, final String... domains)
	{
		final Set<String> dom = new HashSet<String>();
		for (String d : domains)
			dom.add(d);
		
		variables.put(var, dom);
	}
	
	@Override
	public String toString()
	{
		final StringBuilder cn = new StringBuilder();
		cn.append("Variables: ");
		cn.append(variables.toString());
		cn.append(System.getProperty("line.separator"));
		cn.append("Constraints: ");
		cn.append(constraints.toString());
		return cn.toString();
	}
}
