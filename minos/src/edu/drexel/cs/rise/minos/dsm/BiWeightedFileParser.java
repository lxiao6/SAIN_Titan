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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.util.WeightedDigraph;

/**
 * 
 * @author lu xiao
 * @since 2.0
 */
public class BiWeightedFileParser extends FileParser
{
    public static String[] dpTypes = null;

    public static WeightedDigraph<String> load(final File path) throws MinosException
    {
        return scan(path);
    }

    public static WeightedDigraph<String> scan(final File path) throws MinosException
    {
        final WeightedDigraph<String> dep = new WeightedDigraph<String>();
        Scanner fs;
        try {
            fs = new Scanner(new FileInputStream(path));

            // first read in dependency types
            String dpLine = fs.nextLine();
            dpLine = dpLine.replace("[", "");
            dpLine = dpLine.replace("]", "");
            dpTypes = dpLine.split(",");

            // second line is the number of variables
            String line = fs.nextLine();
            int size = Integer.parseInt(line);
            if (size > 0) {
                // System.out.println("size of dsm is "+size);

                Map<String, String> edges = new HashMap<String, String>();
                for (int row = 0; row < size; row++) {
                    for (int col = 0; col < size; col++) {

                        String w = fs.next();
                        // System.out.println(row+" "+col+" "+w);
                        if (!w.equals("0")) {
                            String key = row + "," + col;
                            edges.put(key, w);
                            // System.out.println(key+" "+w);
                        }
                    }
                }
                fs.nextLine();
                Map<Integer, String> nodes = new HashMap<Integer, String>();
                for (int i = 0; i < size; i++) {
                    String v = fs.nextLine();
                    dep.addVertex(v);
                    nodes.put(i, v);
                }

                fs.close();

                for (String key : edges.keySet()) {
                    String w = edges.get(key);
                    String[] index = key.split(",");

                    int row = Integer.parseInt(index[0]);
                    int col = Integer.parseInt(index[1]);

                    dep.addEdge(nodes.get(col), nodes.get(row), w);
                    // dep.addEdge(nodes.get(row), nodes.get(col), w);
                }
            }

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return dep;
    }

}
