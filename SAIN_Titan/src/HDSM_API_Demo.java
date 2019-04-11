import edu.drexel.cs.rise.DesignSpace.GenHdsm.GenHistoryDsm;

public class HDSM_API_Demo {

	public static void main(String[] args) {

		/*
		 * This AP takes 6 parameters 1. The revision log history 2. The type of
		 * the log, either git or svn, currently 3. The structure DSM 4. The
		 * start date of the history you want to include in the output, in the
		 * format of MM/dd/yyyy 5. The end date of the history you want to
		 * include in the output in the format of MM/dd/yyyy
		 */

		String historyLog = "data/SAIN/hive/input/hive_git.log";
		String logType = "git";
		String inDsm = "data/SAIN/hive/hive-2.0.0.dsm";
		String start = "1/1/2000";
		String end = "1/1/2015";
		String output = "data/SAIN/hive/hive-2.0.0-history.dsm";

		GenHistoryDsm.genHDSM(historyLog, logType, inDsm, start, end, output);

	}

}
