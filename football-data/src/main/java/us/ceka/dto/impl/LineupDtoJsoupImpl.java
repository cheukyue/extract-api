package us.ceka.dto.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Repository;

import us.ceka.domain.Lineup;
import us.ceka.domain.LineupPlayer;
import us.ceka.domain.Match;
import us.ceka.dto.LineupDto;
import us.ceka.util.RegexUtil;

@Repository("lineupDto")
public class LineupDtoJsoupImpl extends BaseDtoJsoupImpl<Lineup> implements LineupDto{

	private static int PAGE_MAX = 20;
	private static String LINEUP_CONFIRM = "Confirmed";
	
	private static final Map<String, String> positionMap;
	static {
		positionMap = new HashMap<String, String>();
		positionMap.put("gk", "GoalKeeper");
		positionMap.put("lb", "LeftBack");
		positionMap.put("rb", "RightBack");
		positionMap.put("cb", "CentreBack");
		positionMap.put("lm", "LeftMidField");
		positionMap.put("rm", "RightMidField");
		positionMap.put("lw", "LeftWing");
		positionMap.put("rw", "RightWing");
		positionMap.put("am", "AttackMidField");
		positionMap.put("dm", "AttackMidField");
		positionMap.put("cm", "AttackMidField");
		positionMap.put("fw", "Forward");
	}
	
	public List<Lineup> getHistoricLineupList(String team, LocalDateTime dateSince) {
		List<Lineup> list = new ArrayList<Lineup> ();
		//Document doc = getJsoupTemplate().getDocumnetByAlias("url.teamfeed.lineup", StringUtils.lowerCase(team).replace(" ", "-"));
		int i, n;
		for(i=1,n=PAGE_MAX; i<=n; i++) {
			log.info("Team/page:{} {}", StringUtils.lowerCase(team).replace(" ", "-"), i);
			Document doc = getJsoupTemplate().getDocumnetByAlias("url.teamfeed.lineup.page", StringUtils.lowerCase(team).replace(" ", "-"), String.valueOf(i));
			if(doc.select(".pitch-header").size() == 0) break;
			
			List<Lineup> pageList = parseData(doc, dateSince);
			if(pageList.size() == 0) break;
			
			list.addAll(parseData(doc, dateSince));
		}
		
		return list;
	}


	public List<Lineup> getLineupList(String team) {
		log.info("Fetching team: {}", team);
		Document doc = getJsoupTemplate().getDocumnetByAlias("url.teamfeed.lineup", StringUtils.lowerCase(team).replace(" ", "-"));
		return parseData(doc);
	}
	
	private List<Lineup> parseData(Document doc) {
		return parseData(doc, LocalDateTime.MIN);
	}
	
