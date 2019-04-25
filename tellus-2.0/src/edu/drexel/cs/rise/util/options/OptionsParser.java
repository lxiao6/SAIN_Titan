/*
 * OptionsParser.java
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.drexel.cs.rise.util.options.Option.Type;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class OptionsParser implements Iterable<Argument>
{
	public static final char optionChar = '-';
	public static final String optionDelimiter = "-";

	private Map<String, Option> options;
	private List<Argument> arguments;

	public OptionsParser(final Option... opts)
	{
		arguments = new ArrayList<Argument>();
		options = new HashMap<String, Option>();
		for (Option o : opts)
			addOption(o);
	}
	
	public void addOption(final Option o)
	{
		if (o.getType() == null)
			throw new IllegalArgumentException("Invalid (null) option type.");

		options.put(o.getName(), o);
	}

	public Map<String, Option> getOptions()
	{
		return Collections.unmodifiableMap(options);
	}

	public void parse(final String... args) throws OptionException
	{
		arguments.clear();

		int i;

		for (i = 0; i < args.length; ++i) {
			if (args[i].length() == 0 || args[i].charAt(0) != optionChar) {
				unknownArgument(args[i]);
				continue;
			}

			final String name = args[i].substring(1);
			if (name.equals(optionDelimiter))
				break;

			final Option opt = options.get(name);
			// ignore options we do not know about
			if (opt == null) {
				unknownOption(name);
				continue;
			}

			switch (opt.getType()) {
			case NO_ARGUMENT:
				arguments.add(new Argument(opt, name));
				break;
			case REQUIRED_ARGUMENT:
				++i;
				if (i < args.length)
					arguments.add(new Argument(opt, name, args[i]));
				else
					throw new OptionException("Missing argument value for '"
							+ name + "'");
				break;
			case OPTIONAL_ARGUMENT:
				if (i == args.length - 1 || args[i + 1].charAt(0) == optionChar)
					arguments.add(new Argument(opt, name));
				else {
					++i;
					arguments.add(new Argument(opt, name, args[i]));
				}
				break;
			default:
				// should have been checked for already
				throw new IllegalStateException();
			}
		}

		for ( ; i < args.length; ++i)
			unknownArgument(args[i]);
	}

	protected void unknownArgument(final String arg)
	{
		// left for children to override
	}
	
	protected void unknownOption(final String name)
	{
		// left for children to override
	}

	public List<Argument> getArguments()
	{
		return Collections.unmodifiableList(arguments);
	}

	@Override
	public Iterator<Argument> iterator()
	{
		return arguments.iterator();
	}

	@Override
	public String toString()
	{
		final StringBuilder buf = new StringBuilder();

		for (Option opt : options.values()) {
			buf.append(optionChar);
			buf.append(opt.getName());
			buf.append(' ');
			if (opt.getType() == Type.OPTIONAL_ARGUMENT)
				buf.append('[');

			if (opt.getType() != Type.NO_ARGUMENT)
				buf.append("ARG");

			if (opt.getType() == Type.OPTIONAL_ARGUMENT)
				buf.append(']');
		}
		
		return buf.toString();
	}
}
