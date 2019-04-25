/*
 * PrintErrorHandler.java
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

import java.io.PrintStream;
import java.io.PrintWriter;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class PrintErrorHandler implements ErrorHandler
{
	private final PrintWriter out;
	private SAXParseException error = null;

	public PrintErrorHandler(final PrintStream out)
	{
		this(new PrintWriter(out));
	}

	public PrintErrorHandler(final PrintWriter out)
	{
		this.out = out;
	}
	
	public final boolean isInvalid()
	{
		return error != null;
	}
	
	public final SAXParseException getException()
	{
		return error;
	}
	
	public final void clear()
	{
		error = null;
	}

	@Override
	public final void error(final SAXParseException ex)
		throws SAXException
	{
		out.print("ERROR: ");
		printDetails(ex);
		error = ex;
	}

	@Override
	public final void fatalError(final SAXParseException ex)
		throws SAXException
	{
		out.print("FATAL ERROR: ");
		printDetails(ex);
		error = ex;
	}

	@Override
	public final void warning(final SAXParseException ex)
		throws SAXException
	{
		out.print("WARNING: ");
		printDetails(ex);
	}

	protected void printDetails(final SAXParseException ex)
	{
		out.print("[");
		out.print(ex.getLineNumber());
		out.print(",");
		out.print(ex.getColumnNumber());
		out.print("]");
		out.println();
		out.print(ex.getMessage());
		out.println();
	}
}
