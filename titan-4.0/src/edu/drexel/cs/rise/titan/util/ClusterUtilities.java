/*
 * ClusterUtilities.java
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
package edu.drexel.cs.rise.titan.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import edu.drexel.cs.rise.DesignSpace.Utilities.ComplexityChecker;
import edu.drexel.cs.rise.DesignSpace.data.DependencyType;
import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.ClusterVisitor;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.model.MatrixModel;
import edu.drexel.cs.rise.util.Digraph;
import edu.drexel.cs.rise.util.Interval;
import edu.drexel.cs.rise.util.Matrix;
import edu.drexel.cs.rise.util.TreeNode;
import edu.drexel.cs.rise.util.WeightedDigraph;

/**
 *
 * @author Sunny Wong
 * @since 0.2
 */
public final class ClusterUtilities
{

    static Runtime runtime = Runtime.getRuntime();
    private static final Pattern nameRegex = Pattern.compile("[a-zA-Z_$][a-zA-Z0-9_$.]*");

    public static Clustering buildDefaultCluster(final WeightedDigraph<String> deps)
    {
        final ClusterSet root = new ClusterSet("$root");
        final Set<String> variables = deps.vertices();

        for (String var : variables)
            root.addCluster(new ClusterItem(var));

        return root;
    }

    public static Clustering trim(final Clustering cls, final Set<String> variables)
    {
        final Stack<ClusterSet> stack = new Stack<ClusterSet>();

        cls.visit(new ClusterVisitor() {
            @Override
            public void visit(final ClusterSet set)
            {
                final ClusterSet cls = new ClusterSet(set.getName());

                if (!stack.empty()) {
                    final ClusterSet parent = stack.peek();
                    cls.setParent(parent);
                    parent.addCluster(cls);
                }

                stack.push(cls);
                for (Clustering child : set)
                    child.visit(this);

                if (stack.size() > 1)
                    stack.pop();
            }

            @Override
            public void visit(final ClusterItem item)
            {
                final String name = item.getName();
                if (!variables.contains(name))
                    return;

                if (stack.empty())
                    stack.push(new ClusterSet("$root"));

                final ClusterSet parent = stack.peek();
                final ClusterItem cls = new ClusterItem(name, parent);
                parent.addCluster(cls);
            }
        });

        if (stack.empty())
            return null;
        else
            return stack.pop();
    }

