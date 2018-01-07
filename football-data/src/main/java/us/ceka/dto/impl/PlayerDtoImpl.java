package us.ceka.dto.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Repository;

import us.ceka.domain.Player;
import us.ceka.domain.PlayerId;
import us.ceka.domain.Season;
import us.ceka.domain.Team;
import us.ceka.domain.TeamURL;
import us.ceka.dto.PlayerDto;

@Repository("playerDto")
public class PlayerDtoImpl extends BaseDtoJsoupImpl<Player> implements PlayerDto {
	public List<Player> getPlayers(Season season, TeamURL url) {
		List<Player> list = new ArrayList<Player>();

		//String urawa = "https://www.jleague.jp/club/urawa/ajax_player?callingTeamId=122";
		String urawa = "http://www.football-lab.jp/uraw/?year=2017";
		String kashima = "https://www.jleague.jp/club/kashima/day/#player";
		Document doc = getJsoupTemplate().getDocument(urawa);

		Elements playerElts = doc.select("#sorTable tr");
		for(Element playerNode : playerElts) {
			if("thead".equals(playerNode.parent().tagName())) continue;
			String playerNo = playerNode.child(1).text();
			if(StringUtils.isNumeric(playerNo)) {

				Map<String, Object> map = new HashMap<String, Object>();
				map.put("season", season.getSeason());
				map.put("team", url.getTeam());
				map.put("leagueId", season.getLeagueId().getId());
				map.put("league", season.getLeague());
				
				map.put("name", playerNode.child(2).text());
				map.put("position", playerNode.child(0).text());
				//map.put("origin", playerNode.child(4).text());
				//map.put("played", playerNode.child(7).text());
				//map.put("goal", playerNode.child(8).text());

				Player player = new Player();
				player.setFootballPlayerId(new PlayerId(season.getSeasonId(), url.getTeamId(), NumberUtils.toInt(playerNo)));
				try {
					BeanUtils.populate(player, map);
				} catch (IllegalAccessException | InvocationTargetException e) {
					log.error("Error on populating value into FootballPlayer", e);
				}

				if(log.isDebugEnabled()) log.debug("{}", player);
				list.add(player);
			}
		}
		return list;

	}
}
