/*
 * XPathUtilities.java
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
package edu.drexel.cs.rise.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathException;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 
 * @author Sunny Wong
 * @since 0.2
 */
public final class XPathUtilities
{
	private static final XPath xpath = XPathFactory.newInstance().newXPath();

	private XPathUtilities()
	{
		// prevent instantiation
	}

	public static NodeList getNodeList(final String path, final Node root)
			throws XPathException
	{
		final XPathExpression expr = compile(path);
		return (NodeList) expr.evaluate(root, XPathConstants.NODESET);
	}

	public static NodeList getNodeList(final String path, final Node root,
			final NamespaceContext ns) throws XPathException
	{
		NodeList result = null;

		synchronized (xpath) {
			final NamespaceContext prev = xpath.getNamespaceContext();
			xpath.setNamespaceContext(ns);
			result = getNodeList(path, root);
			if (prev != null)
				xpath.setNamespaceContext(prev);
		}

		return result;
	}
	
	public static Node getNode(final String path, final Node root)
			throws XPathException
	{
		final XPathExpression expr = compile(path);
		return (Node) expr.evaluate(root, XPathConstants.NODE);
	}

	public static Node getNode(final String path, final Node root,
			final NamespaceContext ns) throws XPathException
	{
		Node result = null;

		synchronized (xpath) {
			final NamespaceContext prev = xpath.getNamespaceContext();
			xpath.setNamespaceContext(ns);
			result = getNode(path, root);
			if (prev != null)
				xpath.setNamespaceContext(prev);
		}

		return result;
	}

	public static Element getElement(final String path, final Node root)
			throws XPathException
	{
		final XPathExpression expr = compile(path);
		return (Element) expr.evaluate(root, XPathConstants.NODE);
	}

	public static Element getElement(final String path, final Node root,
			final NamespaceContext ns) throws XPathException
	{
		Element result = null;

		synchronized (xpath) {
			final NamespaceContext prev = xpath.getNamespaceContext();
			xpath.setNamespaceContext(ns);
			result = getElement(path, root);
			if (prev != null)
				xpath.setNamespaceContext(prev);
		}

		return result;
	}
	
	private static XPathExpression compile(final String path)
			throws XPathException
	{
		synchronized (xpath) {
			return xpath.compile(path);
		}
	}
}
