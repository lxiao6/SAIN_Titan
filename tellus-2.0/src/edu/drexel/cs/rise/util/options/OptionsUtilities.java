/*
 * OptionsUtilities.java
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
package edu.drexel.cs.rise.util.options;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.drexel.cs.rise.xml.XPathUtilities;

/**
 *
 * @author Sunny Wong
 * @since 1.0
 */
public class OptionsUtilities
{
	private static final DocumentBuilder builder;

	private static final Map<String, Option.Type> optionTypes;

	static
	{
		final Map<String, Option.Type> types = new HashMap<String,
				Option.Type>();

		types.put("", Option.Type.NO_ARGUMENT);
		types.put("none", Option.Type.NO_ARGUMENT);
		types.put("required", Option.Type.REQUIRED_ARGUMENT);
		types.put("optional", Option.Type.OPTIONAL_ARGUMENT);

		optionTypes = Collections.unmodifiableMap(types);

		DocumentBuilder build = null;

		try {
			final DocumentBuilderFactory factory =
					DocumentBuilderFactory.newInstance();
			build = factory.newDocumentBuilder();
		}
		catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
		finally {
			builder = build;
		}
	}
	
	public Map<String, Option> loadOptions(final InputStream input)
			throws OptionException, IOException
	{
		return loadOptions(new InputSource(input));
	}

	public Map<String, Option> loadOptions(final InputSource input)
			throws OptionException, IOException
	{
		final String errorMessage = "Invalid options configuration.";

		Document doc;
		synchronized (builder) {
			try {
				doc = builder.parse(input);
			}
			catch (IOException ex) {
				throw new OptionException(errorMessage, ex);
			}
			catch (SAXException ex) {
				throw new OptionException(errorMessage, ex);
			}
		}

		return loadOptions(doc.getDocumentElement());
	}

	public Map<String, Option> loadOptions(final Element config)
			throws OptionException
	{
		final NodeList opts;
		try {
			opts = XPathUtilities.getNodeList("options/option", config);
		}
		catch (XPathException ex) {
			throw new OptionException("Unable to load options configuration.");
		}

		return loadOptions(opts);
	}

	public Map<String, Option> loadOptions(final NodeList opts)
			throws OptionException
	{
		final Map<String, Option> result = new LinkedHashMap<String, Option>();
		if (opts == null)
			return result;

		for (int i = 0; i < opts.getLength(); ++i) {
			final Element node = (Element) opts.item(i);
			final Option opt = new Option();

			opt.setName(node.getAttribute("name"));
			opt.setDescription(node.getTextContent());
			opt.setType(optionTypes.get(node.getAttribute("argument")));
			result.put(opt.getName(), opt);
		}

		return result;
	}

	public void printUsage(final PrintStream out,
			final Iterable<? extends Option> opts)
	{
		final PrintWriter writer = new PrintWriter(out, true);
		printUsage(writer, opts);
		writer.flush();
	}
	
	public void printUsage(final PrintWriter out,
			final Iterable<? extends Option> opts)
	{
		for (Option opt : opts) {
			out.printf("    %s%s", OptionsParser.optionChar,
					opt.getName());
			switch (opt.getType()) {
			case REQUIRED_ARGUMENT:
				out.print(" value");
				break;
			case OPTIONAL_ARGUMENT:
				out.print(" [value]");
				break;
			}
			out.println();
			out.print("        ");
			out.println(opt.getDescription());
		}
	}
}
