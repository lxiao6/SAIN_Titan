/*
 * MutableDouble.java
 * Copyright (c) 2010, Drexel University
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Drexel University nor the names of its
 *       contributors may be used to endorse or promote products derived from
 *       this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS AND CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.drexel.cs.rise.math;

import edu.drexel.cs.rise.util.Hasher;

/**
 * 
 * @author Sunny Wong
 * @since 1.0
 */
public final class MutableDouble extends Number implements
		Comparable<MutableDouble>
{
	private static final long serialVersionUID = 10L;

	public double value;

	public MutableDouble(double d)
	{
		value = d;
	}

	@Override
	public double doubleValue()
	{
		return value;
	}

	@Override
	public float floatValue()
	{
		return (float) value;
	}

	@Override
	public int intValue()
	{
		return (int) value;
	}

	@Override
	public long longValue()
	{
		return (long) value;
	}

	@Override
	public int compareTo(final MutableDouble d)
	{
		return Double.compare(value, d.value);
	}

	@Override
	public int hashCode()
	{
		return Hasher.hash(value);
	}

	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		else if (o == null)
			return false;
		else if (o instanceof Number)
			return ((Number) o).doubleValue() == value;
		else
			return false;
	}
}
