package us.ceka.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	
	public static String findFirstSingleGroup(String pattern, String match) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(match);
		if(m.find()) return m.group(1); 
		return "";
	}
	
	public static List<String> findSingleGroupList(String pattern, String match) {
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(match);
		List<String> l = new ArrayList<String>();
		if(m.find()) {
			for(int i = 1, n = m.groupCount(); i <= n; i++) l.add(m.group(i)); 
		}
		return l;
	}
	
	
	public static void main( String[] args )
	{
		System.out.println(String.format("%02d xxx", "2"));
		
		
		//String str = "lang=CH&tmatchid=105076&tdate=14-08-2016&tday=SAT&tnum=61";	
		//Pattern p = Pattern.compile("matchid=(\\d+)&tdate=(\\d{2}\\-\\d{2}\\-\\d{4})&tday=(\\w{3})");
		
		//List<String> l = findSingleGroupList("(\\d+):(\\d+)\\((\\d+):(\\d+)\\)", "1:2(3:4)");
		List<String> l = findSingleGroupList("^(\\d+)\\/(\\d+) (\\S+) (\\d+) (.+)", "18/12 星期日 2 CF 阿美利加 對 國民體育會");
		for(String str : l) System.out.println(str);
		
		//System.out.println( findFirstSingleGroup("tmatchid=(\\d+)", "?lang=CH&tmatchid=110100") );
		
		
		/*
		String str = "阿根廷-厄瓜多爾 (Argentina-Ecuador)";
		Pattern p = Pattern.compile("(\\S+) \\(.*\\)");
		Matcher m = p.matcher(str);
		
		System.out.println(m.groupCount());
		while(m.find()) for(int i = 0, n = m.groupCount(); i <= n; i++) System.out.println(i + " " + m.group(i));
		*/
		
	}
	
}
