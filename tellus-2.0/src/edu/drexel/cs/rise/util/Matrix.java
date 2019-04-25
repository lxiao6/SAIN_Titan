/*
 * Matrix.java
 * Copyright (c) 2009, Drexel University
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
package edu.drexel.cs.rise.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class Matrix<T, L>
{
    private final List<L> labels;
    private final Map<L, Integer> indices;
    private final Map<Integer, Map<Integer, T>> data;

    public Matrix(final Collection<? extends L> labels)
    {
        this.labels = new ArrayList<L>(labels);
        this.indices = new HashMap<L, Integer>();
        this.data = new HashMap<Integer, Map<Integer, T>>();

        int i = 0;
        for (L lab : labels)
            indices.put(lab, i++);
    }

    public int length()
    {
        return labels.size();
    }

    public List<L> getLabels()
    {
        return Collections.unmodifiableList(labels);
    }

    public L getLabel(int n)
    {
        return labels.get(n);
    }

    public int getIndex(final L label)
    {
        return indices.get(label);
    }

    public T get(final L row, final L column)
    {
        return get(getIndex(row), getIndex(column));
    }

    public T get(int row, int column)
    {
        final Map<Integer, T> block = data.get(row);
        if (block == null || !block.containsKey(column))
            return null;
        else
            return block.get(column);
    }

    public void remove(final L row, final L column)
    {

        remove(getIndex(row), getIndex(column));

    }

    public void remove(int row, int column)
    {
        if (data.containsKey(row)) {

            final Map<Integer, T> block = data.get(row);

            if (block.containsKey(column)) {

                block.remove(column);
            }

        }

    }

    public void set(final L row, final L column, final T value)
    {
        set(getIndex(row), getIndex(column), value);
    }

    public void set(int row, int column, final T value)
    {
        if (!data.containsKey(row))
            data.put(row, new HashMap<Integer, T>());

        final Map<Integer, T> block = data.get(row);
        if (!block.containsKey(column))
            block.put(column, value);
    }

    public Map<Integer, Map<Integer, T>> getAllCells()
    {
        return data;
    }
}
