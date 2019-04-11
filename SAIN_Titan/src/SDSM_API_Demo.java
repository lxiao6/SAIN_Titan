import edu.drexel.cs.rise.DesignSpace.GenSdsm.GenStructureDsm;

public class SDSM_API_Demo {

	public static void main(String[] args) {

		// The first parameter is the cytoscape input
		// The second parameter is the prefix
		// The third parameter is the xprefix
		// The fourth parameter is the output path
		String cytoscapePath = "data/SAIN/hive/input/hive-release-2.0.0-cytoscape.xml";
		String prefix = "org\\apache\\";
		String xprefix = "";
		String output = "data/SAIN/hive/hive-2.0.0.dsm";

		GenStructureDsm.generateSDSM(cytoscapePath, prefix, xprefix, output);

	}

}
