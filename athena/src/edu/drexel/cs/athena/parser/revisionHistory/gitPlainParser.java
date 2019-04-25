package edu.drexel.cs.athena.parser.revisionHistory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;

import edu.drexel.cs.athena.data.changed_file;
import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.athena.parser.inputParser;

public class gitPlainParser implements inputParser
{

    private commit per_commit;
    private String commit_msg = "";
    private HashSet<commit> history;
    private InputStream is;

    private static String[] path_regex = { "(([0-9]++)|-)(\\s)*(([0-9]++)|-)(\\s)*([a-zA-Z0-9-_.\\s]*/)*[a-zA-Z0-9-\\s.]+?.[a-zA-Z]+?$" };

    // private File is;

    public static enum TagType
    {

        commitid, author, date, msg, path, end

    }

    // @Override
    public void initParser(String[] args)
    {

    }

    // @Override
    public void process(String args) throws FileNotFoundException
    {
        history = new HashSet<commit>();
        File input = new File(args);
        if (input.isDirectory()) {
            for (File f : input.listFiles()) {
                this.is = new FileInputStream(f);
                parse();
            }
        }
        else {
            this.is = new FileInputStream(input);
            parse();
        }

    }

    // @Override
    public void parse()
    {

        String line = null;
        Scanner scan = new Scanner(this.is);

        while (scan.hasNext()) {
            line = scan.nextLine();
            TagType tag;
            if (!line.isEmpty()) {

                tag = category(line);
                switch (tag) {

                case commitid:
                    if (per_commit != null) {
                        per_commit.msg = commit_msg;
                        history.add(per_commit);
                        commit_msg = "";
                    }
                    per_commit = new commit();
                    per_commit.revision = line.replace("commit", "").trim();
                    // System.out.println(line);
                    break;
                case author:
                    per_commit.author = line.replace("Author:", "").trim();
                    // System.out.println(line);
                    break;
                case date:
                    per_commit.date = line.replace("Date:", "").trim();
                    // System.out.println(line);
                    break;
                case msg:
                    commit_msg = commit_msg.concat(line);
                    // System.out.println(line);
                    break;
                case path:
                    /*
                     * line = line.trim();
                     * String path = line.substring(0, line.indexOf('|')).trim();
                     * String churn = line.substring(line.indexOf('|') + 1).trim();
                     * if (Character.isDigit(churn.charAt(0))) {
                     * // System.out.println(churn);
                     * churn = churn.replaceAll("\\D+", "");// remove characters
                     * // System.out.println(churn);
                     * }
                     * else {
                     * churn = "0";
                     * }
                     * 
                     * changed_file cf = new changed_file(path, churn);
                     * // System.out.println(cf.getPath() + " " + cf.getChurn());
                     * per_commit.changed_files.add(cf);
                     */

                    line = line.trim();
                    changed_file changeTofile = instansiateChangePath(line);
                    // per_commit.addChangedFile(changeTofile);
                    per_commit.changed_files.add(changeTofile);

                    break;
                default:
                    System.out.println(tag + "---Sorry, I don't know what is this");
                }
                // System.out.println(content);
            }
        }
        scan.close();

    }

    private static TagType category(String line)
    {

        TagType category = TagType.msg;

        if (line.startsWith("commit "))
            return TagType.commitid;

        if (line.startsWith("Author:"))
            return TagType.author;

        if (line.startsWith("Date:"))
            return TagType.date;

        if (match(line))
            return TagType.path;

        return category;
    }

    public HashSet<commit> getHistory()
    {
        return history;
    }

    private static boolean match(String line)
    {
        boolean isPath = false;

        for (String rgx : path_regex) {
            if (line.matches(rgx)) {
                return true;
            }
        }
        return isPath;
    }

    private changed_file instansiateChangePath(String pathLineInLog)
    {

        changed_file returnValue = new changed_file();

        Scanner StringScanner = new Scanner(pathLineInLog);

        int linesAdded = 0;
        String value1 = StringScanner.next();
        if (!value1.equals("-"))
            linesAdded = Integer.parseInt(value1);

        int linesRemoved = 0;
        String value2 = StringScanner.next();
        if (!value2.equals("-"))
            linesRemoved = Integer.parseInt(value2);

        String fileName = StringScanner.next();

        returnValue.setPath(fileName);
        returnValue.setChurn(Integer.toString((linesAdded + linesRemoved)));

        StringScanner.close();

        return returnValue;

    }

}
