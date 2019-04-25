/*
 * XmlWriter.java
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
package edu.drexel.cs.rise.xml;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 *
 * @author Sunny Wong
 * @since 1.0
 */
public class XmlWriter extends PrintWriter
{
	public XmlWriter(final PrintWriter out)
	{
		super(out);
	}
	
	public XmlWriter(final OutputStream out)
	{
		super(out);
	}

	private int indent = 0;

	public final void tabify(int n)
	{
		for (int i = 0; i < n; ++i)
			print('\t');
	}
	
	public final void tabify()
	{
		tabify(indent);
	}

	public final <T> void printAttribute(final String name, final T value)
	{
		print(" ");
		print(name);
		print("=\"");
		print(protect(value.toString()));
		print("\"");
	}

	public final int getIndentLevel()
	{
		return indent;
	}

	public final void increaseIndentation()
	{
		++indent;
	}

	public final void decreaseIndentation()
	{
		--indent;
	}

	public final void printSimpleTag(final String tag, final String body)
	{
		print('<');
		print(tag);
		print('>');
		print(protect(body));
		print("</");
		print(tag);
		print('>');
	}
	
	public void printProtected(final String text)
	{
		print(protect(text));
	}

	public static final String protect(final String xml)
	{
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < xml.length(); ++i) {
			final char c = xml.charAt(i);
			switch (c) {
			case '\'':
				buf.append("&apos;");
				break;
			case '\"':
				buf.append("&quot;");
				break;
			case '&':
				buf.append("&amp;");
				break;
			default:
				buf.append(c);
			}
		}
		return buf.toString();
	}
}
