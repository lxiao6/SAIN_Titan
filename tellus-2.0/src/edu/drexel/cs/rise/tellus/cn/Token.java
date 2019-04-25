/*
 * Token.java
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
package edu.drexel.cs.rise.tellus.cn;

/**
 *
 * @author Kanwarpreet Sethi
 * @author Sunny Wong
 * @since 0.1
 */
public class Token implements Expr
{
	private static final long serialVersionUID = 10L;
	
	private String identifier;

	public Token(final String t)
	{
		identifier = t;
	}

	@Override
	public String toString()
	{
		return identifier;
	}

	public final String getIdentifier()
	{
		return identifier;
	}

	public final void setIdentifier(final String identifier)
	{
		this.identifier = identifier;
	}
	
	@Override
	public int getSize()
	{
		return 0; 
	}
	
	@Override
	public void visit(final ExprVisitor visitor)
	{
		visitor.visit(this);
	}

	@Override
	public Token clone()
	{
		try {
			return (Token) super.clone();
		}
		catch (CloneNotSupportedException ex) {
			throw new AssertionError(ex);
		}
	}
}
