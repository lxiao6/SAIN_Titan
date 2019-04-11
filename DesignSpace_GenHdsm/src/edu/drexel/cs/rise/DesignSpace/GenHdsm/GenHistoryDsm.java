/*
 * Copyright (c) 2014 Drexel University
 */

package edu.drexel.cs.rise.DesignSpace.GenHdsm;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;

import edu.drexel.cs.athena.data.changed_file;
import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.rise.DesignSpace.GenHdsm.util.LocalPathUtil;
import edu.drexel.cs.rise.DesignSpace.Utilities.ComplexityChecker;
import edu.drexel.cs.rise.DesignSpace.Utilities.FormatUtil;
import edu.drexel.cs.rise.DesignSpace.Utilities.InputUtil;
import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.minos.dsm.BiWeightedFileParser;
import edu.drexel.cs.rise.util.Pair;
import edu.drexel.cs.rise.util.WeightedDigraph;

//import edu.drexel.cs.rise.DesignSpace.Utilities.FormatUtil;

public class GenHistoryDsm {

	private static Runtime runtime = Runtime.getRuntime();

	public static void genHDSM(String historyLog, String logType, String inDsm, String start, String end, String output) {

		Date startDate = FormatUtil.DateFormat(start);
		Date endDate = FormatUtil.DateFormat(end);

		genHDSM(historyLog, logType, inDsm, startDate, endDate, output);

	}

	public static void genHDSM(String historyLog, String logType, String inDsm, Date start, Date end, String output) {

		/*
		 * /Project project = new Project(); project.loadFromSvn(svn);
		 * System.out.println("loading svn commits: "+project.history().size());
		 */
		ComplexityChecker.getMemoryUsage(runtime, "start ");
		ComplexityChecker.getTime("start ");

		Set<commit> history = new HashSet<>();
		if (logType.equals("svn"))
			history.addAll(InputUtil.loadFromSvn(historyLog));
		if (logType.equals("git"))
			history.addAll(InputUtil.loadFromGit(historyLog));

		System.out.println("Load revision history from " + logType + " log: " + historyLog + "\n" + history.size() + " revisions loaded...");

		WeightedDigraph<String> sdsm = null;
		try {
			sdsm = BiWeightedFileParser.load(new File(inDsm));
		} catch (MinosException e1) {
			JOptionPane.showMessageDialog(null, "Unable to load DSM file, please check the format of your dsm\n " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		ComplexityChecker.getMemoryUsage(runtime, "load sdsm ");
		ComplexityChecker.getTime("load sdsm ");

		System.out.println("Load sdsm from file: " + inDsm + "\n" + sdsm.vertices().size() + " elements loaded...");
		WeightedDigraph<String> hdsm = GenHistoryDsm.revolutionCoupling(sdsm, history, start, end);
		System.out.println("Compute history coupling dsm...");

		ComplexityChecker.getMemoryUsage(runtime, "gen hdsm ");
		ComplexityChecker.getTime("gen hdsm ");

		try {
			File op = new File(output);
			edu.drexel.cs.rise.minos.dsm.FileWriter.saveweighted(hdsm, null, op);
			System.out.println("Writer output history dsm to file: " + op.getAbsolutePath());
		} catch (MinosException e) {
			System.out.println("Error in writing to hdsm in file: " + output);
			return;
		}
		ComplexityChecker.getMemoryUsage(runtime, "end ");
		ComplexityChecker.getTime("end ");

	}

	private static WeightedDigraph<String> revolutionCoupling(WeightedDigraph<String> sdsm, Set<commit> history) {

		WeightedDigraph<String> historygraph = new WeightedDigraph<>();
		for (String vertex : sdsm.vertices()) {
			historygraph.addVertex(vertex);
		}

		Set<Set<String>> change_sets = new HashSet<>();
		for (commit theCommit : history) {

			Set<String> change_set = new HashSet<>();
			for (changed_file file : theCommit.changed_files) {

				String path = LocalPathUtil.processPath(sdsm.vertices(), file.getPath());

				if (path != null) {
					// System.out.println(path);
					change_set.add(path);
				} else {
					// System.out.println("Path is null");
				}
			}
			if (change_set.size() > 0 && change_set.size() < 30) {
				change_sets.add(change_set);
			}

		}

		Map<Pair<String, String>, Integer> edges = new HashMap<>();
		for (Set<String> change_set : change_sets) {

			for (String src : change_set) {
				for (String dest : change_set) {
					if (!src.equals(dest)) {
						Pair<String, String> edge = findEdge(edges, src, dest);
						if (edge == null) {
							Pair<String, String> newEdge = new Pair<>(src, dest);
							edges.put(newEdge, 1);
						} else {
							int weight = edges.get(edge) + 1;
							edges.remove(edge);
							edges.put(edge, weight);
						}
					}
				}
			}
		}

		for (Pair<String, String> edge : edges.keySet()) {
			String w = Integer.toString(edges.get(edge) / 2);
			if (historygraph.containsVertex(edge.first()) && historygraph.containsVertex(edge.second())) {
				historygraph.addEdge(edge.first(), edge.second(), w);
				historygraph.addEdge(edge.second(), edge.first(), w);
			}

		}

		return historygraph;

	}

	private static WeightedDigraph<String> revolutionCoupling(WeightedDigraph<String> sdsm, Set<commit> history, Date start, Date end) {

		Set<commit> includedHistory = new HashSet<>();

		for (commit theCommit : history) {

			if (dateValid(theCommit, start, end)) {

				includedHistory.add(theCommit);
			}

		}

		System.out.println(includedHistory.size() + " revisions after filtering by date.");

		return revolutionCoupling(sdsm, includedHistory);

	}

	private static Pair<String, String> findEdge(Map<Pair<String, String>, Integer> edges, String src, String dest) {

		for (Pair<String, String> edge : edges.keySet()) {
			if ((edge.first().equals(src) && edge.second().equals(dest)) || (edge.first().equals(dest) && edge.second().equals(src))) {
				return edge;
			}
		}
		return null;
	}

	/*
	 * private static String findPath(Set<String> vertices, String path) {
	 * 
	 * 
	 * 
	 * if(path.endsWith(".cpp") || path.endsWith(".h") || path.endsWith(".c")){
	 * 
	 * if(path.endsWith(".cpp") ){ path = path.substring(0,
	 * path.indexOf(".cpp")); path = path+"_c"; }
	 * 
	 * if(path.endsWith(".c") ){ path = path.substring(0, path.indexOf(".c"));
	 * path = path+"_c"; }
	 * 
	 * if(path.endsWith(".h") ){ path = path.substring(0, path.indexOf(".h"));
	 * path = path+"_h"; }
	 * 
	 * path = path.replace("/", "."); if(path.startsWith(".")) path =
	 * path.substring(1);
	 * 
	 * for(String vertex:vertices){ if(path.contains(vertex)){ return vertex; }
	 * } }
	 * 
	 * return null; }
	 */

	private static boolean dateValid(commit c, Date start, Date end) {
		if (start == null && end == null)
			return true;

		if (c.date != null) {
			Date rdate = FormatUtil.DateFormat(c.date);
			return (rdate.before(end) && rdate.after(start));
		}
		return true;

	}

}
