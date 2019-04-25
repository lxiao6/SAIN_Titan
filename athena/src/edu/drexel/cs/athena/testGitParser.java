package edu.drexel.cs.athena;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.drexel.cs.athena.data.changed_file;
import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.athena.parser.revisionHistory.gitPlainParser;
import edu.drexel.cs.rise.DesignSpace.Utilities.FormatUtil;
import edu.drexel.cs.rise.DesignSpace.Utilities.InputUtil;

public abstract class testGitParser
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Map<String, int[]> file_churn = new HashMap<String, int[]>();
        String[] keys = { "SPR-[0-9]*" };
        String jira_id_dir = "C:/Users/lx52/Dropbox/SoftServe-Refactor-Analyze/analsis-data/jira-type-change-lists/seperate-jira-issues/";
        Set<String> bug_issues = InputUtil.loadBugTicketList(jira_id_dir + "bug_id.csv");
        Set<String> story_issues = InputUtil.loadBugTicketList(jira_id_dir + "story_id.csv");
        Set<String> improvement_issues = InputUtil.loadBugTicketList(jira_id_dir
                + "Improvement_id.csv");
        Set<String> techinical_issues = InputUtil.loadBugTicketList(jira_id_dir
                + "techinical_task_id.csv");

        System.out.println(bug_issues.size() + " " + story_issues.size() + " "
                + improvement_issues.size() + " " + techinical_issues.size());

        gitPlainParser parser = new gitPlainParser();
        try {
            parser.process("C:/Users/lx52/Dropbox/SoftServe-Refactor-Analyze/original-data/commits_log.txt");
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        System.out.println(parser.getHistory().size());

        for (commit c : parser.getHistory()) {

            String msg = c.msg;

            boolean none_tag = true;
            int index = 0;
            if (!FormatUtil.extractIssueKey(msg, keys, improvement_issues).equals("not found")) {
                none_tag = false;
                index = 0;
            }
            if (!FormatUtil.extractIssueKey(msg, keys, bug_issues).equals("not found")) {
                none_tag = false;
                index = 1;
            }
            if (!FormatUtil.extractIssueKey(msg, keys, story_issues).equals("not found")) {
                none_tag = false;
                index = 2;
            }
            if (!FormatUtil.extractIssueKey(msg, keys, techinical_issues).equals("not found")) {
                none_tag = false;
                index = 3;
            }
            if (none_tag) {
                index = 4;
            }

            for (changed_file cf : c.changed_files) {
                String path = cf.getPath();
                if (file_churn.containsKey(path)) {
                    file_churn.get(path)[index] = file_churn.get(path)[index]
                            + Integer.parseInt(cf.getChurn());

                }
                else {
                    int[] churns = new int[5];
                    churns[index] = Integer.parseInt(cf.getChurn());
                    file_churn.put(path, churns);
                }
            }
        }

        try {
            FileWriter fw = new FileWriter(
                    new File(
                            "C:/Users/lx52/Dropbox/SoftServe-Refactor-Analyze/analsis-data/churn_per_file.csv"));
            fw.write("path, improvement, bug, story, techinical, non tag\n");
            for (String path : file_churn.keySet()) {
                String orig_path = path;
                path = path.replace('.', '_');
                path = path.replace("/", ".");
                fw.write(path + ",");
                for (int i : file_churn.get(orig_path)) {
                    fw.write(i + ",");
                }
                fw.write("\n");
            }
            fw.flush();
            fw.close();
        }
        catch (IOException ex) {
            // TODO Auto-generated catch block
            ex.printStackTrace();
        }

    }
}
