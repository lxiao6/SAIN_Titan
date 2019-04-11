package edu.drexel.cs.rise.DesignSpace.GenSdsm;

import java.io.PrintStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.drexel.cs.athena.data.edge;
import edu.drexel.cs.athena.parser.cytoscape.fileDependencyCytoscapeParser;
import edu.drexel.cs.athena.parser.moka.mokaParser;

public class LoadStructure
{

    private Set<String> dpTypes = new HashSet<String>();
    private Map<Integer, String> nodes = new TreeMap<Integer, String>();
    private Set<edge> edges = new HashSet<edge>();

    public void loadInput(PrintStream writer, String input)
    {

        fileDependencyCytoscapeParser myParser = null;
        try {
            myParser = new fileDependencyCytoscapeParser();
            myParser.process(input);
            dpTypes.addAll(myParser.dpTypes);
            nodes.putAll(myParser.nodes);
            edges.addAll(myParser.edges);

            writer.println("# nodes: " + nodes.size());
            writer.println("# edges: " + edges.size());

        }
        catch (Exception e) {
            writer.println(e.getMessage());
            writer.println("Error in parsing dependency file: " + input);
        }

    }

    public void loadInput(PrintStream writer, String input, String valid_prefix)
    {

        mokaParser myParser = null;
        try {
            myParser = new mokaParser(valid_prefix);
            myParser.process(input);
            writer.println("Process input from file: " + input);
            dpTypes.addAll(myParser.dpTypes);
            nodes.putAll(myParser.nodes);
            edges.addAll(myParser.edges);

            // System.out.println(dpTypes.size()+" "+nodes.size()+" "+edges.size());

        }
        catch (Exception e) {

            writer.println("Error in parsing dependency file: " + input);
        }

    }

    public Set<String> getDpTypes()
    {
        // System.out.println(dpTypes.size());
        return dpTypes;
    }

    public Set<edge> getEdges()
    {
        // System.out.println(edges.size());
        return edges;
    }

    public Map<Integer, String> getNodes()
    {
        // System.out.println(nodes.size());
        return nodes;
    }

}
