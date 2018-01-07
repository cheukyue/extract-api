package us.ceka.dto.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import us.ceka.domain.League;
import us.ceka.domain.Match;
import us.ceka.domain.Season;
import us.ceka.domain.Team;
import us.ceka.dto.TeamDto;
import us.ceka.util.RegexUtil;

@Repository("teamDto")
public class TeamDtoImpl extends BaseDtoJsoupImpl<Team> implements TeamDto{
	
	public List<String> getTeamNamesByLeague(League league, Season season) {
		List<String> l = new ArrayList<String>();
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.league.table", league.getId(), season.getSeasonId());
		Elements teamElts = doc.select("table.mainTable td a");
		teamElts.forEach((team)-> {
				log.info("Team Name: {}", team.text());
				l.add(team.text());
		});
		return l;
	}
	
	public List<Team> getAllTeams() {
		List<Team> list = new ArrayList<Team> ();
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.teams");
		Elements teams = doc.select("#ctl00_cm_srch_DDL_TeamList option");
		for(Element t : teams) {
			String teamId = t.attr("value");
			if(log.isDebugEnabled()) log.debug("#ctl00_cm_srch_DDL_TeamList option html:{}", t);
			if(NumberUtils.isDigits(teamId)) {
				List<String> teamName = RegexUtil.findSingleGroupList("(.*?)\\s\\((.*)\\)", t.text()); 
				if(teamName.size() > 0) {
					if("Kyoto Purple Sanga".equals(teamName.get(1))) continue;
					Team ft = new Team();
					ft.setId(teamId);
					ft.setName(teamName.get(0));
					ft.setEngName(teamName.get(1));
					log.info("{}", ft);
					list.add(ft);
				}
			}
		}

		return list;
	}
	
	public Map<String, Object> getResult(LocalDateTime fromDate, LocalDateTime toDate, String teamId) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.match.result", 
				fromDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), 
				toDate.format(DateTimeFormatter.ofPattern("yyyyMMdd")), teamId);
		Element halfResult = doc.getElementById("ctl00_cm_rptMatches_ctl01_tdHalfRes");
		
		if(halfResult != null) {
			if(Match.MATCH_REMARK.CANCELLED.getMessage().equals(halfResult.text())) {
				map.put("status", Match.MATCH_STATUS.RESCHEDULED.getCode());
				map.put("remark", Match.MATCH_REMARK.CANCELLED.getMessage());
				return map;
			}
			List<String> hl = RegexUtil.findSingleGroupList("([0-9]+) : ([0-9]+)", halfResult.text());
			map.put("homeHalfScore", hl.get(0));
			map.put("awayHalfScore", hl.get(1));
		}
		Element fullResult = doc.getElementById("ctl00_cm_rptMatches_ctl01_tdFullRes");
		if(fullResult != null) {
			if(Match.MATCH_REMARK.CANCELLED.getMessage().equals(fullResult.text())) {
				map.put("status", Match.MATCH_STATUS.RESCHEDULED.getCode());
				map.put("remark", Match.MATCH_REMARK.CANCELLED.getMessage());
				return map;
			}
			List<String> fl = RegexUtil.findSingleGroupList("([0-9]+) : ([0-9]+)", fullResult.text());
			map.put("homeScore", fl.get(0));
			map.put("awayScore", fl.get(1));
		}
		
		if(halfResult != null && fullResult != null)
			map.put("status", Match.MATCH_STATUS.COMPLETED.getCode());

		return map;
	}
	
	public Map<String, Object> getResult(LocalDateTime date, String teamId) {
		return getResult(date, date.plusDays(1), teamId);
	}
}
