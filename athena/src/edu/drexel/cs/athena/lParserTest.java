package edu.drexel.cs.athena;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import edu.drexel.cs.athena.data.changed_file;
import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.athena.data.issue;
import edu.drexel.cs.athena.parser.inputParser;
import edu.drexel.cs.athena.parser.issueHistory.jiraXmlParser;
import edu.drexel.cs.athena.parser.revisionHistory.mercurialPlainParser;
import edu.drexel.cs.athena.parser.revisionHistory.svnXmlParser;

public class lParserTest
{

    /**
     * @param args
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static void main(String[] args) throws ParserConfigurationException, SAXException,
            IOException
    {

        if (args.length < 2) {
            System.err.println("para1: input_type\n" + "para2: input_path\n");
            return;
        }
        String input_type = args[0];
        String input_path = args[1];

        inputParser myParser = null;

        /*
         * switch(input_type){
         * case "svn":
         * myParser = new svnXmlParser();
         * myParser.process(input_path);
         * break;
         * case "jira":
         * myParser = new jiraXmlParser();
         * myParser.process(input_path);
         * break;
         * case "und-f":
         * myParser = new fileDependencyCytoscapeParser();
         * myParser.process(input_path);
         * break;
         * case "svn-s":
         * myParser = new svnSummaryParser();
         * myParser.process(input_path);
         * case "mercurial":
         * myParser = new mercurialPlainParser();
         * myParser.process(input_path);
         * default:
         * break;
         * }
         */

        if (myParser instanceof svnXmlParser)
            write_svn(((svnXmlParser) myParser).history);
        if (myParser instanceof jiraXmlParser)
            write_jira(((jiraXmlParser) myParser).all_issues);
        /*
         * if(myParser instanceof svnSummaryParser )
         * write_svn_s(((svnSummaryParser)myParser).history);
         */
        if (myParser instanceof mercurialPlainParser) {
            write_m_rvs(((mercurialPlainParser) myParser).getHistory());
        }

    }

    /*
     * private static void write_svn_s(Set<commit> history) {
     * Map<String, Integer> ticket_churn = new HashMap<String, Integer>();
     * int total_churn = 0;
     * for(commit c:history){
     * 
     * String churn = c.churn;
     * Scanner sc = new Scanner(churn);
     * int ch = 0;
     * ch = sc.nextInt();
     * total_churn += ch;
     * sc.close();
     * 
     * String msg = c.msg;
     * String key = "DTS"+"[0-9]+";
     * 
     * Pattern p = Pattern.compile(key);
     * Matcher m = p.matcher(msg);
     * String fkey;
     * if(m.find()){
     * fkey = m.group(0);
     * //System.out.println(fkey);
     * if(ticket_churn.containsKey(fkey)){
     * int cc = ticket_churn.get(fkey)+ch;
     * ticket_churn.remove(fkey);
     * ticket_churn.put(fkey, cc);
     * }else{
     * ticket_churn.put(fkey, ch);
     * }
     * }
     * 
     * 
     * }
     * 
     * 
     * try {
     * FileWriter fw = new FileWriter("churn.csv");
     * fw.write(total_churn+"\n");
     * for(String s:ticket_churn.keySet()){
     * fw.write(s+","+ticket_churn.get(s)+"\n");
     * }
     * fw.flush();
     * fw.close();
     * } catch (IOException e) {
     * e.printStackTrace();
     * }
     * 
     * 
     * }
     */

    private static void write_m_rvs(HashSet<commit> history)
    {

        for (commit c : history) {
            System.out.print(c.revision);
            System.out.print(" " + c.author);
            System.out.print(" " + c.date);
            System.out.print(" " + c.msg);
            // System.out.print(" " + c.churn);
            for (changed_file p : c.changed_files) {
                System.out.println(p.getPath());
            }
        }

    }

    private static void write_jira(Set<issue> history)
    {
        try {
            int i = 0;
            FileWriter fw = new FileWriter(new File("out.txt"));

            for (issue c : history) {
                i++;
                fw.write("===============================================\n");
                fw.write("key: " + c.getKey() + "\n" + "priority: " + c.getPriority() + "\n"
                        + "type: " + c.getType() + "\n" + "status: " + c.getStatus() + "\n"
                        + "summary: " + c.getSummary() + "\n");

            }
            fw.flush();
            fw.close();
            System.out.println(i);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void write_svn(Set<commit> history)
    {

        try {
            FileWriter fw = new FileWriter(new File("out.txt"));

            for (commit c : history) {
                fw.write("===============================================\n");
                fw.write("revision: " + c.revision + "\n" + "author: " + c.author + "\n" + "date: "
                        + c.date + "\n" + "msg: " + c.msg + "\n");
                for (changed_file f : c.changed_files) {
                    fw.write(f.getPath() + "\n");

                }
            }
            fw.flush();
            fw.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
