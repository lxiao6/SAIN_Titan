package edu.drexel.cs.athena;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.athena.parser.moka.mokaParser;
import edu.drexel.cs.athena.parser.revisionHistory.mercurialPlainParser;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//getHistory();
		
		try {
			mokaParser myParser = new mokaParser("org.apache.avro");
			myParser.process("../data/avro/moka/avro-1.3.0.moka");
			
			int n1 = myParser.dpTypes.size();
			int n2 = myParser.nodes.size();
			int n3 = myParser.edges.size();
			
			System.out.println(n1+" "+n2+" "+n3);
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
	
	public static HashSet<commit> getHistory(){
		mercurialPlainParser myParser;
		try {
			myParser = new mercurialPlainParser();

			myParser.process("C:/Users/lu/My Research/siemens/mercurial-log/Alllog.txt");
			System.out.println(myParser.getHistory().size());
			return myParser.getHistory();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
