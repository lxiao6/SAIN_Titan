/*
 * AbstractGraph.java
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
package edu.drexel.cs.rise.util;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public abstract class AbstractGraph<T> implements Graph<T>
{
    protected static class DefaultEdge<V> implements Edge<V>
    {
        private final V first, second;
        private String weight;

        public DefaultEdge(final V f, final V s)
        {
            if (f == null || s == null)
                throw new NullPointerException();

            first = f;
            second = s;
        }

        public DefaultEdge(final V f, final V s, final String w)
        {
            if (f == null || s == null || w == null) {

                throw new NullPointerException();
            }

            first = f;
            second = s;
            weight = w;
        }

        public final V first()
        {
            return first;
        }

        public final V second()
        {
            return second;
        }

        @Override
        public boolean equals(final Object obj)
        {
            if (obj == this)
                return true;
            else if (obj == null || !(obj instanceof DefaultEdge<?>))
                return false;

            return equals((DefaultEdge<?>) obj);
        }

        public boolean equals(final DefaultEdge<?> e)
        {
            if (e == this)
                return true;
            else if (e == null)
                return false;
            else
                return Hasher.equals(e.first, first) && Hasher.equals(e.second, second);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Edge<V> clone()
        {
            try {
                return (Edge<V>) super.clone();
            }
            catch (CloneNotSupportedException ex) {
                throw new AssertionError(ex);
            }
        }

        @Override
        public int hashCode()
        {
            return Hasher.hashAll(first, second);
        }

        @Override
        public String weight()
        {
            return weight;
        }

        @Override
        public void setWeight(String w)
        {
            this.weight = w;
        }
    }

    protected int size;

    protected AbstractGraph()
    {
        size = 0;
    }

    public static <T> Edge<T> createEdge(final T first, final T second)
    {
        return new DefaultEdge<T>(first, second);
    }

    public static <T> Edge<T> createEdge(final T first, final T second, final String weight)
    {
        return new DefaultEdge<T>(first, second, weight);
    }

    protected Set<T> createVertexSet()
    {
        return new HashSet<T>();
    }

    /**
     * Returns the size (number of edges) of the graph. This operation is
     * guaranteed to run in constant time, &Theta;(1).
     */
    public int size()
    {
        return size;
    }

    @Override
    public abstract Graph<T> clone();
}
