package us.ceka.dto.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import us.ceka.domain.Odds;
import us.ceka.dto.OddsDto;
import us.ceka.util.RegexUtil;

@Repository("oddsDto")
public class OddsDtoImpl extends BaseDtoJsoupImpl<Odds> implements OddsDto{
	public Odds getLatestById(String matchId) {
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.match.odd", matchId);
		Element titleNode = doc.select("a[title^=對賽往績]").first();
		List<String> teams = RegexUtil.findSingleGroupList("(.+)\\(主\\) 對 (.+)\\(客\\)", titleNode.text()); 
		log.info("{}", teams);

		//check if the match is already kicked off
		boolean kickoff = doc.getElementsByClass("nopoolmsg").size() > 0;
		if(kickoff) {
			log.info("Match {} is already kicked off", matchId);
			return null;
		}

		Element homeRate = doc.getElementById(String.format("%s_HAD_H", matchId));
		Element drawRate = doc.getElementById(String.format("%s_HAD_D", matchId));
		Element awayRate = doc.getElementById(String.format("%s_HAD_A", matchId));
		Element handicapLine = doc.select(String.format("#%s_HDC_HG label.lblGoal", matchId)).first();
		Element handicapHomeRate = doc.getElementById(String.format("%s_HDC_H", matchId));
		Element handicapAwayRate = doc.getElementById(String.format("%s_HDC_A", matchId));

		Map<String, Object> oddsPropMap = new HashMap<String, Object>();
		oddsPropMap.put("homeTeam", teams.get(0));
		oddsPropMap.put("awayTeam", teams.get(1));
		oddsPropMap.put("homeRate", homeRate == null ? -1 : homeRate.text());
		oddsPropMap.put("drawRate", drawRate == null ? -1 : drawRate.text());
		oddsPropMap.put("awayRate", awayRate == null ? -1 : awayRate.text());
		oddsPropMap.put("handicapLine", handicapLine == null ? null : handicapLine.text());
		oddsPropMap.put("handicapHomeRate", handicapHomeRate == null ? -1 : handicapHomeRate.text());
		oddsPropMap.put("handicapAwayRate", handicapAwayRate == null ? -1 : handicapAwayRate.text());
		oddsPropMap.put("dateCreated", LocalDateTime.now());

		Odds odds = new Odds(matchId, null);
		try {
			BeanUtils.populate(odds, oddsPropMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("Error on populating value into FootballMatch bean", e);
		}

		log.info("Latest odds: {}", odds);
		return odds;
	}

	public Odds getArchiveById(String matchId) {
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.archive.odd", matchId);
		Element titleNode = doc.select("#footballmaincontent .LastOddsHeader").first();
		List<String> regexMatches = RegexUtil.findSingleGroupList("球賽編號:\\W+星期(.+?) (\\d+) (.+) \\(主\\) 對 (.+)\\(客\\)", titleNode.text());
		log.info("Matches over title node{}", regexMatches);

		Elements rateNode = doc.select("#footballcontentcontainer table[poolkey=HAD] tr:nth-child(3) td"); //1 => toggle row, 2 => header
		Elements handicapNode = doc.select("#footballcontentcontainer table[poolkey=HDC] tr:nth-child(3) td"); //1 => toggle row, 2 => header
		String handicapLine = "-";
		if(handicapNode.size() > 1) {
			log.info("Handicap line: {}", handicapNode.get(0).text());
			handicapLine = RegexUtil.findFirstSingleGroup(".+?\\[(.+?)\\]", handicapNode.get(0).text());
		}

		Map<String, Object> oddsPropMap = new HashMap<String, Object>();
		oddsPropMap.put("type", Odds.TYPE.ARCHIVE.getCode());
		oddsPropMap.put("homeTeam", regexMatches.get(2));
		oddsPropMap.put("awayTeam", regexMatches.get(3));
		oddsPropMap.put("homeRate", rateNode == null ? 0 : rateNode.get(0).text());
		oddsPropMap.put("drawRate", rateNode == null ? 0 : rateNode.get(1).text());
		oddsPropMap.put("awayRate", rateNode == null ? 0 : rateNode.get(2).text());
		oddsPropMap.put("handicapHomeRate", handicapNode.size() < 1 ? 0 : handicapNode.get(1).text());
		oddsPropMap.put("handicapAwayRate", handicapNode.size() < 1 ? 0 : handicapNode.get(2).text());
		oddsPropMap.put("handicapLine", handicapLine);
		oddsPropMap.put("dateCreated", LocalDateTime.now());

		Odds odds = new Odds(matchId, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyymmdd-HHmmss")));
		try {
			BeanUtils.populate(odds, oddsPropMap);
		} catch (IllegalAccessException | InvocationTargetException e) {
			log.error("Error on populating value into FootballMatch bean", e);
		}
		
		log.info("Archive odds: {}", odds);

		return odds;
	}
}
