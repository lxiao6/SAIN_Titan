package edu.drexel.cs.athena.download;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import edu.drexel.cs.athena.util.dateUtil;

public class downloadJiraBugIssuesWeekly
{

    /**
     * @param args
     */
    static String out_dir;
    static String url_root = "https://issues.apache.org/jira/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?";
    static String tempMax = "100";

    public static void download(String project, String start, String end, String odir)
    {

        out_dir = odir;
        // url_root = urlRoot;
        // tempMax = max;
        Date sdate = dateUtil.DateFormat(start);
        Date edate = dateUtil.DateFormat(end);
        Date next = dateUtil.nextWeek(sdate);

        while (next.before(edate)) {

            // System.out.println(sdate+"->"+next);
            download(project, sdate, next);
            sdate = next;
            next = dateUtil.nextWeek(sdate);
        }
        // System.out.println(start+"->"+end);
        download(project, sdate, edate);
    }

    private static String formDate(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTime());
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        if (day > 28) {
            day = 28;
        }
        return year + "-" + month + "-" + day;
    }

    private static String jqlQuery(String project, String start, String end)
    {
        // jqlQuery=project+%3D+HADOOP+AND+resolution+%3D+Unresolved+ORDER+BY+priority+DESC%2C+updated+DESC&tempMax=1000
        // https://issues.apache.org/jira/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?jqlQuery=project+%3D+CAMEL&tempMax=100
        return "jqlQuery=project+%3D+" + project + "+AND+created+%3E%3D+" + start
                + "+AND+created+%3C%3D+" + end + "+ORDER+BY+key+ASC&tempMax=" + tempMax;
    }

    public static void download(String project, Date start, Date end)
    {

        String s = formDate(start);
        String e = formDate(end);
        System.out.println(s + "->" + e);
        String jqlQuery = jqlQuery(project, s, e);
        String address = url_root + jqlQuery;

        try {

            URL url = new URL(address);
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

            File dir = new File(out_dir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File output = new File(dir, project + "_" + s + "_" + e + ".xml");

            FileWriter fw = new FileWriter(output);
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                fw.write(inputLine + "\n");
            // System.out.println(inputLine);
            in.close();
            fw.flush();
            fw.close();

        }
        catch (MalformedURLException ex1) {
            ex1.printStackTrace();
        }
        catch (IOException ex2) {
            ex2.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

        if (args.length < 4) {

            System.err
                    .println("4 parameters:\n"
                            + "project name e.g. HADOOP\n"
                            + "start date e.g. 2006-6-1\n"
                            + "end date\n"
                            + "out put directory\n"
                            + "Please refer to this url to form input parameters: https://issues.apache.org/jira/sr/jira.issueviews:searchrequest-excel-current-fields/temp/SearchRequest.xls?jqlQuery=project+%3D+HADOOP+AND+issuetype+%3D+Bug+AND+created+%3E%3D+2006-01-01+AND+created+%3C%3D+2009-12-31+ORDER+BY+key+ASC&tempMax=100\n");
            return;
        }

        download(args[0], args[1], args[2], args[3]);
        Toolkit.getDefaultToolkit().beep();

    }

}
