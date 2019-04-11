package edu.drexel.cs.rise.DesignSpace.GenSdsm;

import java.io.File;
import java.io.PrintStream;
import java.util.Set;

import edu.drexel.cs.rise.DesignSpace.Utilities.ComplexityChecker;
//import edu.drexel.cs.rise.DesignSpace.Utilities.PathUtil;
import edu.drexel.cs.rise.DesignSpace.Utilities.UsageInfo;
import edu.drexel.cs.rise.minos.MinosException;
import edu.drexel.cs.rise.minos.dsm.FileWriter;
import edu.drexel.cs.rise.util.WeightedDigraph;
import edu.drexel.cs.rise.util.options.Argument;
import edu.drexel.cs.rise.util.options.Option;
import edu.drexel.cs.rise.util.options.OptionException;
import edu.drexel.cs.rise.util.options.SimpleOptionsParser;

public class Program {

	/**
	 * @param args
	 */

	// private static Runtime runtime = Runtime.getRuntime();
	private static UsageInfo info;
	private static boolean quite = true;

	public static void main(String[] args) {

		info = new UsageInfo("GenSdsm.config");
		if (args.length < 2) {
			printUsage(System.err);
			return;
		}

		// =======required parameters=======//
		String input = null;
		String output = null;
		String input_type = "cytoscape";

		// =======optional parameters=======//
		// ArrayList<String> language = new ArrayList<String>();
		// ArrayList<String> exts = new ArrayList<String>();
		// String[] exts = null;
		String prefix = "";
		String xprefix = "";

		final SimpleOptionsParser opts = new SimpleOptionsParser();
		for (Option o : info.getOptions().values())
			opts.addOption(o);
		try {
			opts.parse(args);

			for (Argument arg : opts.getArguments()) {
				if (arg.getName().equals("moka")) {
					input_type = "moka";
				}
				if (arg.getName().equals("cytoscape")) {
					input_type = "cytoscape";
				}
				if (arg.getName().equals("verbose")) {
					quite = false;
				}
				if (arg.getName().equals("f")) {
					input = arg.getValue();
				}
				if (arg.getName().equals("o")) {
					output = arg.getValue();

				}
				if (arg.getName().equals("xprefix")) {
					xprefix = arg.getValue();
				}
				if (arg.getName().equals("prefix")) {
					prefix = arg.getValue();
				}

			}

		} catch (OptionException e) {
			printUsage(System.err);
			return;
		}

		if (input == null || output == null) {
			System.err.println("Required parameters are missing...");
			System.err.println("Please specify input and output parameters!");
			printUsage(System.err);
			return;
		}

		LoadStructure content = new LoadStructure();
		if (input_type.equals("moka"))
			content.loadInput(System.out, input, prefix);
		if (input_type.equals("cytoscape"))
			content.loadInput(System.out, input);

		if (!quite)
			ComplexityChecker.getTime("Start");

		Set<String> dpTypes = content.getDpTypes();
		WeightedDigraph<String> graph = GenStructureDsm.process(System.out, content.getNodes(), content.getEdges(), prefix, xprefix, dpTypes);

		try {

			String[] tps = GenStructureDsm.convertDpTypes(dpTypes);

			File po = new File(output);
			FileWriter.saveweighted(tps, graph, null, po);
			if (!quite) {
				System.out.println(tps.length + " types of dependencies");
				System.out.println(graph.vertices().size() + " files in dsm");
				System.out.println(graph.edges().size() + " dependencies in dsm");
				System.out.println("DSM saved in: " + po.getAbsolutePath());
			}

		} catch (MinosException e) {
			e.printStackTrace();
		}

		if (!quite)
			ComplexityChecker.getTime("End");

	}

	static void printUsage(final PrintStream out) {
		info.printInformation(out);
		out.println();

		out.printf("Usage: " + "java -jar GenSdsm.jar \n" + "-cytoscape\n" + "-f value\n" + "-xprefix value [optional] (This is the prefix you don't want to keep in your dsm path, only include everything after this prefix bug not this prefix)\n"
				+ "-prefix value [optional] (This is the prefix you want to keep in your dsm path, include this prefix and everything after)\n" + "-o value \n" + Program.class.getName());
		out.println();
		out.println("where options are:");

		info.printOptions(out);
		out.println();
		out.println(info.getDescription());
	}

}
