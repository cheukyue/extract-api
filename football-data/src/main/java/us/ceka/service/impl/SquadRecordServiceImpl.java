package us.ceka.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.dao.LineupDao;
import us.ceka.dao.LineupPlayerDao;
import us.ceka.dao.StandingDao;
import us.ceka.dao.TeamNameDao;
import us.ceka.domain.League;
import us.ceka.domain.Lineup;
import us.ceka.domain.Match;
import us.ceka.dto.LineupDto;
import us.ceka.service.SquadRecordService;

@Service("squadRecordService")
@Transactional
public class SquadRecordServiceImpl extends GenericServiceImpl implements SquadRecordService{
	@Autowired
	private LineupDto lineupDto;

	@Autowired
	private StandingDao standingDao;
	@Autowired
	private TeamNameDao teamNameDao;
	@Autowired
	private LineupDao lineupDao;
	@Autowired
	private LineupPlayerDao lineupPlayerDao;


	public void executeRecordMajorSquad() {
		persistSquad(new League[] {League.ENG_PREMIER_LEAGUE, League.GER_BUNDESLIGA});
	}

	private void persistSquad(League[] leagues) {
		
		Arrays.stream(leagues).forEach(league -> {
			List<String> teams = standingDao.getStanding(league.name(), Match.MATCH_SEASON.CURRENT.getLabel()).stream()
					.map(team -> teamNameDao.getByKey(team.getFootballStandingId().getTeamId()).getTeamfeed())
					.collect(Collectors.toList());

			log.info("all teams: {}", teams);

			teams.stream().filter(team -> team.equals(team)).forEach(team -> {
				lineupDto.getLineupList(team).forEach( lineup -> {
					Lineup l = lineupDao.getByDateTeam(lineup.getMatchDate(), lineup.getHomeTeam(), lineup.getAwayTeam());
					if(l == null) {
						lineupDao.persist(lineup);

						lineup.getPlayers().forEach(player -> {
							player.setLineupId(lineup.getId());
							lineupPlayerDao.persist(player);
						});
					} 
					else if (Boolean.FALSE.equals(l.isFinalized()))  {
						lineupPlayerDao.getPlayersByLineupId(lineup.getId()).forEach(player -> lineupPlayerDao.delete(player));

						lineup.setId(l.getId());
						lineupDao.merge(lineup);

						lineup.getPlayers().forEach(player -> {
							player.setLineupId(lineup.getId());
							lineupPlayerDao.persist(player);
						});
					}

				});			
			});
		});
	}

	public void recordPremierLeagueSquadByDateBefore() {

		LocalDateTime dateSince = LocalDateTime.parse("2011-01-01 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

		List<String> teams = standingDao.getStanding(League.GER_BUNDESLIGA.name(), Match.MATCH_SEASON.CURRENT.getLabel()).stream()
				.map(team -> teamNameDao.getByKey(team.getFootballStandingId().getTeamId()).getTeamfeed())
				.collect(Collectors.toList());

		log.info("all teams: {}", teams);

		//teams.stream().filter(team -> team.equals("Manchester United") )
		//Southampton, Liverpool, Stoke, Swansea, Huddersfield, Watford, Everton, Manchester City, Manchester Utd, Leicester, Crystal Palace, Tottenham, Brighton, Newcastle, Burnley, Bournemouth, West Bromwich, Chelsea, Arsenal, West Ham
		teams.stream()
		.forEach(team -> {
			lineupDto.getHistoricLineupList(team, dateSince).forEach( lineup -> {
				if(lineupDao.getByDateTeam(lineup.getMatchDate(), lineup.getHomeTeam(), lineup.getAwayTeam()) == null) {
					lineupDao.persist(lineup);
					log.info("persist lineup:{}", lineup);
					lineup.getPlayers().forEach(player -> {
						player.setLineupId(lineup.getId());
						lineupPlayerDao.persist(player);
					});
				}
			});			
		});	
	}

}
