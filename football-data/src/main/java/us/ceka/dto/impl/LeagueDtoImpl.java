package us.ceka.dto.impl;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import us.ceka.domain.League;
import us.ceka.domain.Season;
import us.ceka.dto.LeagueDto;

@Repository("leagueDto")
public class LeagueDtoImpl extends BaseDtoJsoupImpl<League> implements LeagueDto{
	public List<Season> getAllSeasons(League league) {
		
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.league.current.table", league.getId());
		List<Season> list = new ArrayList<Season> ();
		Elements seasonElts = doc.select("#topMostLayoutTemplate_layoutTemplate_TeamHeader1_SeasonSelector1_seasonList option");
		if(log.isDebugEnabled()) log.debug("Elements retrived: {}", seasonElts);
		for(Element season : seasonElts) {
			if(season.attr("value") != null) {
				Season s = new Season();
				s.setSeasonId(season.attr("value"));
				s.setSeason(season.text());
				s.setLeagueId(league);
				s.setLeague(league.name());
				s.setLeagueName(league.getName());
				log.info("FootballSeason: {}", s);
				list.add(s);
			}
		}
		return list;
	}

}
