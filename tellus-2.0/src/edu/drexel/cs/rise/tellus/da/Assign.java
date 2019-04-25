/*
 * Assign.java
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

import edu.drexel.cs.rise.util.Hasher;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public final class Assign implements Serializable, Cloneable
{
	private static final long serialVersionUID = 10L;

	private String variable;
	private String value;
	
	public Assign()
	{
		this("", "");
	}
	
	public Assign(final String var, final String val)
	{
		variable = var;
		value = val;
	}
	
	public final String getVariable()
	{
		return variable;
	}

	public final void setVariable(final String variable)
	{
		if (variable != null)
			this.variable = variable;
		else
			this.variable = "";
	}

	public final String getValue()
	{
		return value;
	}

	public final void setValue(final String value)
	{
		if (value != null)
			this.value = value;
		else
			this.value = "";
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || obj.getClass() != this.getClass())
			return false;
		else {
			final Assign assign = (Assign)obj;
			return variable.equals(assign.variable) &&
					value.equals(assign.value);
		}
	}

	@Override
	public int hashCode()
	{
		return Hasher.hashAll(value, variable);
	}
	
	@Override
	public String toString()
	{
		return String.format("%s = %s", variable, value);
	}
	
	@Override
	public Assign clone()
	{
		try {
			return (Assign) super.clone();
		}
		catch (CloneNotSupportedException ex) {
			throw new AssertionError("Clone failed.");
		}
	}
}
