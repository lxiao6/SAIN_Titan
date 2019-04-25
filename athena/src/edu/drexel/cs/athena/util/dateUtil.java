package edu.drexel.cs.athena.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class dateUtil {
	
	public static Date DateFormat(String date){
		
		DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		DateFormat df3 = new SimpleDateFormat("MM/dd/yyyy kk:mm");
		DateFormat df4 = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat df5 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z"); //Sun Sep 8 13:02:43 2013 +0200
		
		try {
			return df1.parse(date);
		} catch (ParseException e1) {
			try{
				return df2.parse(date);
			}catch(ParseException e2){
				try{
					return df3.parse(date);
				}catch(ParseException e3){
					try{
						return df4.parse(date);
					}catch(ParseException e4){
						try{
							return df5.parse(date);
						}catch(ParseException e5){
							System.err.println("Error in parsing time: "+date+" in edu.drexe.cs.rise.DesignSpace.util.DateFormat");
						}
					
					}
				}
			}
		}
		return null;
}

	public static Date nextWeek(Date date) {
		long orig = date.getTime();
		long day = (1000 * 60 * 60 * 24); 
		long diff = (7*day);
		Date time = new Date(orig+diff); 
		return time;
	}
	
	

}
