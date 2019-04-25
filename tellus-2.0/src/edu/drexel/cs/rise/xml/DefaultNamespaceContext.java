/*
 * DefaultNamespaceContext.java
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public class DefaultNamespaceContext implements NamespaceContext
{
	private Map<String, String> prefixes = new HashMap<String, String>();
	private Map<String, Set<String>> uris =
			new HashMap<String, Set<String>>();

	public void addNamespace(final String prefix, final String uri)
	{
		if (prefixes.containsKey(prefix)) {
			if (!prefixes.get(prefix).equals(uri))
				throw new IllegalArgumentException(
						"Prefix already assigned to another namespace URI.");
			else
				return;
		}

		if (!uris.containsKey(uri))
			uris.put(uri, new HashSet<String>());

		uris.get(uri).add(prefix);
		prefixes.put(prefix, uri);
	}

	public void removeNamespace(final String uri)
	{
		if (!uris.containsKey(uri))
			return;

		for (String p : uris.get(uri))
			prefixes.remove(p);

		uris.remove(uri);
	}

	@Override
	public String getNamespaceURI(final String prefix)
	{
		return prefixes.get(prefix);
	}

	@Override
	public String getPrefix(final String uri)
	{
		final Iterator<String> prefs = getPrefixes(uri);

		if (prefs != null)
			return prefs.next();
		else
			return null;
	}

	@Override
	public Iterator<String> getPrefixes(final String uri)
	{
		final Set<String> prefs = uris.get(uri);

		if (prefs != null)
			return prefs.iterator();
		else
			return null;
	}

}
