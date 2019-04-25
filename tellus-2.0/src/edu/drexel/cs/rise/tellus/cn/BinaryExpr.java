/*
 * BinaryExpr.java
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
 * @author Kanwapreet Sethi
 * @author Sunny Wong
 * @since 0.1
 */
public class BinaryExpr implements Expr
{
	private static final long serialVersionUID = 10L;
	
	public static enum Operator
	{
		IMPLIES("=>"),
		EQUALS("="),
		NEQUALS("!="),
		AND("and"),
		OR("or"),
		EQUIV("<=>");

		private final String symbol;
		
		private Operator(final String symbol)
		{
			this.symbol = symbol;
		}
		
		@Override
		public String toString()
		{
			return symbol;
		}
	}
	
	private Operator operator;
	private Expr e1;
	private Expr e2;

	public BinaryExpr(final Expr expr1, final Expr expr2, Operator op)
	{
		operator = op;
		e1 = expr1;
		e2 = expr2;
	}

	public final Operator getOperator()
	{
		return operator;
	}

	public final Expr getExpr1()
	{
		return e1;
	}

	public final Expr getExpr2()
	{
		return e2;
	}

	public final void setOperator(Operator operator)
	{
		this.operator = operator;
	}

	public final void setExpr1(final Expr e1)
	{
		this.e1 = e1;
	}

	public final void setExpr2(final Expr e2)
	{
		this.e2 = e2;
	}
	
	@Override
	public String toString()
	{
		return e1 + " " + operator.toString() + " " + e2;
	}
	
	@Override
	public int getSize()
	{
		if (operator == Operator.EQUALS || operator == Operator.NEQUALS)
			return 1;
		else
			return e1.getSize() + e2.getSize();
	}

	@Override
	public void visit(final ExprVisitor visitor)
	{
		visitor.visit(this);
	}

	@Override
	public BinaryExpr clone()
	{
		try {
			final BinaryExpr clone = (BinaryExpr) super.clone();
			clone.e1 = e1.clone();
			clone.e2 = e2.clone();
			return clone;
		}
		catch (CloneNotSupportedException ex) {
			throw new AssertionError(ex);
		}
	}
}
