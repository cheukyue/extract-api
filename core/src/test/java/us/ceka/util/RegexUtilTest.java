package us.ceka.util;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegexUtilTest {
	
	protected final Logger log = LoggerFactory.getLogger(RegexUtilTest.class);
	
	@Test
	public void findFirstSingleGroupTest() {
		/*
		String str = "阿根廷-厄瓜多爾 (Argentina-Ecuador)";
		Pattern p = Pattern.compile("(\\S+) \\(.*\\)");
		Matcher m = p.matcher(str);
		
		System.out.println(m.groupCount());
		while(m.find()) for(int i = 0, n = m.groupCount(); i <= n; i++) System.out.println(i + " " + m.group(i));
		*/
		//String str = "lang=CH&tmatchid=105076&tdate=14-08-2016&tday=SAT&tnum=61";	
		//Pattern p = Pattern.compile("matchid=(\\d+)&tdate=(\\d{2}\\-\\d{2}\\-\\d{4})&tday=(\\w{3})");
		
		log.info("executing findFirstSingleGroupTest...");
		String id = RegexUtil.findFirstSingleGroup("tmatchid=(\\d+)", "?lang=CH&tmatchid=110100");
		assertEquals("110100", id);
	}
	
	@Test
	public void findFirstSingleGroupListTest() {
		log.info("executing findFirstSingleGroupListTest...");
		List<String> l = RegexUtil.findSingleGroupList("^(\\d+)\\/(\\d+) (\\S+) (\\d+) (.+)", "18/12 星期日 2 CF 阿美利加 對 國民體育會");
		//for(String str : l) log.info("{}", str);
		assertEquals("18", l.get(0));
		assertEquals("12", l.get(1));
		assertEquals("星期日", l.get(2));
		assertEquals("2", l.get(3));
		assertEquals("CF 阿美利加 對 國民體育會", l.get(4));
	}
}
