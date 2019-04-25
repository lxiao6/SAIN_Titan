/*
 * Automaton.java
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.drexel.cs.rise.util.Hasher;
import edu.drexel.cs.rise.util.Pair;

/**
 * 
 * @author Sunny Wong
 * @author Kanwarpreet Sethi
 * @since 0.1
 */
public class Automaton<S, T>
{
	public static final class Transition<S, T>
	{
		private S src;
		private S dest;
		private T label;

		protected Transition(final S src, final S dest, final T label)
		{
			this.src = src;
			this.dest = dest;
			this.label = label;
		}

		public final S getSource()
		{
			return src;
		}

		public final S getDestination()
		{
			return dest;
		}

		public final T getLabel()
		{
			return label;
		}

		@Override
		public String toString()
		{
			return String.format("%s -> %s [label = \"%s\"]", src, dest, label);
		}

		@Override
		public boolean equals(final Object obj)
		{
			if (obj == this)
				return true;
			else if (obj == null || obj.getClass() != this.getClass())
				return false;
			else
				return equals((Transition<?, ?>) obj);
		}

		public boolean equals(final Transition<?, ?> trans)
		{
			if (trans == this)
				return true;
			else if (trans == null)
				return false;
			else
				return Hasher.equals(src, trans.src)
						&& Hasher.equals(dest, trans.dest)
						&& Hasher.equals(label, trans.label);
		}

		@Override
		public int hashCode()
		{
			return Hasher.hashAll(src, dest, label);
		}
	}

	protected final Map<S, Set<S>> outgoing;
	protected final Map<Pair<S, S>, Set<T>> transitions;

	public Automaton()
	{
		transitions = new HashMap<Pair<S, S>, Set<T>>();
		outgoing = new HashMap<S, Set<S>>();
	}

	public Automaton(final Collection<? extends S> states)
	{
		this();
		for (S s : states)
			addState(s);
	}

	protected <E> Set<E> createSet()
	{
		return new HashSet<E>();
	}

	public final int size()
	{
		return outgoing.size();
	}

	public final Set<S> states()
	{
		return Collections.unmodifiableSet(outgoing.keySet());
	}

	public void addState(final S state)
	{
		if (state == null)
			throw new NullPointerException();

		if (!outgoing.containsKey(state))
			outgoing.put(state, this.<S> createSet());
	}

	public void removeState(final S state)
	{
		if (state == null)
			throw new NullPointerException();

		if (outgoing.containsKey(state))
			outgoing.remove(state);

		for (S s : states()) {
			for (Transition<S, T> t : getTransitions(s, state)) {
				removeTransition(t.getSource(), t.getDestination(),
						t.getLabel());
			}

			for (Transition<S, T> t : getTransitions(state, s)) {
				removeTransition(t.getSource(), t.getDestination(),
						t.getLabel());
			}
		}
	}

	public final boolean containsState(final S state)
	{
		return outgoing.containsKey(state);
	}

	public void addTransition(final S src, final S dest, final T label)
	{
		if (src == null || dest == null || label == null)
			throw new NullPointerException();

		addState(src);
		addState(dest);
		final Pair<S, S> pair = new Pair<S, S>(src, dest);
		if (transitions.get(pair) == null)
			transitions.put(pair, this.<T> createSet());

		transitions.get(pair).add(label);
		outgoing.get(src).add(dest);
	}

	public void removeTransition(final S src, final S dest, final T label)
	{
		if (src == null || dest == null || label == null)
			return;

		final Pair<S, S> pair = new Pair<S, S>(src, dest);
		if (transitions.get(pair) != null)
			transitions.get(pair).remove(label);
	}

	public final boolean containsTransition(final S src, final S dest,
			final T label)
	{
		if (src == null || dest == null || label == null)
			return false;

		final Pair<S, S> pair = new Pair<S, S>(src, dest);
		final Set<T> trans = transitions.get(pair);
		return trans != null && trans.contains(label);
	}

	public final Set<Transition<S, T>> getTransitions()
	{
		final Set<Transition<S, T>> trans = this.<Transition<S, T>>createSet();

		for (Map.Entry<Pair<S, S>, Set<T>> entry : transitions.entrySet()) {
			final Pair<S, S> pair = entry.getKey();
			final S src = pair.first();
			final S dest = pair.second();

			for (T label : entry.getValue()) {
				trans.add(new Transition<S, T>(src, dest, label));
			}
		}

		return Collections.unmodifiableSet(trans);
	}

	public final Set<Transition<S, T>> getTransitions(final S state)
	{
		final Set<Transition<S, T>> trans = this.<Transition<S, T>>createSet();

		for (S dest : outgoing.get(state)) {
			final Pair<S, S> pair = new Pair<S, S>(state, dest);
			for (T label : transitions.get(pair)) {
				trans.add(new Transition<S, T>(state, dest, label));
			}
		}

		return Collections.unmodifiableSet(trans);
	}

	public final Set<Transition<S, T>> getTransitions(final S src, final S dest)
	{
		final Set<Transition<S, T>> trans = this.<Transition<S, T>> createSet();

		final Pair<S, S> pair = new Pair<S, S>(src, dest);
		if (transitions.containsKey(pair))
			for (T label : transitions.get(pair)) {
				trans.add(new Transition<S, T>(src, dest, label));
			}

		return Collections.unmodifiableSet(trans);
	}

	public Automaton<S, T> union(final Automaton<S, T> other)
	{
		if (other != null) {
			for (Transition<S, T> trans : other.getTransitions())
				addTransition(trans.getSource(), trans.getDestination(),
						trans.getLabel());
		}

		return this;
	}

	@Override
	public String toString()
	{
		return "NFA: " + transitions.toString() + ", " + outgoing.toString();
	}
}
