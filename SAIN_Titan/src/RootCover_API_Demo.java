import edu.drexel.cs.rise.DesignSpace.RootCover.RootCover;

public class RootCover_API_Demo {

	public static void main(String[] args) {

		/*
		 * This API takes the following parameters:
		 */

		String inSdsm = "data/SAIN/hive/hive-2.0.0.dsm";
		String inHdsm = "data/SAIN/hive/hive-2.0.0-history.dsm";
		String inBugSpace = "data/SAIN/hive/hive-buglist-API1.csv";
		String outDir = "data/SAIN/hive/hive-root";
		int bugFreq = 5;
		double percent = 0.7;

		RootCover.calculate(inSdsm, inHdsm, inBugSpace, outDir, bugFreq, percent);

		// You can also remove the last two parameters to use the defaul setting
		// of 1 and 0.8

	}

}
