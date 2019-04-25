package edu.drexel.cs.athena.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


public class downloadJiraBugIssues {

	/**
	 * @param args
	 */
	static String out_dir;
	static String url_root;
	static String tempMax;
	
	public static void download(String project,String urlRoot, String max, String start, String end,String odir){
		
		
		out_dir = odir;
		url_root = urlRoot;
		tempMax = max;
		String next = nextMonth(start);
		
		while(before(next,end)){
			
			System.out.println(start+"->"+next);
			download(project, start, next);
			start = next;
			next = nextMonth(start);
		}
		System.out.println(start+"->"+end);
		download(project,start,end);
	}	
	
	private static boolean before(String date1, String date2) {
		
		String[] v1 = date1.split("-");
		String[] v2 = date2.split("-");
		
		int y1,y2,m1,m2,d1,d2;
		y1 = Integer.parseInt(v1[0]);
		y2 = Integer.parseInt(v2[0]);
		if(y1 < y2){
			return true;
		}
		if(y1 > y2){
			return false;
		}
		
		m1 = Integer.parseInt(v1[1]);
		m2 = Integer.parseInt(v2[1]);
		
		if(m1 < m2){
			return true;
		}
		if(m1 > m2){
			return false;
		}
		
		d1 = Integer.parseInt(v1[2]);
		d2 = Integer.parseInt(v2[2]);
		
		if(d1 < d2){
			return true;
		}
		if(d1 > d2){
			return false;
		}
		
		
		
		return true;
	}

	private static String nextMonth(String date) {
		
		String[] v = date.split("-");		
		return nextMonth(Integer.parseInt(v[0]),Integer.parseInt(v[1]),Integer.parseInt(v[2]));
	}

	private static String nextMonth(int year, int month, int day) {
		
		if(month == 12) return formDate(year+1,1,day);
		else return formDate(year,month+1,day);
	}

	private static String formDate(int year, int month, int day) {
		return Integer.toString(year)+"-"+Integer.toString(month)+"-"+Integer.toString(day);
	}
	
	

	private static String jqlQuery(String project, String start, String end) {
		
		return  "jqlQuery=project+%3D+"+project+"+AND+issuetype+%3D+Bug+AND+created+%3E%3D+"+start+"+AND+created+%3C%3D+"+end+"+ORDER+BY+key+ASC&tempMax="+tempMax;
	}

	public static void download(String project, String start, String end){
		
		String jqlQuery = jqlQuery(project,start,end);
		String address = url_root+jqlQuery;
		
		  try {
			  
				URL url = new URL(address);
				URLConnection yc = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
		                yc.getInputStream()));
				
				
				File dir = new File(out_dir);
				if(!dir.exists()){
					dir.mkdirs();
				}
				File output = new File(dir,project+"_"+start+"_"+end+".xml");
				
				FileWriter fw = new FileWriter(output);
				String inputLine;
		        while ((inputLine = in.readLine()) != null) 
		        	fw.write(inputLine+"\n");
		            //System.out.println(inputLine);
		        in.close();
		        fw.flush();
		        fw.close();
		        
			  } catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	

	public static void main(String[] args) {
		
		if(args.length < 6){
			
			System.err.println("java -jar DownloadBug.jar p1 p2 p3 p4 p5 p6\n" +
					"	p1: project name e.g. HADOOP\n"+ 
					"	p2: url_root e.g. https://issues.jboss.org/sr/jira.issueviews:searchrequest-xml/temp/SearchRequest.xml?\n"+
					"	p3: tempMax e.g. 100 or 1000 (usually 100)\n" +
					"	p4: start date e.g. 2006-6-1\n" +
					"	p5: end date\n" +
					"	p6: out put directory\n" +
					"	Please refer to this url to form input parameters: \n" +
					"	https://issues.apache.org/jira/sr/jira.issueviews:searchrequest-excel-current-fields/temp/SearchRequest.xls?jqlQuery=project+%3D+HADOOP+AND+issuetype+%3D+Bug+AND+created+%3E%3D+2006-01-01+AND+created+%3C%3D+2009-12-31+ORDER+BY+key+ASC&tempMax=100\n");
			return ;
		}
		
		download(args[0], args[1],args[2], args[3], args[4],args[5]);

	}

}
