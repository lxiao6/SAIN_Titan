package edu.drexel.cs.rise.DesignSpace.GenHdsm.util;

import java.util.Set;

public class LocalPathUtil {

	public static String processPath(Set<String> nodes, String cp) {

		int index = cp.lastIndexOf(".");
		if (index > 0) {

			cp = path2namespace(cp, index);

			return findpath(cp, nodes);
		}
		return null;
	}

	private static String path2namespace(String cp, int index) {
		String extension = cp.substring(index + 1);
		cp = cp.substring(0, index) + "_" + extension;

		cp = cp.replace("/", ".");
		cp = cp.replace("\\", ".");

		return cp;

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

}
