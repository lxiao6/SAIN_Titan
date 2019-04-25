/*
 * ClusterItem.java
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
package edu.drexel.cs.rise.tellus.cluster;

import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author Sunny Wong
 * @author Kanwarpreet Sethi
 * @since 0.1
 */
public class ClusterItem extends Clustering
{
	public ClusterItem(final String name)
	{
		this(name, null);
	}
	
	public ClusterItem(final String name, final ClusterSet parent)
	{
		super(name, parent);
	}

	@Override
	public Iterator<Clustering> iterator()
	{
		return Collections.<Clustering>emptyList().iterator();
	}

	@Override
	public void visit(final ClusterVisitor visitor)
	{
		visitor.visit(this);
	}
	
	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || obj.getClass() != this.getClass())
			return false;
		else
			return super.equals((ClusterItem) obj);
	}
	
	@Override
	public int hashCode()
	{
		return 3 * super.hashCode();
	}
}