/*
 * FileWriter.java
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
import java.io.IOException;
import java.io.PrintWriter;

import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.ClusterVisitor;
import edu.drexel.cs.rise.tellus.cluster.Clustering;

/**
 *
 * @author Sunny Wong
 * @since 0.1
 */
public class FileWriter
{
	public static void save(final Clustering cluster, final File path)
			throws MinosException
	{
		try {
			final PrintWriter out = new PrintWriter(path);
			save(cluster, out);

			out.flush();
			out.close();
		}
		catch (IOException ex) {
			throw new MinosException(ex);
		}
	}
	
	public static void save(final Clustering cluster, final PrintWriter out)
			throws MinosException
	{
		if (cluster == null)
			return;

		out.println("<cluster>");
		cluster.visit(new ClusterVisitor() {
			@Override
			public void visit(final ClusterItem item)
			{
				out.print("<item name=\"");
				out.print(item.getName());
				out.println("\" />");
			}

			@Override
			public void visit(final ClusterSet set)
			{
				out.print("<group name=\"");
				out.print(set.getName());
				out.println("\">");
				
				for (Clustering cls : set)
					cls.visit(this);

				out.println("</group>");
			}
		});
		out.println("</cluster>");
		out.flush();
	}
}

