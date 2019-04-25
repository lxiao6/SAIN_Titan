package edu.drexel.cs.athena;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.drexel.cs.athena.parser.issueHistory.jiraXmlParser;

public class testIssue {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			jiraXmlParser myParser = new jiraXmlParser();
			myParser.process("CONN-2014-09.xml");
			System.out.println(myParser.all_issues.size());
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

}
