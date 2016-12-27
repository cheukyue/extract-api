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
	
}
