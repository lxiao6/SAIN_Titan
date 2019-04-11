package edu.drexel.cs.rise.titan.util;

import java.util.Set;
import java.util.TreeSet;

import edu.drexel.cs.rise.tellus.cluster.ClusterItem;
import edu.drexel.cs.rise.tellus.cluster.ClusterSet;

public class ClusterBuilder
{
    private ClusterSet root = new ClusterSet("root");
    private ClusterSet current = root;

    private char seperator = '.';

    public ClusterSet getCluster()
    {
        return root;
    }

    public void buildClusters(Set<String> items)
    {
        seperator = figureOutSeperator(items);

        String previous = "";
        TreeSet<String> sorted_items = new TreeSet<String>();
        for (String i : items) {
            sorted_items.add(i);
        }

        for (String item : sorted_items) {
            int[] levels = level_up(previous, item);
            int i = levels[0] - levels[1];
            while (i > 0) {
                current = current.getParent();
                i--;
            }
            addItems(item, levels[1]);
            previous = item;
        }

    }

    private char figureOutSeperator(Set<String> items)
    {
        for (String file : items) {
            if (file.contains("/"))
                return '/';
        }
        return '.';
    }

    private void addItems(String item, int level)
    {
        String original_item = item;
        String name;
        int index = 0;
        for (int i = 0; i < level; i++) {
            index = item.indexOf(seperator);
            item = item.substring(index + 1);
        }

        index = item.indexOf(seperator);
        while (index > 0) {
            name = item.substring(0, index);
            addClusterSet(name);
            item = item.substring(index + 1);
            index = item.indexOf(seperator);
        }

        addClusterItem(original_item);

    }

    private void addClusterSet(String name)
    {

        ClusterSet newSet = new ClusterSet(name);
        newSet.setParent(current);
        current.addCluster(newSet);
        current = newSet;
    }

    private void addClusterItem(String name)
    {

        ClusterItem newItem = new ClusterItem(name);
        newItem.setParent(current);
        current.addCluster(newItem);

    }

    private int[] level_up(String previous, String item)
    {
        int count = 0;
        int level = 0;
        int index_p = previous.indexOf(seperator);
        int index_i = item.indexOf(seperator);
        String part_p = "", part_i = "";

        while (index_p > 0) {
            count = count + 1;
            part_p = part_p + previous.substring(0, index_p);
            previous = previous.substring(index_p + 1);
            index_p = previous.indexOf(seperator);

            if (index_i > 0) {
                part_i = part_i + item.substring(0, index_i);
                item = item.substring(index_i + 1);
                index_i = item.indexOf(seperator);
                if (part_i.equals(part_p)) {
                    level = count;
                }
            }
        }
        int[] result = new int[2];
        result[0] = count;
        result[1] = level;
        return result;
    }

}
