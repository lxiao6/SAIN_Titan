package edu.drexel.cs.athena.parser.revisionHistory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.drexel.cs.athena.data.changed_file;
import edu.drexel.cs.athena.data.commit;
import edu.drexel.cs.athena.parser.inputParser;

public class mercurialPlainParser implements inputParser
{

    private commit per_commit = new commit();
    private HashSet<commit> history;
    private InputStream is;

    // private File is;

    public static enum TagType
    {

        changeset, branch, user, date, summary, end, parent, path, tag

    }

    // @Override
    public void initParser(String[] args)
    {
        // TODO Auto-generated method stub

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
        String content = null;
        Scanner scan = new Scanner(this.is);
        // BufferedReader br = new BufferedReader(new FileReader(this.is));

        while (scan.hasNext()) {
            line = scan.nextLine();
            // System.out.println(line);
            TagType tag;
            if (!line.isEmpty()) {

                tag = category(line);
                switch (tag) {

                case changeset:
                    content = getcontent(line);
                    per_commit = new commit(content);
                    break;
                case branch:
                    content = getcontent(line);
                    break;
                case user:
                    content = getcontent(line);
                    per_commit.author = content;
                    break;
                case date:
                    content = getcontent(line);
                    per_commit.date = content;
                    break;
                case summary:
                    content = getcontent(line);
                    per_commit.msg = content;
                    break;
                case parent:
                    content = getcontent(line);
                    break;
                case path:
                    content = getcontent(line);
                    content = content.substring(0, content.indexOf('|')).trim();
                    per_commit.changed_files.add(new changed_file(content));
                    break;
                case tag:
                    content = getcontent(line);
                    per_commit.tag = content;
                    break;
                case end:
                    content = getcontent(line);
                    // System.out.println(content);
                    String[] key = new String[2];
                    key[0] = "[0-9]+ insertions(\\+)";
                    key[1] = "[0-9]+ deletions(-)";
                    for (String k : key) {
                        Pattern p = Pattern.compile(k);
                        Matcher m = p.matcher(content);
                        if (m.find()) {
                            System.out.println("find " + k);
                            String v = m.group(0);
                            Scanner tps = new Scanner(v);
                            // per_commit.churn += tps.nextInt();
                            tps.close();
                        }
                    }
                    // System.out.println(per_commit.churn);
                    history.add(per_commit);
                    break;

                default:
                    System.out.println(tag + "---Sorry, I don't know what is this");
                }
                // System.out.println(content);
            }
        }
        scan.close();

    }

    private static String getcontent(String line)
    {
        return line.substring(line.indexOf(":") + 1).trim();
    }

    private static TagType category(String line)
    {

        TagType category = TagType.path;

        if (line.startsWith("changeset:"))
            return TagType.changeset;

        if (line.startsWith("branch:"))
            return TagType.branch;

        if (line.startsWith("user:"))
            return TagType.user;

        if (line.startsWith("date:"))
            return TagType.date;

        if (line.startsWith("summary:"))
            return TagType.summary;

        if (line.startsWith("tag:"))
            return TagType.tag;

        if (line.startsWith("parent:"))
            return TagType.parent;

        Pattern lastline = Pattern.compile("\\s[0-9]+\\s");
        String[] result = lastline.split(line);
        if (result.length > 1
                && result[1].equalsIgnoreCase("files changed,")
                && (result[result.length - 1].equalsIgnoreCase("insertions(+)") || result[result.length - 1]
                        .equalsIgnoreCase("deletions(-)"))) {
            return TagType.end;
        }
        ;
        return category;
    }

    public HashSet<commit> getHistory()
    {
        return history;
    }

}
