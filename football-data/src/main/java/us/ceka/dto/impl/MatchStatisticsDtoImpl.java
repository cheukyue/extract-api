package us.ceka.dto.impl;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Repository;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import us.ceka.domain.MatchStatistics;
import us.ceka.dto.MatchStatisticsDto;

@Repository("matchStatisticsDto")
public class MatchStatisticsDtoImpl extends BaseDtoJsoupImpl<MatchStatistics> implements MatchStatisticsDto{

	public MatchStatistics getMatchStat(String matchId) {
		
		//Document doc = getJsoupTemplate().getDocumnetByAlias("url.match.stat", matchId);
		Document doc = getJsoupTemplate().getDocument("http://int.soccerway.com/national/england/premier-league/20162017/regular-season");
		
		//if(log.isInfoEnabled()) log.info("payload: {}", doc);
		doc.select("#page_competition_1_block_competition_tables_9_block_competition_league_table_1_table td.team:nth-child(3) a").forEach( elt -> {
			log.info(elt.text());
		});
		 
/*
	    try {
	    	final WebClient webClient = new WebClient(BrowserVersion.FIREFOX_45);
			final HtmlPage page = webClient.getPage("https://www.whoscored.com/Matches/1080640/MatchReport");
			log.info("!!!!!!!!!!!!!!!!!!!!!!!!!{}", page.getBody().asXml());
		} catch (Exception e) {e.printStackTrace();}
		*/
		return null;
	}
}
