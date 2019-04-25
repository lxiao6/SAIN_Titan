/*
 * Pair.java
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

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public final class Pair<T1, T2>
{
	private final T1 first;
	private final T2 second;

	public static <T1, T2> Pair<T1, T2> of(final T1 first, final T2 second)
	{
		return new Pair<T1, T2>(first, second);
	}

	public Pair(final T1 first, final T2 second)
	{
		this.first = first;
		this.second = second;
	}

	public final T1 first()
	{
		return first;
	}
	
	public final T2 second()
	{
		return second;
	}
	
	@Override
	public String toString()
	{
		return String.format("(%s, %s)", first, second);
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || obj.getClass() != this.getClass())
			return false;

		final Pair<?, ?> pair = (Pair<?, ?>)obj;
		return Hasher.equals(first, pair.first) &&
				Hasher.equals(second, pair.second); 
	}

	@Override
	public int hashCode()
	{
		return Hasher.hashAll(first, second);
	}
}
