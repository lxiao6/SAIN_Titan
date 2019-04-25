/*
 * FileParser.java
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
package edu.drexel.cs.rise.minos.cluster;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Stack;

import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public class FileParser
{
	private static final String SCHEMA_FILE = "cluster.xsd";
	private static final SAXParserFactory parserFactory;

	static
	{
		Schema xsd = null;

		try {
			final SchemaFactory factory = SchemaFactory.newInstance(
					XMLConstants.W3C_XML_SCHEMA_NS_URI);
			xsd = factory.newSchema(new StreamSource(
					FileParser.class.getResourceAsStream(SCHEMA_FILE)));

			parserFactory = SAXParserFactory.newInstance();
			parserFactory.setNamespaceAware(false);
			parserFactory.setSchema(xsd);
			parserFactory.setValidating(true);
		}
		catch (Exception ex) {
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static Clustering load(final File path)
			throws MinosException
	{
		try {
			return load(new FileReader(path));
		}
		catch (FileNotFoundException ex) {
			throw new MinosException(ex);
		}
	}

	public static Clustering load(final Reader input)
			throws MinosException
	{
		final ClusterBuilder builder = new ClusterBuilder();

		try {
			SAXParser parser;
			synchronized (parserFactory) {
				parser = parserFactory.newSAXParser();
			}
			parser.parse(new InputSource(input), builder);
		}
		catch (Exception ex) {
			throw new MinosException(ex);
		}

		return builder.getCluster();
	}

	protected static class ClusterBuilder extends DefaultHandler
	{
		private Stack<ClusterSet> stack = new Stack<ClusterSet>();

		public ClusterSet getCluster()
		{
			return stack.peek();
		}
		
		@Override
		public void startElement(final String uri, final String name,
				final String qname, final Attributes attrs) throws SAXException
		{
			if (qname.equals("group")) {
				final ClusterSet set = new ClusterSet(attrs.getValue("name"));
				if (!stack.isEmpty()) {
					set.setParent(stack.peek());
					stack.peek().addCluster(set);
					
				}

				stack.push(set);
			}
			else if (qname.equals("item")) {
				final ClusterItem item = new ClusterItem(attrs.getValue("name"));
				if (stack.isEmpty())
					stack.push(new ClusterSet("root"));

				item.setParent(stack.peek());
				stack.peek().addCluster(item);
			}
			else if (!qname.equals("cluster")){
				throw new SAXException("Unknown tag: <" + qname + ">");
			}
		}

		@Override
		public void endElement(final String uri, final String name,
				final String qname) throws SAXException
		{
			if (qname.equals("group") && stack.size() > 1)
				stack.pop();
		}
	}
}
