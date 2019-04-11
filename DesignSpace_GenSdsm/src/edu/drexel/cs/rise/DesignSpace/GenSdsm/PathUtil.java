package edu.drexel.cs.rise.DesignSpace.GenSdsm;

import java.util.Set;

public class PathUtil {

	public static String[] valid_extensions = { "java", "c", "h", "cpp", "py", "js" };

	public static String processPath(Set<String> nodes, String cp) {

		int index = cp.lastIndexOf(".");
		if (index > 0) {
			String extension = cp.substring(index + 1);
			cp = cp.substring(0, index) + "_" + extension;

			cp = cp.replace("/", ".");
			cp = cp.replace("\\", ".");

			return findpath(cp, nodes);
		}
		return null;
	}

	private static String findpath(String cp, Set<String> nodes) {

		for (String s : nodes) {

			if (s.contains("$")) {
				// System.out.println(s);
				s = s.substring(0, s.indexOf('$'));
				// System.out.println(s);
			}

			if (cp.endsWith(s) || s.endsWith(cp)) {
				return s;
			}
		}
		return null;
	}

	public static String processPath(String p, String[] excld, String[] incld) {

		// System.out.println(excld.length+" "+incld.length);
		// System.out.println(p);
		boolean pflag = false;
		boolean sflag = false;

		String rt = null;

		if (excld[0].isEmpty() && incld[0].isEmpty())
			pflag = true;

		if (!incld[0].isEmpty()) {

			for (String in : incld) {

				// System.out.println("Process include prefix: " + in);
				if (p.contains(in)) {

					int index = p.indexOf(in);
					p = p.substring(index);
					pflag = true;
				}
			}
		}

		// System.out.println("After match include prefix: " + p);

		if (!excld[0].isEmpty()) {
			for (String ex : excld) {
				if (p.contains(ex)) {

					int index = p.indexOf(ex);
					index = index + ex.length();
					p = p.substring(index);
					pflag = true;
				}
			}
		}

		/** Start: Remove this for class cytoscpage file **/

		if (validExt(p)) {

			String extension;

			int index = p.lastIndexOf(".");

			if (index > 0) {

				extension = p.substring(index + 1);
				p = p.substring(0, index) + "_" + extension;

			}

		} else
			return null;

		/**
		 * End: Remove this for class cytoscpage file *
		 */

		p = p.replace("\\", ".");
		p = p.replace("/", ".");

		while (p.startsWith(".")) {
			p = p.substring(1);
		}
		if (pflag)
			rt = p;
		return rt;
	}

	private static boolean validExt(String path) {

		for (String ext : valid_extensions) {

			if (path.endsWith("." + ext)) {

				return true;
			}

		}
		return false;
	}
}
