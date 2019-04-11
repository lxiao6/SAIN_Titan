package edu.drexel.cs.rise.DesignSpace.GenHdsm;

import java.io.PrintStream;
import java.util.Date;

import edu.drexel.cs.rise.DesignSpace.Utilities.FormatUtil;
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

		info = new UsageInfo("GenHdsm.config");
		if (args.length < 5) {
			printUsage(System.err);
			return;
		}

		// =======required parameters=======//
		String inDsm = null;
		String log = null;
		String log_type = "";
		Date start = null;
		Date end = null;
		String output = null;

		// =======optional parameters=======//
		// ArrayList<String> language = new ArrayList<String>();

		// String[] exts = {".java"};
		// ArrayList<String> exts = new ArrayList<String>();

		final SimpleOptionsParser opts = new SimpleOptionsParser();
		for (Option o : info.getOptions().values())
			opts.addOption(o);
		try {
			opts.parse(args);
			for (Argument arg : opts.getArguments()) {
				// switch(arg.getName()){
				if (arg.getName().equals("inDsm")) {
					inDsm = arg.getValue();
					// System.out.println("inDsm parameter is set\n");
				}

				if (arg.getName().equals("svn")) {
					log = arg.getValue();
					log_type = "svn";
				}
				if (arg.getName().equals("git")) {
					log = arg.getValue();
					log_type = "git";
				}
				if (arg.getName().equals("dmr")) {
					log = arg.getValue();
					log_type = "dmr";
				}

				if (arg.getName().equals("sDate"))
					start = FormatUtil.DateFormat(arg.getValue());

				if (arg.getName().equals("eDate"))
					end = FormatUtil.DateFormat(arg.getValue());

				if (arg.getName().equals("o")) {
					output = arg.getValue();
					if (output != null && !output.endsWith(".dsm"))
						output = output + ".dsm";
				}

				// }
			}

		} catch (OptionException e) {
			printUsage(System.err);
			return;
		}

		if (log == null) {
			System.err.println("log missing");
			return;
		}

		if (output == null) {
			System.err.println("output missing");
			return;
		}
		if (inDsm == null) {
			System.err.println("inDsm missing");
			return;
		}

		/*
		 * LangInfo langinfo = new LangInfo("Language.config");
		 * 
		 * ArrayList<String> langs = langinfo.getKeyLang();
		 * 
		 * for(String lang : langs){ for(String lang2 : language){
		 * if(lang2.equals(lang)){ exts.addAll(langinfo.getSetLang(lang)); } } }
		 * 
		 * if(language.equals("c")){ exts = new String[3]; exts[0] = ".c";
		 * exts[1] = ".cpp"; exts[2] = ".h"; }
		 */

		GenHistoryDsm.genHDSM(log, log_type, inDsm, start, end, output);

	}

	static void printUsage(final PrintStream out) {
		info.printInformation(out);
		out.println();

		out.printf("Usage: " + "java -jar GenHdsm.jar \n" + "-inDsm value\n" + "-svn/-git value\n" + "-sDate value e.g. MM/dd/yyyy\n" + "-eDate value e.g. MM/dd/yyyy\n" + "-o value \n", Program.class.getName());
		out.println();
		out.println("where options are:");

		info.printOptions(out);
		out.println();
		out.println(info.getDescription());
	}

}
