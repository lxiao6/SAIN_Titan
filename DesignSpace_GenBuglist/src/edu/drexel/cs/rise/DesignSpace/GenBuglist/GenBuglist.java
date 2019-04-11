package edu.drexel.cs.rise.DesignSpace.GenBuglist;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

import edu.drexel.cs.athena.data.changed_file;
import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.rise.DesignSpace.Utilities.FormatUtil;
import edu.drexel.cs.rise.DesignSpace.Utilities.InputUtil;
import edu.drexel.cs.rise.DesignSpace.Utilities.OutputUtil;
import edu.drexel.cs.rise.DesignSpace.Utilities.PathUtil;
import edu.drexel.cs.rise.DesignSpace.data.item;
import edu.drexel.cs.rise.util.WeightedDigraph;

public class GenBuglist {

	static int bugFreq = 1;

	private static void genBuglistCSV(String inLog, String logType, String inDsm, Date start, Date end, String[] keys, Set<String> issueIDs, String output) {

		Set<commit> history = new HashSet<>();

		if (logType.equals("svn"))
			history.addAll(InputUtil.loadFromSvn(inLog));
		if (logType.equals("git")) {
			history.addAll(InputUtil.loadFromGit(inLog));
		}

		System.out.println("Load revision history from " + logType + " log: " + inLog + "\n" + " total number of commits: " + history.size());

		WeightedDigraph<String> dsm = InputUtil.loadSdsm(inDsm);
		System.out.println("Load dsm from file: " + inDsm + "\n" + dsm.vertices().size() + " elements are loaded");

		Vector<item> bugs = GenBuglist.extractBugList(dsm, history, start, end, 1, keys, issueIDs);
		System.out.println("Generate bug list of all buggy files, list size: " + bugs.size());

		Vector<item> bugsByThred = GenBuglist.getBugsby(bugs, bugFreq);
		System.out.println("Size of bug list with threshold: " + bugFreq + " is: " + bugsByThred.size());

		try {
			OutputUtil.write_buglist(output, bugsByThred);
			System.out.println("Write bug list to output file: " + output);
		} catch (Exception e) {
			System.out.println("Error in writing to bug list: " + output);
			return;
		}
	}

	public static void genBugSpaceFromBugIDList(String inLog, String logType, String inDsm, String start, String end, String[] keys, String BugIDs, String output) {

		Set<String> bug_keys = null;

		if (BugIDs != null) {
			bug_keys = InputUtil.loadBugTicketList(BugIDs);
			System.out.println("Load jira bug tickets from: " + BugIDs + "\n" + bug_keys.size() + " bug tickets are loaded");
		}

		genBuglistCSV(inLog, logType, inDsm, FormatUtil.DateFormat(start), FormatUtil.DateFormat(end), keys, bug_keys, output);

	}

	public static void genBugSpaceFromJiraReports(String inLog, String logType, String inDsm, String start, String end, String[] keys, String jiraReports, String output) {

		Set<String> bug_keys = null;
		if (jiraReports != null) {
			bug_keys = InputUtil.loadBugTickets(jiraReports);
			System.out.println("Load jira bug tickets from: " + jiraReports + "\n" + bug_keys.size() + " bug tickets are loaded");
		}

		genBuglistCSV(inLog, logType, inDsm, FormatUtil.DateFormat(start), FormatUtil.DateFormat(end), keys, bug_keys, output);

	}

	private static Vector<item> getBugsby(Vector<item> bugs, int thred) {
		Vector<item> ret = new Vector<>();
		for (item i : bugs) {
			if (i.getBug_freq() < thred) {
				return ret;
			}
			ret.add(i);
		}
		return ret;
	}

	private static Vector<item> extractBugList(WeightedDigraph<String> sdsm, Set<commit> change_history, Date start, Date end, int bug_freq_threshold, String[] keys, Set<String> bug_issues) {
		if (sdsm == null || change_history == null) {
			System.err.println("sdsm or history is null");
			return null;
		}

		Map<String, item> bug_freq = new HashMap<>();

		for (commit c : change_history) {
			if (dateValid(c, start, end)) {

				String msg = c.msg;
				if ((keys == null) || (!FormatUtil.extractIssueKey(msg, keys, bug_issues).equals("not found"))) {
					// System.out.println("debug info: in util.extractBugList
					// find key in msg: "+msg);
					Set<changed_file> changed_files = c.changed_files;
					for (changed_file f : changed_files) {
						String cp = f.getPath();
						String fp = PathUtil.processPath(sdsm.vertices(), cp);// FormatUtil.processPath(sdsm.vertices(),cp);

						if (fp != null) {

							if (bug_freq.containsKey(fp)) {
								item find_item = bug_freq.get(fp);
								int count = find_item.getBug_freq() + 1;
								find_item.setBug_freq(count);
							} else {
								item new_item = new item(fp);
								new_item.setBug_freq(1);
								bug_freq.put(fp, new_item);
							}
						}
					}
				}

			}

		}

		final Queue<item> bugItems = new PriorityQueue<>(sdsm.vertices().size(), new Comparator<item>() {
			@Override
			public int compare(final item a, final item b) {
				return b.getBug_freq() - a.getBug_freq();
			}
		});
		bugItems.addAll(bug_freq.values());

		int rank = 1;
		int count = 1;
		int previous_freq = 0;
		Vector<item> bug_list_rank = new Vector<>();
		while (!bugItems.isEmpty()) {
			item current = bugItems.poll();
			if (current.getBug_freq() >= bug_freq_threshold && sdsm.containsVertex(current.getName())) {
				if (previous_freq == current.getBug_freq()) {
					current.setBug_rank(rank);
				} else {
					current.setBug_rank(count);
					rank = count;
				}

				bug_list_rank.add(current);
				count++;
				previous_freq = current.getBug_freq();
			}
		}

		return bug_list_rank;
	}

	private static boolean dateValid(commit c, Date start, Date end) {
		if (start == null && end == null)
			return true;

		if (c.date != null) {
			Date rdate = FormatUtil.DateFormat(c.date);
			return (rdate.before(end) && rdate.after(start));
		}
		return true;

	}

	public static void setBugFreq(int freq) {

		bugFreq = freq;

	}
}
