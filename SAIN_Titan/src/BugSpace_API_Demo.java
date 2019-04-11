import edu.drexel.cs.rise.DesignSpace.GenBuglist.GenBuglist;

public class BugSpace_API_Demo {

	public static void main(String[] args) {

		/* This component has two different APIs to reach the same goal */

		// The first one calculates a bug space from the original jira reports
		// in xml formats.

		// Note that sometimes you see exceptions due to
		// abnormal reports. But the program will skip these exceptions to
		// generate the final output.
		testAPI1();

		System.out.println("------------------------------------------------");

		// The second one calculates a bug space from a given list of bug IDs.
		// This option is more flexible to interface other bug tracking
		// databases. The key data is the list of bug IDs.
		testAPI2();

	}

	public static void testAPI1() {

		/*
		 * This API takes the following parameters: 1. The history revision log
		 * 2. The log type, either svn or git 3. The structural DSM 4. The start
		 * time-stamp to be included in the calculation, format is MM/dd/yyyy
		 * 5.The end time-stamp to be included in the calculation, format is
		 * MM/dd/yyyy 6. The regular expression of the issue key IDs, e.g.
		 * AVRO-[0-9]*; a project may have multiple keys, thus this is an array
		 * 7. The path to the jira issue reports 8. The output path
		 */

		String inLog = "data/SAIN/hive/input/hive_git.log";
		String logType = "git";
		String inDsm = "data/SAIN/hive/hive-2.0.0.dsm";
		String start = "1/1/2000";
		String end = "1/1/2019";
		String[] keys = { "HIVE-[0-9]*" };
		String jiraReports = "data/SAIN/hive/input/hive-jira-bugs";
		String output = "data/SAIN/hive/hive-buglist-API1.csv";

		GenBuglist.setBugFreq(5); // You can change the default threshold,
									// defaul is 1

		GenBuglist.genBugSpaceFromJiraReports(inLog, logType, inDsm, start, end, keys, jiraReports, output);

	}

	public static void testAPI2() {

		/*
		 * This API takes the following APIs 1. The history revision log 2. The
		 * log type, either svn or git 3. The structural DSM 4. The start
		 * time-stamp to be included in the calculation, format is MM/dd/yyyy 5.
		 * The end time-stamp to be included in the calculation, format is
		 * MM/dd/yyyy 6. The regular expression of the issue key IDs, e.g.
		 * AVRO-[0-9]*; a project may have multiple keys, thus this is an array
		 * 7. The path to the list of issue IDs 8. The output path
		 */

		String inLog = "data/SAIN/hive/input/hive_git.log";
		String logType = "git";
		String inDsm = "data/SAIN/hive/hive-2.0.0.dsm";
		String start = "1/1/2000";
		String end = "1/1/2019";
		String[] keys = { "HIVE-[0-9]*" };
		String issueIDs = "data/SAIN/hive/input/hive-bug-id-list.csv";
		String output = "data/SAIN/hive/hive-buglist-API2.csv";

		GenBuglist.setBugFreq(3); // You can change the default threshold,
		// defaul is 1
		GenBuglist.genBugSpaceFromBugIDList(inLog, logType, inDsm, start, end, keys, issueIDs, output);

	}

}
