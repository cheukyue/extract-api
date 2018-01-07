package us.ceka.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.dao.MatchDao;
import us.ceka.dao.OddsDao;
import us.ceka.dao.SeasonDao;
import us.ceka.dao.TeamDao;
import us.ceka.domain.League;
import us.ceka.domain.Match;
import us.ceka.domain.Match.MATCH_SEASON;
import us.ceka.domain.Odds;
import us.ceka.domain.OddsId;
import us.ceka.domain.Season;
import us.ceka.domain.Team;
import us.ceka.dto.LeagueDto;
import us.ceka.dto.MatchDto;
import us.ceka.dto.OddsDto;
import us.ceka.dto.TeamDto;
import us.ceka.service.OddsRecordService;

@Service("oddsRecordService")
@Transactional
public class OddsRecordServiceImpl extends GenericServiceImpl implements OddsRecordService{

	@Autowired
	private OddsDao footballOddsDao;
	@Autowired
	private MatchDao footballMatchDao;
	@Autowired
	private TeamDao footballTeamDao;
	@Autowired
	private SeasonDao footballSeasonDao;

	@Autowired
	private MatchDto footballMatchDto;
	@Autowired
	private OddsDto footballOddsDto;
	@Autowired
	private TeamDto footballTeamDto;
	@Autowired
	private LeagueDto footballLeagueDto;

	public void executeRecordOdds() {
		String batch = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyymmdd-HHmmss"));

		Map<League, List<Match>> map = footballMatchDto.getLatestMatches(League.values());
		map.forEach((league, matchList) -> {
			for(Match match : matchList) {
				Match dbMatch = footballMatchDao.getByKey(match.getMatchId());
				if(dbMatch == null) footballMatchDao.persist(match);

				Odds odds = footballOddsDto.getLatestById(match.getMatchId());
				if(odds != null) {
					odds.setFootballOddsId(new OddsId(match.getMatchId(), batch));
					Odds dbOdds = footballOddsDao.findRecentOddsRecord(match.getMatchId());
					if(!odds.rateEquals(dbOdds)) {
						log.info("Rate changed: Odds Odds:{}\n---New Odds:{}", dbOdds, odds);
						footballOddsDao.persist(odds);
					}
				}
			}				
		});

	}

	public void executeUpdateMatchResult() {
		List<Match> list = footballMatchDao.findByStatus(Match.MATCH_STATUS.PENDING.getCode());
		for(Match match : list) {
			Team team = footballTeamDao.getByName(match.getHomeTeam());
			if(team != null) {
				Map<String, Object> matchMap = footballTeamDto.getResult(match.getMatchDate(), team.getId());

				//message will be given if there is an error
				if(matchMap.size() > 0) {
					matchMap.put("dateUpdated", LocalDateTime.now());
					try {
						BeanUtils.populate(match, matchMap);
						int result = NumberUtils.compare(match.getHomeScore(), match.getAwayScore());
						match.setResult(result > 0 ? 
								Match.MATCH_RESULT.WIN.getCode() : result == 0 ? 
										Match.MATCH_RESULT.DRAW.getCode() : Match.MATCH_RESULT.LOSE.getCode());
						match.setSeason(MATCH_SEASON.CURRENT.getLabel());

					} catch (IllegalAccessException | InvocationTargetException e) {
						log.error("Error when populating map to FootballMatch bean", e);
					}

					log.info("Update FootallMatch result: {}", match);
					footballMatchDao.persist(match);
				}
			}
		}
	}

	public void executeUpdateTeamMatches() {
		//List<FootballLeague> leagueList = new java.util.ArrayList<FootballLeague>();
		//leagueList.add(FootballLeague.ARG_DIVISION_1);
		//for(FootballLeague league : leagueList) {
		for(League league : League.values()) {
			if(league.getType().equals(League.TYPE.LEAGUE)) {
				//Retrieve current season and insert matches of current seasons into database  
				List<Season> seasonList = footballLeagueDto.getAllSeasons(league).subList(0, 1); //footballSeasonDao.getSeasons(league, 2); 				
				seasonList.forEach( (season) -> {
					footballTeamDto.getTeamNamesByLeague(league, season).forEach((teamName) -> {
						if(log.isDebugEnabled()) log.debug("Arguments(league, season, team): {}, {}, {}", league, season, teamName);
						List<Match> l = footballMatchDto.getAllMatchResults(league, season, footballTeamDao.getByName(teamName));
						for(Match match : l) {
							Team homeTeam = footballTeamDao.getByName(match.getHomeTeam());
							Team awayTeam = footballTeamDao.getByName(match.getAwayTeam());
							if(homeTeam != null && awayTeam != null){
								String matchId = String.format("A%s-%s-%s", 
										match.getMatchDate().format(DateTimeFormatter.ofPattern("yyyyMMdd")),
										StringUtils.leftPad(homeTeam.getId(), 5, "0"),
										StringUtils.leftPad(awayTeam.getId(), 5, "0")
										);
								match.setMatchId(matchId);
								if(!match.equals(footballMatchDao.getMatch(match.getHomeTeam(), match.getAwayTeam(), match.getMatchDate()))) {
									log.info("Insert football match:{}", match);
									footballMatchDao.persist(match);
								}
							}
						}
					});
				});			
			}
		}
		
	}
	
	public void executeUpdateTeamMatchesWithOdds() {
		//List<FootballLeague> leagueList = new java.util.ArrayList<FootballLeague>();
		//leagueList.add(FootballLeague.ESP_LALIGA);
		//for(FootballLeague league : leagueList) {
		for(League league : League.values()) {
			if(league.getType().equals(League.TYPE.LEAGUE)) {
				//Retrieve current season and insert matches of current seasons into database  
				List<Season> seasonList = footballLeagueDto.getAllSeasons(league).subList(0, 1); //footballSeasonDao.getSeasons(league, 2);
				//List<FootballSeason> seasonList = footballSeasonDao.getSeasons(league, 4);
				seasonList.forEach( (season) -> {
					footballTeamDto.getTeamNamesByLeague(league, season).forEach((teamName) -> {
						if(log.isDebugEnabled()) log.debug("Arguments(league, season, team): {}, {}, {}", league, season, teamName);
						List<Match> l = footballMatchDto.getAllMatchResults(league, season, footballTeamDao.getByName(teamName));
						for(Match match : l) {
							//if(!match.equals(footballMatchDao.getMatch(match.getHomeTeam(), match.getAwayTeam(), match.getMatchDate()))) {
							if(!match.equals(footballMatchDao.getByKey(match.getMatchId()))) {
								log.info("Insert football match:{}", match);
								footballMatchDao.persist(match);
								footballOddsDao.persist(footballOddsDto.getArchiveById(match.getMatchId()));
							}
						}
					});
				});
			}
		}
	}




}
