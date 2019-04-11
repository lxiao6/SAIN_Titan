package edu.drexel.cs.rise.titan.action.cluster.generate;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import edu.drexel.cs.rise.ArchDRH.ArchDrh;
import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;
import edu.drexel.cs.rise.tellus.cluster.Clustering;
import edu.drexel.cs.rise.titan.Project;
import edu.drexel.cs.rise.titan.util.ClusterUtilities;
import edu.drexel.cs.rise.util.Digraph;

//import edu.drexel.cs.rise.ArchDRH.a;

public class ArchDrhClusterAction extends AbstractAction
{

    private static final long serialVersionUID = 10L;
    protected final Component parent;

    protected final Project proj;

    public ArchDrhClusterAction(final Project proj, final Component parent)
    {
        this.parent = parent;
        this.proj = proj;
        initialize();
    }

    private void initialize()
    {
        putValue(NAME, "Arch DRH Cluster");
        putValue(MNEMONIC_KEY, (int) 'F');
        putValue(SHORT_DESCRIPTION, "Generate clusters based on design rule hierarchy");
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

        ClusterBuilder cb = new ClusterBuilder();
        cb.buildClusters(proj);

        if (cb.getCluster() == null) {

            JOptionPane.showMessageDialog(parent, "Unable to cluster:\n  "
                    + cb.getGraph().edges().size() + " edges and "
                    + cb.getGraph().vertices().size() + " nodes in dsm", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        final Clustering cls = ClusterUtilities.trim(cb.getCluster(), proj.getStructureDependency()
                .vertices());
        proj.clearCollapsed();
        proj.getCollapsed().add(cls);
        proj.setCluster(cb.getCluster());
        proj.setModified(false);

        JOptionPane.showMessageDialog(parent, "ArchDRH Clustering is done.");

    }

    protected class ClusterBuilder
    {
        private ClusterSet root = null;
        private Digraph<String> graph = null;

        public ClusterSet getCluster()
        {
            return root;
        }

        public Digraph<String> getGraph()
        {
            return graph;
        }

        public void buildClusters(Project proj)
        {

            graph = ClusterUtilities.getCombineGraph(proj);

            // root = (ClusterSet) a.a(graph);

            root = (ClusterSet) ArchDrh.buildArchDRHRecursive(graph);

        }

        /*
         * private void sort(ClusterSet root, Digraph<String> graph) {
         * 
         * Set<String> module = new HashSet<String>();
         * boolean need_sort = true;
         * for(Clustering child:root.clusters()){
         * if(child instanceof ClusterSet){
         * need_sort = false;
         * sort((ClusterSet)child,graph);
         * }
         * if(child instanceof ClusterItem){
         * module.add(child.getName());
         * }
         * }
         * if(need_sort){
         * Digraph<String> subgraph = graph.subset(module);
         * lower_triangle(root,subgraph);
         * }
         * 
         * 
         * }
         */

        private void lower_triangle(ClusterSet root, Digraph<String> graph_input)
        {

            root.clear();
            Map<String, Integer> temp = new HashMap<String, Integer>();
            for (String v : graph_input.vertices()) {
                temp.put(v, graph_input.getOutDegree(v));
            }
            Map<String, Integer> sorted = sortByValues(temp);

            for (String v : sorted.keySet()) {
                ClusterItem item = new ClusterItem(v);
                item.setParent(root);
                root.addCluster(item);
            }
        }

        public <K extends Comparable, V extends Comparable> Map<K, V> sortByValues(Map<K, V> map)
        {
            List<Map.Entry<K, V>> entries = new LinkedList<Map.Entry<K, V>>(map.entrySet());

            Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

                @Override
                public int compare(Entry<K, V> o1, Entry<K, V> o2)
                {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });

            // LinkedHashMap will keep the keys in the order they are inserted
            // which is currently sorted on natural ordering
            Map<K, V> sortedMap = new LinkedHashMap<K, V>();

            for (Map.Entry<K, V> entry : entries) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            return sortedMap;
        }

    }

}