    public static void buildMatrix(final Project proj, final MatrixModel model)
    {
        ComplexityChecker.getMemoryUsage(runtime, "Start build matrix");
        long t1 = ComplexityChecker.getTime("Start build matrix: ");
        final Clustering root = proj.getCluster();
        if (root == null) {
            System.out.println("return because root is null");
            model.setStructureData(new Matrix<Integer, String>(Collections.<String> emptyList()),
                    null);
            return;
        }

        final WeightedDigraph<String> structureDsm = proj.getStructureDependency();
        final WeightedDigraph<String> historyDsm = proj.getHistoryDependency();
        final WeightedDigraph<String> f2fDsm = proj.getf2fDependency();

        final Map<String, List<String>> groups = new LinkedHashMap<String, List<String>>(
                structureDsm.order());
        final Set<Clustering> collapsed = proj.getCollapsed();
        final Stack<TreeNode<Interval>> tree = new Stack<TreeNode<Interval>>();

        // get labels
        root.visit(new ClusterVisitor() {
            private List<String> childs = null;
            private int n = 1;
            private int k = 0;

            @Override
            public void visit(final ClusterSet set)
            {
                final boolean terminal = childs == null && collapsed.contains(set);

                if (terminal)
                    childs = new ArrayList<String>();

                final TreeNode<Interval> node = new TreeNode<Interval>();
                if (!tree.empty())
                    tree.peek().appendChild(node);

                tree.push(node);

                final int start = k;
                for (Clustering cls : set)
                    cls.visit(this);

                if (terminal) {
                    final String name = getName(set);
                    groups.put(name, childs);
                    childs = null;
                    ++k;
                }

                final int stop = k;
                node.setValue(new Interval(start, stop));
                if (tree.size() > 1)
                    tree.pop();
            }

            @Override
            public void visit(final ClusterItem item)
            {
                if (childs != null)
                    childs.add(item.getName());
                else {
                    final String name = getName(item);
                    groups.put(name, Collections.singletonList(item.getName()));
                    ++k;
                }
            }

            private String getName(final Clustering cls)
            {
                final String name = n + "  " + cls.getName();
                ++n;
                return name;
            }
        });

        final List<String> labels = new ArrayList<String>(groups.keySet());
        final Matrix<Integer, String> matrix = new Matrix<Integer, String>(labels);

        final Matrix<Integer, String> cluster_matrix = new Matrix<Integer, String>(labels);

        for (int row = 0; row < labels.size(); ++row) {
            final List<String> dest = groups.get(labels.get(row));

            for (int col = 0; col < labels.size(); ++col) {
                if (row == col)
                    continue;

                final List<String> src = groups.get(labels.get(col));
                matrix.set(row, col,
                        countDependencies(src, dest, structureDsm, proj.getDPControl()));
                cluster_matrix.set(row, col, itemsType(src, dest));
            }
        }

        final Matrix<Integer, String> history_matrix = new Matrix<Integer, String>(labels);

        if (historyDsm != null) {
            for (int row = 0; row < labels.size(); ++row) {
                final List<String> dest = groups.get(labels.get(row));

                for (int col = 0; col < labels.size(); ++col) {
                    if (row == col)
                        continue;

                    final List<String> src = groups.get(labels.get(col));
                    // if(row > col)
                    history_matrix.set(row, col, sumIntDependencies(src, dest, historyDsm));
                    // else history_matrix.set(row, col, 0);
                }
            }

            model.setHistoryData(history_matrix);
        }

        final Matrix<Double, String> f2f_matrix = new Matrix<Double, String>(labels);

        if (f2fDsm != null) {
            for (int row = 0; row < labels.size(); ++row) {
                final List<String> dest = groups.get(labels.get(row));

                for (int col = 0; col < labels.size(); ++col) {
                    if (row == col)
                        continue;

                    final List<String> src = groups.get(labels.get(col));
                    // if(row > col)
                    double value = sumDoubleDependencies(src, dest, f2fDsm);
                    f2f_matrix.set(row, col, value);
                    System.out.println(value);
                    // else history_matrix.set(row, col, 0);
                }
            }

            model.setf2fData(f2f_matrix);
        }

        TreeNode<Interval> modules = null;
        if (!tree.empty())
            modules = tree.pop();

        ComplexityChecker.getMemoryUsage(runtime, "End build matrix and start fire JTable change:");
        long t2 = ComplexityChecker.getTime("End build matrix and start fire JTable change: ");

        model.setStructureData(matrix, modules);
        long t3 = ComplexityChecker.getTime("End fire JTable change: ");
        ComplexityChecker.getMemoryUsage(runtime, "End fire JTable change:");
        System.out.println("build matrix time: " + (t2 - t1) / 1000);
        System.out.println("Fire Jtable change time: " + (t3 - t2) / 1000);
        model.setClusterData(cluster_matrix);
    }

    private static Integer itemsType(List<String> src, List<String> dest)
    {

        if (src.size() == 1 && dest.size() == 1) {
            return 1; /* means one to one relationship */
        }
        return 0; /* means many to many */
    }

    private static Integer countDependencies(List<String> src, List<String> dest,
            WeightedDigraph<String> deps, DependencyType dpControl)
    {

        int n = 0;

        if (src.size() == 1 && dest.size() == 1) {
            for (String s : src) {
                for (String d : dest) {
                    if (deps.containsEdge(s, d)) {
                        String w = dpControl.filter(deps.getEdge(s, d).weight());
                        return Integer.parseInt(w, 2);
                    } // now it is binary code
                }
            }

        }
        for (String s : src) {
            for (String d : dest) {
                if (deps.containsEdge(s, d)) {
                    String w = dpControl.filter(deps.getEdge(s, d).weight());
                    if (w.contains("1")) {
                        ++n;
                    }
                }
            }
        }

        return n;
    }

    protected static int countDependencies(final Collection<String> src,
            final Collection<String> dest, final WeightedDigraph<String> deps)
    {
        int n = 0;

        if (src.size() == 1 && dest.size() == 1) {
            for (String s : src) {
                for (String d : dest) {
                    if (deps.containsEdge(s, d))
                        return Integer.parseInt(deps.getEdge(s, d).weight(), 2); // now it is binary
                                                                                 // code
                }
            }

        }
        for (String s : src) {
            for (String d : dest) {
                if (deps.containsEdge(s, d))
                    ++n;
            }
        }

        return n;
    }

    protected static int sumIntDependencies(final Collection<String> src,
            final Collection<String> dest, final WeightedDigraph<String> deps)
    {
        int n = 0;

        if (src.size() == 1 && dest.size() == 1) {
            for (String s : src) {
                for (String d : dest) {
                    if (deps.containsEdge(s, d))
                        return Integer.parseInt(deps.getEdge(s, d).weight());
                }
            }

        }
        for (String s : src) {
            for (String d : dest) {
                if (deps.containsEdge(s, d))
                    n = n + Integer.parseInt(deps.getEdge(s, d).weight());
            }
        }

        return n;
    }

