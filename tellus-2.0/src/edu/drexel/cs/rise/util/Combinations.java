/*
 * Combinations.java
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
package edu.drexel.cs.rise.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class allows for iterating all <i>t</i>-combinations of a collection
 * with <i>n</i> elements. (See Donald Knuth's
 * <em>The Art of Computer Programming, Volume 4, Fasicle 4</em>, Algorithm T).
 *
 * @author Sunny Wong
 * @since 1.0
 */
public class Combinations<T> implements Iterator<Collection<T>>
{
	protected final List<T> items;
	protected int[] buf;
	protected int count;
	protected int[] selected;
	protected int index, x;

	public Combinations(final Collection<T> items, int count)
	{
		this.items = new ArrayList<T>(items);
		initialize(count);
	}

	public final void initialize(int count)
	{
		if (0 > count || count > items.size())
			throw new IllegalArgumentException("Invalid combination size.");

		this.count = count;
		buf = new int[count + 3];
		selected = new int[count + 3];

		buf[0] = -1;
		for (int i = 1; i <= count; ++i)
			buf[i] = i - 1;
		buf[count + 1] = items.size();
		buf[count + 2] = 0;

		System.arraycopy(buf, 0, selected, 0, buf.length);
		index = count;
	}

	@Override
	public boolean hasNext()
	{
		return selected != null;
	}

	@Override
	public Collection<T> next()
	{
		if (!hasNext())
			throw new NoSuchElementException();

		if (count == items.size()) {
			selected = null;
			return Collections.unmodifiableCollection(items);
		}

		final Deque<T> result = new LinkedList<T>();
		for (int i = 1; i <= count; ++i)
			result.add(items.get(selected[i]));

		boolean trivial = false;
		if (index > 0)
			x = index;
		else {
			if (buf[1] + 1 < buf[2]) {
				++buf[1];
				trivial = true;
			}
			else {
				index = 2;

				do {
					buf[index - 1] = index - 2;
					x = buf[index] + 1;
	
					if (x == buf[index + 1])
						++index;
				} while (x == buf[index]);
			}
		}

		if (index <= count) {
			if (!trivial) {
				buf[index] = x;
				--index;
			}

			System.arraycopy(buf, 0, selected, 0, buf.length);
		}
		else
			selected = null;

		return Collections.unmodifiableCollection(result);
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
}
