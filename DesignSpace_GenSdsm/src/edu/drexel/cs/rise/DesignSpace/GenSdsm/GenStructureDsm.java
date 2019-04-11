package edu.drexel.cs.rise.DesignSpace.GenSdsm;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.drexel.cs.athena.data.edge;
import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.minos.dsm.FileWriter;
import edu.drexel.cs.rise.util.WeightedDigraph;

public class GenStructureDsm {

	public static void generateSDSM(String cytoscapePath, String prefix, String xprefix, String output) {

		LoadStructure content = new LoadStructure();
		content.loadInput(System.out, cytoscapePath);

		Set<String> dpTypes = content.getDpTypes();

		WeightedDigraph<String> graph = process(System.out, content.getNodes(), content.getEdges(), prefix, xprefix, dpTypes);

		String[] tps = GenStructureDsm.convertDpTypes(dpTypes);

		File po = new File(output);

		try {
			FileWriter.saveweighted(tps, graph, null, po);
		} catch (MinosException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}

		System.out.println(tps.length + " types of dependencies");
		System.out.println(graph.vertices().size() + " files in dsm");
		System.out.println(graph.edges().size() + " dependencies in dsm");
		System.out.println("DSM saved in: " + po.getAbsolutePath());

	}

	public static WeightedDigraph<String> process(PrintStream writer, Map<Integer, String> nodes, Set<edge> edges, String prefixField, String xprefixField, Set<String> selectedDpTypes) {

		String[] prefix = null, xprefix = null;

		if (prefixField != null)
			prefix = prefixField.trim().split(",");

		if (xprefixField != null)
			xprefix = xprefixField.trim().split(",");

		// System.out.println(xprefix.length+" "+prefix.length);

		/* Process each path name */
		Map<Integer, String> format_nodes = new HashMap<>();

		for (int id : nodes.keySet()) {
			String p = nodes.get(id);

			String np = PathUtil.processPath(p, xprefix, prefix);

			// System.out.println("path " + np);

			if (np != null)
				format_nodes.put(id, np);
		}

		WeightedDigraph<String> graph = new WeightedDigraph<>();

		/* Add node to graph */
		for (String v : format_nodes.values()) {

			graph.addVertex(v);

		}

		/* Add edge to graph */
		for (edge ed : edges) {

			String s = format_nodes.get(ed.s());
			String t = format_nodes.get(ed.t());
			String w = "";
			for (String dp : selectedDpTypes) {
				if (ed.dps().contains(dp)) {
					w = w + "1";
				} else {
					w = w + "0";
				}
			}

			if (w.contains("1") && s != null && t != null) {
				if (graph.containsVertex(s) && graph.containsVertex(t))
					graph.addEdge(s, t, w);
			}
		}
		return graph;
	}

	public static String[] convertDpTypes(Set<String> dpTypes) {

		String[] tps = new String[dpTypes.size()];
		int index = 0;
		for (String s : dpTypes) {
			tps[index] = s;
			index++;

		}

		return tps;
	}

}
