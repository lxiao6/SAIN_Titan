/*
 * Option.java
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
package edu.drexel.cs.rise.util.options;

import java.io.Serializable;

import edu.drexel.cs.rise.util.Hasher;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class Option implements Serializable
{
	private static final long serialVersionUID = 10L;

	public enum Type
	{
		NO_ARGUMENT,
		REQUIRED_ARGUMENT,
		OPTIONAL_ARGUMENT
	}

	private String name;
	private Type type;
	private String description;

	public Option()
	{
		this("", null);
	}

	public Option(final String name, Type type)
	{
		this.name = name;
		this.type = type;
	}

	public final String getName()
	{
		return name;
	}

	public final void setName(final String name)
	{
		this.name = name;
	}

	public final Type getType()
	{
		return type;
	}

	public final void setType(Type type)
	{
		this.type = type;
	}

	public final String getDescription()
	{
		return description;
	}

	public final void setDescription(final String text)
	{
		this.description = text;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (obj == this)
			return true;
		else if (obj == null || obj.getClass() != getClass())
			return false;

		final Option opt = (Option) obj;
		return Hasher.equals(opt.getName(), name) && opt.getType() == type;
	}

	@Override
	public int hashCode()
	{
		return Hasher.hashAll(name, type);
	}
}
