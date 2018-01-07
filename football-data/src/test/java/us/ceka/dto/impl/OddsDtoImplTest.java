package us.ceka.dto.impl;



import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import us.ceka.dto.OddsDto;
import us.ceka.odds.config.AppConfig;
import us.ceka.util.JsoupTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class OddsDtoImplTest {
	
	protected final Logger log = LoggerFactory.getLogger(OddsDtoImplTest.class);
	
	@Autowired
	private OddsDto footballOddsDto;
	
	@Autowired
	private JsoupTemplate jsoupTemplate;
	
	@Test
	public void getArchiveByIdTest() {
		//footballOddsDto.getArchiveById("69", LocalDateTime.parse("20171217 00:00", DateTimeFormatter.ofPattern("yyyyMMdd HH:mm")));
		/*
		Document d = jsoupTemplate.getDocument("http://bet.hkjc.com/football/results/search_result.aspx?lang=CH&search=true&srchdate=1&fdate=20170104&tdate=20171104&srchteam=1&teamcode=69&pageno=2");
		Element elt = d.getElementById("ctl00_cm_rptMatches_ctl12_tdFullRes");
		if(elt != null) log.info("{}", elt.text());
		*/
		Document d = jsoupTemplate.getDocument("http://www.teamfeed.co.uk/injuries-suspensions/football/germany/bundesliga");
		List<Element>li = d.select("h1");
		for(Element elt : li) log.info("{}", elt.text());
		
		log.info("----------------------- end test--------------");
		assertTrue(true);
		
	}
}
