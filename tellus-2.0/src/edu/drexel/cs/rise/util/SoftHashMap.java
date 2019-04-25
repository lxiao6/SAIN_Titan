/*
 * SoftHashMap.java
 * Copyright (c) 2009, Drexel University
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

import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class SoftHashMap<K, V> extends AbstractMap<K, V>
{
	private final Map<K, SoftReference<V>> data =
			new HashMap<K, SoftReference<V>>();
	
	public SoftHashMap()
	{
	}

	@Override
	public V get(final Object key)
	{
		V val = null;
		final SoftReference<V> ref = data.get(key);

		if (ref != null) {
			val = ref.get();

			if (val == null)
				data.remove(key);
		}

		return val;
	}

	@Override
	public V put(final K key, final V value)
	{
		final SoftReference<V> ref = data.put(key, new SoftReference<V>(value));
		if (ref != null)
			return ref.get();
		else
			return null;
	}

	@Override
	public boolean containsKey(final Object key)
	{
		return get(key) != null;
	}

	@Override
	public boolean containsValue(final Object value)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<Entry<K, V>> entrySet()
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear()
	{
		data.clear();
	}
}
