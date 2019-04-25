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
package edu.drexel.cs.rise.minos.dsm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.util.AbstractGraph;
import edu.drexel.cs.rise.util.Digraph;

/**
 *
 * @author Kanwarpreet Sethi
 * @since 0.1
 * 
 * @author lu
 * @since 2.0
 */
public class FileParser
{
	public FileParser()
	{
	}

	public static AbstractGraph<String> load(final File path) throws MinosException
	{
		try {
			return load(new java.io.FileReader(path));	
		}
		catch (FileNotFoundException ex) {
			throw new MinosException(ex);
		}
	}

	public static AbstractGraph<String> load(final Reader input) throws MinosException
	{
			
		final Digraph<String> dep = new Digraph<String>();
		try {
			final BufferedReader br = new BufferedReader(input);

			//first line is the number of variables
			String line = br.readLine();
			int arraysize = Integer.parseInt(line);
			
			int counter = 0;
			String[] dsm = new String[arraysize];
			String[] vars = new String[arraysize];
			while((line = br.readLine()) != null) {
				if (counter < arraysize) {
					dsm[counter] = line;
				}
				else {
					vars[counter%arraysize] = line;
					dep.addVertex(line);
				}
				counter++;
			}
			
			for (int i = 0; i < arraysize; i++) {
				String[] temp = dsm[i].split(" ");
				for (int j = 0; j < temp.length; j++) {
					if (temp[j].equals("1"))
						dep.addEdge(vars[j], vars[i]);
				}
			}
			
			return dep;
		}
		catch (IOException ex) {
			throw new MinosException(ex);
		}
	}
}