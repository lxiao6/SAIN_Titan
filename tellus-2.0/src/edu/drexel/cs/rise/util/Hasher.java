/*
 * Hasher.java
 * Copyright (c) 2009, Drexel University
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

import java.lang.reflect.Array;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class Hasher
{
	private static final int initial = 23;
	private static final int multiplier = 11;
	private static final Hasher hasher = new Hasher();

	private int value;

	public Hasher()
	{
		clear();
	}

	public void clear()
	{
		value = initial;
	}

	public final int getValue()
	{
		return value;
	}

	public static int hash(final Object obj)
	{
		if (obj == null)
			return 0;

		int value = 0;
		if (!obj.getClass().isArray())
			value = multiplier * obj.hashCode();
		else {
			final int n = Array.getLength(obj);
			for (int i = 0; i < n; ++i)
				value += hash(Array.get(obj, i));
		}

		return value;
	}

	public void add(final Object obj)
	{
		value += hash(obj);
	}

	public static int hash(boolean b)
	{
		return b ? multiplier : 0;
	}
	
	public void add(boolean b)
	{
		value += hash(b);
	}

	public static int hash(char n)
	{
		return hash((int) n);
	}

	public void add(char n)
	{
		value += hash(n);
	}

	public static int hash(short n)
	{
		return hash((int) n);
	}

	public void add(short n)
	{
		value += hash(n);
	}

	public static int hash(int n)
	{
		return multiplier * n;
	}

	public void add(int n)
	{
		value += hash(n);
	}

	public static int hash(long n)
	{
		return multiplier * (int)(n ^ (n >>> 32));
	}

	public void add(long n)
	{
		value += hash(n);
	}

	public static int hash(float f)
	{
		return hash(Float.floatToIntBits(f));
	}

	public void add(float f)
	{
		value += hash(f);
	}

	public static int hash(double d)
	{
		return hash(Double.doubleToLongBits(d));
	}

	public void add(double d)
	{
		value += hash(d);
	}
	
	public static <T> int hash(final T[] arr)
	{
		int n = 0;
		if (arr == null)
			return n;

		for (T t : arr)
			n += hash(t);
		
		return n;
	}

	public <T> void add(final T[] arr)
	{
		value += hash(arr);
	}
	
	public static int hashAll(final Object... objs)
	{
		hasher.clear();
		
		for (Object o : objs)
			hasher.add(o);
		
		return hasher.getValue();
	}

	public static boolean equals(final Object a, final Object b)
	{
		return (a == null && b == null) ||
				(a != null && a.equals(b));
	}
}
