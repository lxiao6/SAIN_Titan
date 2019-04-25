/*
 * BidiMap.java
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
import java.util.Set;

/**
 *
 * @author Sunny Wong
 * @author Patrick Lockner
 * @since 0.2
 */
public class BidiMap<T1, T2>
{
	protected final Map<T1, Set<T2>> left;
	protected final Map<T2, Set<T1>> right;

	public BidiMap()
	{
		left = new HashMap<T1, Set<T2>>();
		right = new HashMap<T2, Set<T1>>();
	}

	protected <T> Set<T> createMappingList()
	{
		return new HashSet<T>();
	}

	public final int getLeftSize()
	{
		return left.size();
	}

	public Set<T1> getLeft()
	{
		return Collections.unmodifiableSet(left.keySet());
	}

	public void addLeft(final T1 t)
	{
		if (!left.containsKey(t))
			left.put(t, this.<T2>createMappingList());
	}

	public Set<T2> getRightMapped(final T1 t)
	{
		if (!left.containsKey(t))
			return null;

		return Collections.unmodifiableSet(left.get(t));
	}

	public final int getRightSize()
	{
		return right.size();
	}

	public Set<T2> getRight()
	{
		return Collections.unmodifiableSet(right.keySet());
	}

	public void addRight(final T2 t)
	{
		if (!right.containsKey(t))
			right.put(t, this.<T1>createMappingList());
	}

	public Set<T1> getLeftMapped(final T2 t)
	{
		if (!right.containsKey(t))
			return null;

		return Collections.unmodifiableSet(right.get(t));
	}

	public void add(final T1 a, final T2 b)
	{
		if (!left.containsKey(a))
			addLeft(a);

		if (!right.containsKey(b))
			addRight(b);

		left.get(a).add(b);
		right.get(b).add(a);
	}

	public void remove(final T1 a, final T2 b)
	{
		if (!(left.containsKey(a) && right.containsKey(b)))
			return;

		left.get(a).remove(b);
		right.get(b).remove(a);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || !(obj instanceof BidiMap<?, ?>))
			return false;
		else
			return equals((BidiMap<?, ?>)obj);
	}

	public boolean equals(final BidiMap<?, ?> m)
	{
		if (m == this)
			return true;
		else if (m == null)
			return false;
		else
			return left.equals(m.left);
	}
	
	@Override
	public int hashCode()
	{
		return left.hashCode();
	}

	@Override
	public String toString()
	{
		return "BidiMap: " + left.toString();
	}
}
