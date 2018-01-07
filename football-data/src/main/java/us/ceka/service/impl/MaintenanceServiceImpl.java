package us.ceka.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.dao.PlayerDao;
import us.ceka.dao.SeasonDao;
import us.ceka.dao.StandingDao;
import us.ceka.dao.TeamDao;
import us.ceka.dao.TeamURLDao;
import us.ceka.domain.League;
import us.ceka.domain.Match;
import us.ceka.domain.Season;
import us.ceka.domain.Standing;
import us.ceka.domain.StandingId;
import us.ceka.domain.Team;
import us.ceka.domain.TeamURL;
import us.ceka.dto.LeagueDto;
import us.ceka.dto.MatchDto;
import us.ceka.dto.PlayerDto;
import us.ceka.dto.StandingDto;
import us.ceka.dto.TeamDto;
import us.ceka.service.MaintenanceService;

@Service("maintenanceService")
@Transactional
public class MaintenanceServiceImpl extends GenericServiceImpl implements MaintenanceService{
	@Autowired
	private TeamDto footballTeamDto;
	@Autowired
	private LeagueDto footballLeagueDto;
	@Autowired
	private TeamDao footballTeamDao;
	@Autowired
	private SeasonDao footballSeasonDao;
	@Autowired
	private StandingDao footballStandingDao;
	@Autowired
	private PlayerDao footballPlayerDao;
	
	@Autowired
	private MatchDto footballMatchDto;
	@Autowired
	private StandingDto footballStandingDto;
	@Autowired
	private PlayerDto footballPlayerDto;
	@Autowired
	private TeamURLDao footballTeamURLDao;

	public void executeRefreshStanding() {
		
		log.info("truncate footballStanding table");
		footballStandingDao.truncate();
		
		for(League league : League.values()) {
			if (League.TYPE.LEAGUE.equals(league.getType())) {
				//FootballSeason season = footballSeasonDao.getLatestSeason(league);
				for(Season season : footballSeasonDao.getSeasons(league, 3)) {			
				footballStandingDto.getLeagueStanding(season.getSeasonId(), league.getId()).forEach(fl -> {
					fl.setFootballStandingId(new StandingId(league.getId(), season.getSeasonId(), footballTeamDao.getByName(fl.getTeam()).getId()));
					fl.setLeague(league.name());
					fl.setSeason(season.getSeason());
					log.info("Insert FootballStanding{}", fl);
					footballStandingDao.persist(fl);
				});
				}
			}
		}

	}

	public void executeRefreshTeamTable() {
		log.info("truncate footballteam table");
		footballTeamDao.truncate();

		List<Team> set = footballTeamDto.getAllTeams();

		log.info("insert records into footballteam table");
		for(Team t : set) {
			footballTeamDao.persist(t);
		}
	}

	public void executeRefreshFootballLeague() {
		log.info("truncate footballSeason table");
		footballSeasonDao.truncate();

		for(League league : League.values()) {
			if(league.getType().equals(League.TYPE.LEAGUE)) {
				List<Season> seasonList = footballLeagueDto.getAllSeasons(league);

				seasonList.forEach( (season) -> {
					String rank1Team = footballTeamDto.getTeamNamesByLeague(league, season).get(0);
					List<Match> l = footballMatchDto.getAllMatchResults(league, season, footballTeamDao.getByName(rank1Team));
					if(!Match.MATCH_SEASON.CURRENT.getLabel().equals(season.getSeason())) season.setEnd(l.get(0).getMatchDate());
					if(l.size() > 0) season.setStart(l.get(l.size() - 1).getMatchDate());
					log.info("insert season: {}", season);
					footballSeasonDao.persist(season);
				});
			}
		}
	}

	@Override
	public void executeRefreshPlayerTable() {
		Season season = footballSeasonDao.getLatestSeason(League.JPN_JLEAGUE);
		List<Standing> teams = footballStandingDao.getStanding(League.JPN_JLEAGUE.name(), season.getSeason());
		TeamURL url = footballTeamURLDao.getByKey(teams.get(0).getFootballStandingId().getTeamId()); 
		
		footballPlayerDto.getPlayers(season, url).forEach((player) -> {
			footballPlayerDao.persist(player);
		});
	}
}