	private List<Lineup> parseData(Document doc, LocalDateTime dateSince) {
		//String[] matchType = new String[] {"UEFA Champions League", "UEFA Europa League", "Championship", "Premier League"};
		String season = Match.MATCH_SEASON.CURRENT.getLabel(); ///////////season ///////

		return IntStream.range(0, doc.select(".pitch-header").size()).mapToObj((matchItr) -> {		
			Lineup lineup = new Lineup();
			Set<LineupPlayer> players = new HashSet<LineupPlayer>();

			Element pitchHeader = doc.select(".pitch-header").get(matchItr);
			Element pitch = doc.select(".pitch").get(matchItr);
			Element pitchFooter = doc.select(".pitch-footer").get(matchItr);
			
			
			if(log.isDebugEnabled()) log.debug("pitchHeader: {}", pitchHeader);		

			List<String> caption = RegexUtil.findSingleGroupList("\\- (.+?) \\- [a-zA-Z]+? ([0-9]+\\. [a-zA-Z]+ [0-9]{4}\\, [0-9]+:[0-9]+)$", pitchHeader.text());
			String leagueStr = caption.get(0);
			
			LocalDateTime matchDate = LocalDateTime.parse(caption.get(1), DateTimeFormatter.ofPattern("d. MMMM yyyy, HH:mm", Locale.US));

			//if(!LINEUP_CONFIRM.equalsIgnoreCase(doc.select("h2 .lineup-status").get(matchItr).text())) return null;
			if(dateSince.compareTo(matchDate) > 0) return null;
			
			Map<String, Object> prop = new HashMap<String, Object>();
			prop.put("finalized", LINEUP_CONFIRM.equalsIgnoreCase(doc.select("h2 .lineup-status").get(matchItr).text()));
			prop.put("matchDate", matchDate);
			//prop.put("league", Arrays.stream(matchType).filter(t -> t.equalsIgnoreCase(leagueStr)).findFirst().get());
			prop.put("league", leagueStr);
			prop.put("season", season);
			prop.put("homeTeam", pitch.select(".teamname-h").text());
			prop.put("awayTeam", pitch.select(".teamname-a").text());
			prop.put("homeFormation", pitch.select(".formation-h").text());
			prop.put("awayFormation", pitch.select(".formation-a").text());
			prop.put("homeSubstitutionNo", pitchFooter.select(".home-subs .help-tip").stream()
					.map(node -> RegexUtil.findFirstSingleGroup("([0-9]+)\\.", node.text())).collect(Collectors.joining(",")));
			prop.put("awaySubstitutionNo", pitchFooter.select(".away-subs .help-tip").stream()
					.map(node -> RegexUtil.findFirstSingleGroup("([0-9]+)\\.", node.text())).collect(Collectors.joining(",")));
			prop.put("homeSubstitution", pitchFooter.select(".home-subs .help-tip").stream()
					.map(node -> RegexUtil.findFirstSingleGroup("[0-9]+\\. (.+)", node.text())).collect(Collectors.joining(",")));
			prop.put("awaySubstitution", pitchFooter.select(".away-subs .help-tip").stream()
					.map(node -> RegexUtil.findFirstSingleGroup("[0-9]+\\. (.+)", node.text())).collect(Collectors.joining(",")));

			prop.put("homeLineupNo", pitch.select(".player-h .player-no").stream().map(Element::text).collect(Collectors.joining(",")));
			prop.put("awayLineupNo", pitch.select(".player-a .player-no").stream().map(Element::text).collect(Collectors.joining(",")));
			prop.put("homeLineup", pitch.select(".player-h .player-name").stream().map(Element::text).collect(Collectors.joining(",")));
			prop.put("awayLineup", pitch.select(".player-a .player-name").stream().map(Element::text).collect(Collectors.joining(",")));

			try {
				BeanUtils.populate(lineup, prop);
			} catch (IllegalAccessException | InvocationTargetException e) {
				log.error("Error on populating value into Lineup", e);
			}

			Arrays.stream(new String[] {"h", "a"}).forEach( side -> {
				pitch.select(String.format(".player-%s", side)).forEach((node) -> {

					String pattern = String.format("(\\w+?\\-%s%s)", side, "$");
					String position = StringUtils.substring(
							RegexUtil.findFirstSingleGroup(pattern, node.className()), 0, 2);

					LineupPlayer player = new LineupPlayer();
					player.setSide("h".equals(side) ? LineupPlayer.SIDE.HOME : LineupPlayer.SIDE.AWAY);
					player.setPlayerNo(NumberUtils.toInt(node.select(".player-no").text()));
					player.setName(node.select(".player-name").text());
					player.setPosition(position);
					player.setTeam("h".equals(side) ? lineup.getHomeTeam() : lineup.getAwayTeam());
					players.add(player);

					if(log.isDebugEnabled()) log.debug("positionMap > search pattern, position returned", pattern, position);
				});
			});
			lineup.setPlayers(players);
			log.info("{}", lineup);

			return lineup;
		}).filter(l -> l != null).collect(Collectors.toList());
		
	}


}