    protected static double sumDoubleDependencies(final Collection<String> src,
            final Collection<String> dest, final WeightedDigraph<String> deps)
    {
        double n = 0;

        if (src.size() == 1 && dest.size() == 1) {
            for (String s : src) {
                for (String d : dest) {
                    if (deps.containsEdge(s, d))
                        return Double.parseDouble(deps.getEdge(s, d).weight());
                }
            }

        }
        for (String s : src) {
            for (String d : dest) {
                if (deps.containsEdge(s, d))
                    n = n + Double.parseDouble(deps.getEdge(s, d).weight());
            }
        }

        return n;
    }

    // This method is currently disabled, all names are acceptable.
    public static boolean isValidName(final String name)
    {
        /*
         * final Matcher matcher;
         * synchronized (nameRegex) {
         * matcher = nameRegex.matcher(name);
         * }
         * 
         * return matcher.matches();
         */
        return true;
    }

    public static Digraph<String> getGraph(Project proj)
    {

        return getCombineGraph(proj);
    }

    /*
     * public static WeightedDigraph<String> getWeightedGraph(Project proj) {
     * 
     * note the number of items reduced after this function because the isolated elements are
     * excluded
     * The reason isolated elements are excluded is because the new graph is formed by adding edges
     * that satify the
     * pannel selection.
     * If there is no edge linked to an item, it will not be added to the new graph
     * WeightedDigraph<String> structureDependency = proj.getStructureDependency();
     * WeightedDigraph<String> graph = new WeightedDigraph<String>(structureDependency);
     * 
     * 
     * //System.out.println("Project weighted graph "+structureDependency.vertices().size());
     * 
     * WeightedDigraph<String> historyDependency = proj.getHistoryDependency();
     * 
     * 
     * if(structureDependency != null)
     * {
     * if(!proj.isInherit()){
     * graph.removeAllEdgeByWeight(DependencyType.inherit);
     * }
     * if(!proj.isRealize()){
     * graph.removeAllEdgeByWeight(DependencyType.realize);
     * //graph.union(structureDependency.subGraphWeight(DependencyType.realize));
     * }
     * if(!proj.isAggregate()){
     * graph.removeAllEdgeByWeight(DependencyType.aggregate);
     * //graph.union(structureDependency.subGraphWeight(DependencyType.aggregate));
     * }
     * if(!proj.isDepend()){
     * graph.removeAllEdgeByWeight(DependencyType.depend);
     * //graph.union(structureDependency.subGraphWeight(DependencyType.depend));
     * }
     * if(!proj.isNested()){
     * graph.removeAllEdgeByWeight(DependencyType.nested);
     * //graph.union(structureDependency.subGraphWeight(DependencyType.nested));
     * }
     * }
     * 
     * if(historyDependency != null && proj.isHistory())
     * graph.union(historyDependency.toWeightedDigraphThreshold(proj.getThreshold()));
     * 
     * 
     * return graph;
     * }
     */

    public static Digraph<String> getCombineGraph(Project proj)
    {
        WeightedDigraph<String> f2fDependency = proj.getf2fDependency();
        WeightedDigraph<String> structureDependency = proj.getStructureDependency();
        WeightedDigraph<String> graph = new WeightedDigraph<String>(structureDependency);

        WeightedDigraph<String> historyDependency = proj.getHistoryDependency();

        if (structureDependency != null) {
            proj.getDPControl().removeUnselectedTypes(graph);

        }

        if (historyDependency != null && proj.isHistory())
            graph.union(historyDependency.toWeightedDigraphThreshold(proj.getHistoryDsmThreshold()));

        if (f2fDependency != null && proj.isf2f())
            graph.union(f2fDependency.toWeightedDigraphThreshold(proj.getf2fDsmThreshold()));

        return graph.toDigraph();
    }

    public static WeightedDigraph<String> getStructureGraph(Project proj)
    {

        WeightedDigraph<String> structureDependency = proj.getStructureDependency();
        WeightedDigraph<String> graph = new WeightedDigraph<String>(structureDependency);
        // System.out.println("before structure graph has number of node: "+graph.vertices().size());
        if (graph != null) {
            proj.getDPControl().removeUnselectedTypes(graph);
        }
        // System.out.println("after structure graph has number of node: "+graph.vertices().size());
        return graph;

    }

    public static WeightedDigraph<String> getHistoryGraph(Project proj)
    {

        WeightedDigraph<String> graph = new WeightedDigraph<String>();
        WeightedDigraph<String> historyDependency = proj.getHistoryDependency();

        if (historyDependency != null && proj.isHistory())
            graph.union(historyDependency.subGraphThreshold(proj.getHistoryDsmThreshold()));

        return graph;
    }
}
