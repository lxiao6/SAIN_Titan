package edu.drexel.cs.rise.DesignSpace.GenBuglist;

import java.io.PrintStream;

import edu.drexel.cs.rise.DesignSpace.Utilities.UsageInfo;
import edu.drexel.cs.rise.util.options.Argument;
import edu.drexel.cs.rise.util.options.Option;
import edu.drexel.cs.rise.util.options.OptionException;
import edu.drexel.cs.rise.util.options.SimpleOptionsParser;

public class Program {

	/**
	 * @param args
	 */
	private static UsageInfo info;

	public static void main(String[] args) {

		info = new UsageInfo("GenBuglist.config");
		if (args.length < 5) {
			printUsage(System.err);
			System.out.println("Less than 5 parameters\n");
			return;
		}

		// =======required parameters=======//
		String inDsm = null;
		String log = null;
		String log_type = "";
		String start = null;
		String end = null;
		String output = null;
		String[] keys = null;

		// =======optional parameters=======//
		int bugFreq = 1;
		String jiraBugs = null;
		String bugDataType = "";

		final SimpleOptionsParser opts = new SimpleOptionsParser();
		for (Option o : info.getOptions().values())
			opts.addOption(o);
		try {
			opts.parse(args);
			for (Argument arg : opts.getArguments()) {

				if (arg.getName().equals("inDsm")) {
					inDsm = arg.getValue();
				}
				if (arg.getName().equals("svn")) {
					log = arg.getValue();
					log_type = "svn";
				}
				if (arg.getName().equals("git")) {
					log = arg.getValue();
					log_type = "git";
				}

				if (arg.getName().equals("sDate"))
					start = arg.getValue();
				if (arg.getName().equals("eDate"))
					end = arg.getValue();
				if (arg.getName().equals("o"))
					output = arg.getValue();
				if (arg.getName().equals("keys")) {
					if (arg.getValue().contains(","))
						keys = arg.getValue().split(",");
					else {
						keys = new String[1];
						keys[0] = arg.getValue();
					}
				}
				if (arg.getName().equals("bugFreq"))
					bugFreq = Integer.parseInt(arg.getValue());

				if (arg.getName().equals("jiraBugs")) {
					jiraBugs = arg.getValue();
					bugDataType = "jira";
				}
				if (arg.getName().equals("bugList")) {
					jiraBugs = arg.getValue();
					bugDataType = "list";
				}

				/*
				 * switch(arg.getName()){ case "inDsm": inDsm = arg.getValue();
				 * break; case "svn": svn = arg.getValue(); break; case "sDate":
				 * start = FormatUtil.DateFormat(arg.getValue()); break; case
				 * "eDate": end = FormatUtil.DateFormat(arg.getValue()); break;
				 * case "o": output = arg.getValue(); break; case "keys":
				 * if(arg.getValue().contains(",")) keys =
				 * arg.getValue().split(","); else { keys = new String[1];
				 * keys[0] = arg.getValue(); } break; case "bugFreq": bugFreq =
				 * Integer.parseInt(arg.getValue()); break; case "jiraBugs":
				 * jiraBugs = arg.getValue(); break; case "l": String tmp =
				 * arg.getValue(); String[] tmp2 = tmp.split(","); for(int i =
				 * 0; i < tmp2.length; i++){ language.add(tmp2[i]); } break;
				 * 
				 * }
				 */
			}

		} catch (OptionException e) {
			printUsage(System.err);
			return;
		}

		if (inDsm == null || log == null || output == null) {
			System.err.println("****Parameters missing...");
			printUsage(System.err);
			return;
		}
		/*
		 * LangInfo langinfo = new LangInfo("Language.config");
		 * 
		 * ArrayList<String> langs = langinfo.getKeyLang();
		 * 
		 * for(String lang : langs){ for(String lang2 : language){
		 * if(language.equals(lang)){ exts.addAll(langinfo.getSetLang(lang)); }
		 * } }
		 * 
		 * 
		 * if(language.equals("c")){ exts = new String[3]; exts[0] = ".c";
		 * exts[1] = ".cpp"; exts[2] = ".h"; }
		 */

		GenBuglist.setBugFreq(bugFreq);

		if (bugDataType.equals("list")) {

			GenBuglist.genBugSpaceFromBugIDList(log, log_type, inDsm, start, end, keys, jiraBugs, output);

		}

		if (bugDataType.equals("jira")) {

			GenBuglist.genBugSpaceFromJiraReports(log, log_type, inDsm, start, end, keys, jiraBugs, output);
		}

	}

	static void printUsage(final PrintStream out) {
		info.printInformation(out);
		out.println();

		out.printf("Usage: " + "java -jar GenBuglist.jar \n" + "-inDsm value\n" + "-svn/-git value\n" + "-sDate value e.g. MM/dd/yyyy\n" + "-eDate value e.g. MM/dd/yyyy\n" + "-keys value[,value,value...,value]\n"
				+ "-bugFreq value [optional] 1 by default\n" + "-jiraBugs value [optional] The path to the jira xml files\n" + "-bugList value [optional] The path to a file containing a list of bug keys\n" + "-o value \n"
				+ "If none of -jiraBugs and -bugList is set, every bug key is considered to be a bug key", Program.class.getName());
		out.println();
		out.println("where options are:");

		info.printOptions(out);
		out.println();
		out.println(info.getDescription());
	}

}
