package us.ceka.dto.impl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import us.ceka.domain.League;
import us.ceka.domain.Match;
import us.ceka.domain.Odds;
import us.ceka.domain.Season;
import us.ceka.domain.Team;
import us.ceka.dto.MatchDto;
import us.ceka.util.DateTimeUtil;
import us.ceka.util.RegexUtil;

@Repository("matchDto")
public class MatchDtoImpl extends BaseDtoJsoupImpl<Match> implements MatchDto{

	private @Value("${footballodds.numPerPage}") int numberMatchesPerPage = 60;
	private @Value("${url.json.match.result}") String matchResultJsonURL;

	public List<Match> getLatestMatchesByLeauge(League league) {
		return getLatestMatches(league).get(league);
	}

	public Map<League, List<Match>> getAllLatestMatches() {		
		return getLatestMatches(League.values());
	}

	public Map<League, List<Match>> getLatestMatches(League... league) {
		List<Match> matchList = new ArrayList<Match>();
		Map<League, List<Match>> leagueMatchMap = new HashMap<League, List<Match>>();
		for(League l : league) leagueMatchMap.put(l, new ArrayList<Match>());

		//Retrieve all matches from <options>
		Document matchDoc = getJsoupTemplate().getDocumnetByAlias("url.allOdds");
		Elements matchTags = matchDoc.select("#footballmaincontent select option");
		for(Element elt : matchTags) {
			if(StringUtils.contains(elt.attr("value"), "matchid")) {
				if(log.isDebugEnabled()) log.debug("{}", elt.attr("value"));
				//List<String> matchInfo = RegexUtil.findSingleGroupList("matchid=(\\d+)&tdate=(\\d{2}\\-\\d{2}\\-\\d{4})&tday=(\\w{3})&tnum=(\\d+)", elt.attr("value"));
				//List<String> teamName = RegexUtil.findSingleGroupList("(\\S+) 對 (\\S+)", elt.text());
				//propMap.put("matchDate", LocalDateTime.parse(String.format("%s 00:00", matchInfo.get(1)), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

				List<String> matchInfo = RegexUtil.findSingleGroupList("^(\\d+)\\/(\\d+) (\\S+) (\\d+) (.+)", elt.text());
				String matchId = RegexUtil.findFirstSingleGroup("tmatchid=(\\d+)", elt.attr("value"));
				List<String> teamName = RegexUtil.findSingleGroupList("(\\S+) 對 (\\S+)", matchInfo.get(4));

				
				String matchYear = DateTimeUtil.isFutureMonthNextYear(matchInfo.get(1)) ? 
						LocalDateTime.now().plusYears(1).format(DateTimeFormatter.ofPattern("yyyy")) : LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
						
						LocalDateTime matchDatetime = LocalDateTime.parse(String.format("%02d-%02d-%s 00:00", 
								Integer.parseInt(matchInfo.get(0)), 
								Integer.parseInt(matchInfo.get(1)), 
								matchYear
								), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
						
						String matchDay = matchInfo.get(2);
						LocalDateTime matchDate = null;
						try {
							matchDate = DateTimeUtil.correctDateWithChineseDayOfWeek(matchDatetime, matchDay);
						} catch (ParseException e1) {
							log.error("Unable to parse {}", matchDay);
						}
						Map<String, Object> propMap = new HashMap<String, Object>();
						propMap.put("matchDate", matchDate);
						propMap.put("matchDatetime", matchDatetime);
						propMap.put("homeTeam", teamName.get(0));
						propMap.put("awayTeam", teamName.get(1));
						propMap.put("matchDay", matchDay);
						propMap.put("matchNum", matchInfo.get(3));
						propMap.put("type", Odds.TYPE.SYSTEM.getCode());
						propMap.put("status", Match.MATCH_STATUS.PENDING.getCode());
						propMap.put("season", Match.MATCH_SEASON.CURRENT.getLabel());
						Match footballMatch = new Match(matchId);
						try {
							BeanUtils.populate(footballMatch, propMap);
						} catch (IllegalAccessException | InvocationTargetException e) {
							log.error("Error on creating FootballMatch bean", e);
						}
						log.info("{}", footballMatch);
						matchList.add(footballMatch);
			}
		}
		log.info("Number of matches at [{}]: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), matchList.size());

		//Categorize each matches into leagues
		int numPages = (int) Math.ceil(matchList.size() / (double) numberMatchesPerPage);
		int i, n;
		for(i = 1, n = numPages; i <= n; i++) {
			Document categoryDoc = getJsoupTemplate().getDocumnetByAlias("url.main", new Integer(i));
			for(Match match : matchList) {
				if(log.isDebugEnabled()) log.debug(String.format("#rmid%s td.cflag img", match.getMatchId()));
				Element leagueNode = categoryDoc.select(String.format("#rmid%s td.cflag img", match.getMatchId())).first();
				Element timeNode = categoryDoc.select(String.format("#rmid%s td.cesst span", match.getMatchId())).first();

				String leagueCode = leagueNode == null ? null : leagueNode.className();
				if(log.isDebugEnabled()) log.debug("leagueCode:{}", leagueNode);
				String matchDateTime = timeNode == null ? null : timeNode.text();
				if(log.isDebugEnabled()) log.debug("timeNode:{}", timeNode);

				for(League l : league) {
					if(l.getCode().equals(leagueCode)) {
						Map<String, Object> propMap = new HashMap<String, Object>();
						propMap.put("league", l);
						if(matchDateTime != null) {
							List<String> matchDateParts = RegexUtil.findSingleGroupList("(\\d{2}/\\d{2}) (\\d{2}:\\d{2})", matchDateTime);
							LocalDateTime matchDate = LocalDateTime.parse(String.format("%s/%s %s", matchDateParts.get(0), match.getMatchDate().getYear(),
									matchDateParts.get(1)), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
							propMap.put("matchDate", matchDate);
						}
						try {
							BeanUtils.populate(match, propMap);
						} catch (IllegalAccessException | InvocationTargetException e) {
							log.error("Error on creating FootballMatch bean", e);
						}
						log.info("{}", match);
						leagueMatchMap.get(l).add(match);
					}
				}

			}
		}
		leagueMatchMap.forEach((k, v)-> log.info("{}:{}", k, v));

		return leagueMatchMap;
	}

	public Map<String, Object> getMatchUpStat(Match footballMatch) {
		Map<String, Object> map = new HashMap<String, Object>();
		Document statDoc = getJsoupTemplate().getDocumnetByAlias("url.stat.main", 
				footballMatch.getMatchDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), 
				footballMatch.getMatchDay(), 
				footballMatch.getMatchNum()
				);

		Elements vsElt = statDoc.select("a#topMostLayoutTemplate_H2HLayoutTemplate1_H2HPage1Control1_H2HTeamsRank1_H2H2Link");
		String params = RegexUtil.findFirstSingleGroup("\\?(.*)", vsElt.attr("href"));

		if(vsElt != null) {
			Document vsDoc = getJsoupTemplate().getDocumnetByAlias("url.stat.vs", params);
			Elements vslts = vsDoc.select("#topMostLayoutTemplate_H2HLayoutTemplate1_H2HPage2Control1__ctl0_htmlTable tr:eq(1) td");
			Elements vsHomeElts = vsDoc.select("#topMostLayoutTemplate_H2HLayoutTemplate1_H2HPage2Control1__ctl3_htmlTable tr:eq(1) td");
			//size = 3, win,draw,lose
			if(vslts.size() == 3) {
				map.put("vsWin", vslts.get(0).text());
				map.put("vsDraw", vslts.get(1).text());
				map.put("vsLose", vslts.get(2).text());
			}
			//size = 3, win,draw,lose
			if(vsHomeElts.size() == 3) {
				map.put("vsHomeWin", vsHomeElts.get(0).text());
				map.put("vsHomeDraw", vsHomeElts.get(1).text());
				map.put("vsHomeLose", vsHomeElts.get(2).text());
			}
		}
		return map;
	}

	/* Sample link:
	 * http://football.hkjc.com/football/statistics/chinese/centre/TeamResult.aspx?ci=zh-HK&goal_type=0&ha=0&hdc=-2&hf=0&hha=-1&isFG=1&league_no=1265&league_type=3&nf=0&player_pos=0&s_type=0&season_id=1547&ss=1&tdate=22-12-2017&tday=FRI&team_no=1154&tnum=1
	 */
	/*
	matchElt.child(0); //賽事
	matchElt.child(1); //日期
	matchElt.child(2); //HOME/AWAY
	matchElt.child(3); //W/D/L
	matchElt.child(4); //對手
	matchElt.child(5); //賽果
	matchElt.child(6); //角球
	 */
	public List<Match> getAllMatchResults(League league, Season season, Team team) {
		List<Match> list = new ArrayList<Match>();
		Map<String, Object> propertiesMap = null;
		boolean isNeutralStadium = false;
		String matchId;
		Match match;

		Document doc = getJsoupTemplate().getDocumnetByAlias("url.team.stat", league.getId(), season.getSeasonId(), team.getId()) ;
		Elements matchElts = doc.select("#topMostLayoutTemplate_layoutTemplate_TeamResultControl1_htmlTable tr:not(#topMostLayoutTemplate_layoutTemplate_TeamResultControl1_header_row1)");
		if(log.isDebugEnabled()) log.debug("Team stat table: {}", matchElts);
		for(Element matchElt : matchElts) {
			if(matchElt.childNodeSize() > 0) {
				if(StringUtils.isEmpty(matchElt.child(5).text()) || "-".equals(matchElt.child(6).text())) continue;
				if(League.FRIENDLY.getName().equals(StringUtils.trim(matchElt.child(0).text()))) continue;
				
				matchId = null;
				League matchLeague = League.getByName(matchElt.child(0).text());
				propertiesMap = new HashMap<String, Object>();
				propertiesMap.put("league", matchLeague == null ? League.OTHERS : matchLeague);
				LocalDateTime matchDate = LocalDateTime.parse(String.format("%s 00:00", matchElt.child(1).text()), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
				propertiesMap.put("matchDay", StringUtils.upperCase(matchDate.format(DateTimeFormatter.ofPattern("EEE"))));
				List<String> matchResult = RegexUtil.findSingleGroupList("(\\d+):(\\d+)\\((\\d+):(\\d+)\\)", matchElt.child(5).text());
				propertiesMap.put("status", Match.MATCH_STATUS.COMPLETED.getCode());
				propertiesMap.put("dateUpdated", LocalDateTime.now());
				propertiesMap.put("season", season.getSeason());

				if(Match.MATCH_AT.NEUTRAL.getLabel().equals(matchElt.child(2).text())) {
					isNeutralStadium = true;
					propertiesMap.put("neutral", "Y");
				}

				if(Match.MATCH_AT.HOME.getLabel().equals(matchElt.child(2).text())) {
					propertiesMap.put("homeTeam", team.getName());
					propertiesMap.put("awayTeam", matchElt.child(4).text());
					if(matchResult.size() > 0) {
						propertiesMap.put("homeScore", matchResult.get(0));
						propertiesMap.put("awayScore", matchResult.get(1));
						propertiesMap.put("homeHalfScore", matchResult.get(2));
						propertiesMap.put("awayHalfScore", matchResult.get(3));
					}
				} else {
					propertiesMap.put("homeTeam", matchElt.child(4).text());
					propertiesMap.put("awayTeam", team.getName());
					if(matchResult.size() > 0) {
						propertiesMap.put("homeScore", matchResult.get(1));
						propertiesMap.put("awayScore", matchResult.get(0));
						propertiesMap.put("homeHalfScore", matchResult.get(3));
						propertiesMap.put("awayHalfScore", matchResult.get(2));
					}
				}
				/* Sample link
				 * http://bet.hkjc.com/football/results/search_result.aspx?lang=CH&search=true&srchdate=1&fdate=20171104&tdate=20171104&srchteam=1&teamcode=541
				 */
				try {
					getMatchResult(team.getId(), matchDate);
				} catch (Exception e) {
					log.error("Exception caught on fetching match result", e);
				}
				
				Document matchDoc = getJsoupTemplate().getDocumnetByAlias("url.match.result", 
											matchDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), 
											matchDate.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")), 
											team.getId());
				log.info("calling url:{}", matchDoc.baseUri());

				Elements matchNumberNode = matchDoc.select("#footballmaincontent .matchNum"); 
				if(matchNumberNode.size() > 0) {
					if(log.isDebugEnabled()) log.debug("Match number node: {}", matchNumberNode.get(1).text()); // 0 is the header row
					List<String> matchNumber = RegexUtil.findSingleGroupList("(星期.+?) (\\d+)", matchNumberNode.get(1).text());
					if(matchNumber.size() == 2) {
						String matchDayOfWeek = matchNumber.get(0); 
						propertiesMap.put("matchDay", matchDayOfWeek);
						propertiesMap.put("matchNum", matchNumber.get(1));
						
						//Match date showing in team history may not be correct, fix it by comparing the day of week
						try {
							matchDate = DateTimeUtil.correctDateWithChineseDayOfWeek(matchDate, matchDayOfWeek);
						} catch (ParseException e) {
							log.error("Unable to parse {}", matchDayOfWeek);
						}
					}				
					
					Element matchIdNode = matchDoc.select("#footballmaincontent .matchOdds a").first(); // header row is without anchor <a> element
					if(log.isDebugEnabled()) log.debug("Match id node: {}", matchIdNode.attr("href"));

					matchId = RegexUtil.findFirstSingleGroup(".+?id=(\\d+)", matchIdNode.attr("href"));
				}

				if(isNeutralStadium) {
					Elements matctupNode = matchDoc.select("#footballmaincontent .matchTeam");
					if(matctupNode.size() > 1) {
						if(log.isDebugEnabled()) log.debug("Matchup node: {}", matctupNode.first().text()); 
						List<String> matchup = RegexUtil.findSingleGroupList("(.+) 對 (.+)", matctupNode.first().text());
						if(matchup.size() == 2) {
							propertiesMap.put("homeTeam", matchup.get(0));
							propertiesMap.put("awayTeam", matchup.get(1));
						}
					}
					Element halfScoreNode = matchDoc.getElementById("ctl00_cm_rptMatches_ctl01_tdHalfRes");
					if(halfScoreNode != null) {
						List<String> halfScore = RegexUtil.findSingleGroupList("(\\d+) : (\\d+)", halfScoreNode.text());
						propertiesMap.put("homeHalfScore", halfScore.get(0));
						propertiesMap.put("awayHalfScore", halfScore.get(1));
					}
					Element fullScoreNode = matchDoc.getElementById("ctl00_cm_rptMatches_ctl01_tdFullRes");
					if(fullScoreNode != null) {
						List<String> min90Score = RegexUtil.findSingleGroupList("(\\d+) : (\\d+)", fullScoreNode.text());
						propertiesMap.put("homeScore", min90Score.get(0));
						propertiesMap.put("awayScore", min90Score.get(1));
						
					}
				}

				Element extraScoreNode = matchDoc.getElementById("ctl00_cm_rptMatches_ctl01_tdFullRes");
				if(extraScoreNode != null) {
					List<String> extraScore = RegexUtil.findSingleGroupList("(\\d+) : (\\d+) \\[(\\d+) : (\\d+)\\]", extraScoreNode.text());
					if(extraScore.size() > 0) {
						propertiesMap.put("remark", String.format("%s - [%s : %s]", Match.MATCH_REMARK.ADDED_TIME.getMessage(), extraScore.get(2), extraScore.get(3)));
					}
				}
				propertiesMap.put("matchDate", matchDate);
				
				if(matchId != null ) match = new Match(matchId);
				else continue;
				try {
					BeanUtils.populate(match, propertiesMap);
					int result = NumberUtils.compare(match.getHomeScore(), match.getAwayScore());
					match.setResult(result > 0 ? 
							Match.MATCH_RESULT.WIN.getCode() : result == 0 ? 
									Match.MATCH_RESULT.DRAW.getCode() : Match.MATCH_RESULT.LOSE.getCode());
				} catch (IllegalAccessException | InvocationTargetException e) {
					log.debug("Error in populating FootballMatch", e);
				}

				list.add(match);
				log.info("FootballMatch: {}", match);
			}
		}
		return list;
	}

	public Match getMatchResult(String teamId, LocalDateTime startDate) throws Exception {
		
		String url = String.format(matchResultJsonURL, 
									startDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")),
									startDate.plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")), 
									teamId
								);
        
        ResponseEntity<String> response = getRestTemplate().getForEntity(url, String.class);
        
        log.info("xxxxxxxxxxxxx{}", response.getHeaders());
		
		log.info("Calling match json URL: {}", url);
		JsonNode root = new ObjectMapper().readTree(response.getBody());	
		
		int matchCount = root.path(0).get("matchescount").asInt(0);
		if(matchCount == 0) throw new Exception(String.format("No match is returned for teamId[%s], matchDate[%s]", teamId, startDate));
		if(matchCount > 1) log.warn("Unexpected match result count for teamId[{}], matchDate[{}], matchCount[{}]", teamId, startDate, matchCount);
		
		Map<String, Object> fieldMap = new HashMap<String, Object>();
		fieldMap.put("matchId", root.path(0).path("matches").path(0).path("matchID"));
		

		return new Match();
	}


}
