/*
 * Clustering.java
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

import java.util.Iterator;
import java.util.Vector;

import edu.drexel.cs.rise.util.Hasher;

/**
 * 
 * @author Sunny Wong
 * @author Kanwarpreet Sethi
 * @since 0.1
 */
public abstract class Clustering implements Iterable<Clustering>, Comparable<Clustering>
{
    private ClusterSet parent;
    private String name;

    protected Clustering(final ClusterSet par)
    {
        this("", par);
    }

    protected Clustering(String n, final ClusterSet par)
    {
        name = n;
        parent = par;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String n)
    {
        name = n;
    }

    public ClusterSet getParent()
    {
        return parent;
    }

    public void setParent(final ClusterSet cls)
    {
        parent = cls;
    }

    @Override
    public String toString()
    {
        return getName();
    }

    public abstract Iterator<Clustering> iterator();

    public abstract void visit(final ClusterVisitor visitor);

    @Override
    public int compareTo(Clustering c)
    {
        return this.name.compareToIgnoreCase(c.name);
    }

    protected boolean equals(final Clustering cluster)
    {
        return cluster != null && Hasher.equals(name, cluster.getName());
    }

    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }

    public Vector<String> items()
    {
        Vector<String> set = new Vector<String>();

        if (this instanceof ClusterItem) {
            set.add(this.getName());
        }
        if (this instanceof ClusterSet) {
            for (Clustering c : this) {
                set.addAll(c.items());
            }
        }
        return set;
    }
}
