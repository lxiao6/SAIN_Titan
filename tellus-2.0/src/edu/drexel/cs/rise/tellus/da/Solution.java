/*
 * Solution.java
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
package edu.drexel.cs.rise.tellus.da;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public class Solution implements Serializable, Iterable<Assign>, Cloneable
{
	private static final long serialVersionUID = 10L;

	protected static final class AssignIterator implements Iterator<Assign>
	{
		private final Iterator<Map.Entry<String, String>> inner;

		public AssignIterator(final Iterator<Map.Entry<String, String>> iter)
		{
			inner = iter;
		}

		@Override
		public boolean hasNext()
		{
			return inner.hasNext();
		}

		@Override
		public Assign next()
		{
			final Map.Entry<String, String> entry = inner.next();
			return new Assign(entry.getKey(), entry.getValue());
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
	}
	
	private final Map<String, String> assigns;

	public Solution()
	{
		assigns = new HashMap<String, String>();
	}

	private Solution(final Map<String, String> as)
	{
		this();
		assigns.putAll(as);
	}

	@Override
	public Iterator<Assign> iterator()
	{
		return new AssignIterator(assigns.entrySet().iterator());
	}
	
	public final Set<String> getVariableNames()
	{
		return assigns.keySet();
	}
	
	public final String get(final String variable)
	{
		return assigns.get(variable);
	}
	
	public void add(final Assign assign)
	{
		assigns.put(assign.getVariable(), assign.getValue());
	}
	
	public boolean contains(final Assign assign)
	{
		return contains(assign.getVariable(), assign.getValue());
	}

	public boolean contains(final String variable, final String value)
	{
		return assigns.containsKey(variable)
				&& assigns.get(variable).equals(value);
	}
	
	public Set<Assign> getAssignments()
	{
		final Set<Assign> result = new HashSet<Assign>();
		for (Map.Entry<String, String> entry : assigns.entrySet())
			result.add(new Assign(entry.getKey(), entry.getValue()));
		return result;
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || obj.getClass() != this.getClass())
			return false;
		else {
			final Solution soln = (Solution)obj;
			return assigns.equals(soln.assigns);
		}
	}

	@Override
	public String toString()
	{
		return assigns.toString();
	}

	@Override
	public Solution clone()
	{
		return new Solution(assigns);
	}
	
	@Override
	public int hashCode()
	{
		return assigns.hashCode();
	}
}
