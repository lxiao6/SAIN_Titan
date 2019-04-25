/*
 * KeyPair.java
 * Copyright (c) 2010, Drexel University.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Drexel University nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY DREXEL UNIVERSITY ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL DREXEL UNIVERSITY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package edu.drexel.cs.rise.util;

/**
 * 
 * @author Sunny Wong
 * @since 1.0
 */
public final class KeyPair<K extends Comparable<K>, V>
		implements Comparable<KeyPair<? extends K, ?>>
{
	private K key;
	private V value;

	public static <K extends Comparable<K>, V> KeyPair<K, V> of(
			final K key, final V value)
	{
		return new KeyPair<K, V>(key, value);
	}

	public KeyPair(final K key, final V value)
	{
		this.key = key;
		this.value = value;
	}

	public K getKey()
	{
		return key;
	}

	public void setKey(final K key)
	{
		this.key = key;
	}

	public V getValue()
	{
		return value;
	}

	public void setValue(final V value)
	{
		this.value = value;
	}

	@Override
	public int compareTo(final KeyPair<? extends K, ?> pair)
	{
		return key.compareTo(pair.key);
	}

	@Override
	public boolean equals(final Object o)
	{
		if (o == this)
			return true;
		else if (o == null || o.getClass() != this.getClass())
			return false;
		else
			return equals((KeyPair<?, ?>) o);
	}

	public boolean equals(final KeyPair<?, ?> pair)
	{
		return Hasher.equals(key, pair.key) && Hasher.equals(value, pair.value);
	}

	@Override
	public int hashCode()
	{
		return Hasher.hashAll(key, value);
	}
}
