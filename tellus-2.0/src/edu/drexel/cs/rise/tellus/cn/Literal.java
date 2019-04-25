/*
 * Literal.java
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
package edu.drexel.cs.rise.tellus.cn;

/**
 * 
 * @author Sunny Wong
 * @since 0.1
 */
public final class Literal
{
	private final String name;
	private boolean negated;

	public Literal(final String name)
	{
		this(false, name);
	}

	public Literal(boolean negated, final String name)
	{
		if (name == null)
			throw new NullPointerException();

		this.name = name;
		this.negated = negated;
	}

	public String getName()
	{
		return name;
	}

	public boolean isNegated()
	{
		return negated;
	}

	public void negate()
	{
		negated = !negated;
	}

	public static Literal negatedClone(final Literal x)
	{
		return new Literal(!x.isNegated(), x.getName());
	}

	@Override
	public String toString()
	{
		return (negated ? '-' : "") + name; 
	}

	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || !(obj instanceof Literal))
			return false;

		final Literal other = (Literal) obj;
		return name.equals(other.name) && negated == other.negated;
	}
}
