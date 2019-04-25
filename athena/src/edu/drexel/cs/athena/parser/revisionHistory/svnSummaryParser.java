package edu.drexel.cs.athena.parser.revisionHistory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.athena.parser.inputParser;

public class svnSummaryParser implements inputParser
{

    public Set<commit> history = new HashSet<commit>();
    Scanner fs;

    // @Override
    public void initParser(String[] args)
    {

    }

    // @Override
    public void process(String args) throws FileNotFoundException
    {

        File input = new File(args);

        if (input.isDirectory()) {
            for (File f : input.listFiles()) {
                fs = new Scanner(new FileInputStream(f));
                parse();
            }
        }
        else {
            fs = new Scanner(new FileInputStream(new File(args)));
            parse();
        }

    }

    // @Override
    public void parse()
    {
        String tag = null;
        String msg = "";
        String line;
        commit nextCommit = new commit();
        while (fs.hasNextLine()) {
            line = fs.nextLine();
            // System.out.println(line);

            if (line.equals("------------------------------------------------------------------------")) {

                nextCommit.msg = msg.trim();
                // System.out.println(msg);
                if (nextCommit.revision != null)
                    history.add(nextCommit);
                tag = "start";
                msg = "";
                nextCommit = new commit();
            }
            else {
                if (tag.equals("start")) {
                    String[] fields = line.split("\\|");
                    nextCommit.revision = fields[0].trim();
                    nextCommit.author = fields[1].trim();
                    nextCommit.date = fields[2].trim();
                    String ch = fields[3].trim();
                    int index = ch.indexOf(" ");
                    ch = ch.substring(0, index);
                    // nextCommit.churn = Integer.parseInt(ch);
                    // Scanner ts = new Scanner(fields[3]);
                    // nextCommit.churn = ts.nextInt();
                    // ts.close();

                    // System.out.println(nextCommit.revision);
                    // System.out.println(nextCommit.author);
                    // System.out.println(nextCommit.date);
                    // System.out.println(nextCommit.churn);
                    // System.out.println(nextCommit.msg);

                    tag = "msg";
                }
                else {
                    if (tag.equals("msg")) {

                        msg = msg.concat(line + "\n");
                    }
                }
            }
        }
    }

}
