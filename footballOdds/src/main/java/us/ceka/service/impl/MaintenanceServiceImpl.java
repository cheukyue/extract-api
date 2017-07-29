package us.ceka.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import us.ceka.dao.FootballSeasonDao;
import us.ceka.dao.FootballStandingDao;
import us.ceka.dao.FootballTeamDao;
import us.ceka.domain.FootballLeague;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballSeason;
import us.ceka.domain.FootballStandingId;
import us.ceka.domain.FootballTeam;
import us.ceka.dto.FootballLeagueDto;
import us.ceka.dto.FootballMatchDto;
import us.ceka.dto.FootballStandingDto;
import us.ceka.dto.FootballTeamDto;
import us.ceka.service.MaintenanceService;

@Service("maintenanceService")
@Transactional
public class MaintenanceServiceImpl extends GenericServiceImpl implements MaintenanceService{
	@Autowired
	private FootballTeamDto footballTeamDto;
	@Autowired
	private FootballLeagueDto footballLeagueDto;
	@Autowired
	private FootballTeamDao footballTeamDao;
	@Autowired
	private FootballSeasonDao footballSeasonDao;
	@Autowired
	private FootballStandingDao footballStandingDao;
	@Autowired
	private FootballMatchDto footballMatchDto;
	@Autowired
	private FootballStandingDto footballStandingDto;

	public void executeRefreshStanding() {
		
		log.info("truncate footballStanding table");
		footballStandingDao.truncate();
		
		for(FootballLeague league : FootballLeague.values()) {
			if (FootballLeague.TYPE.LEAGUE.equals(league.getType())) {
				//FootballSeason season = footballSeasonDao.getLatestSeason(league);
				for(FootballSeason season : footballSeasonDao.getSeasons(league, 3)) {			
				footballStandingDto.getLeagueStanding(season.getSeasonId(), league.getId()).forEach(fl -> {
					fl.setFootballStandingId(new FootballStandingId(league.getId(), season.getSeasonId(), fl.getFootballStandingId().getTeam()));
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

		List<FootballTeam> set = footballTeamDto.getAllTeams();

		log.info("insert records into footballteam table");
		for(FootballTeam t : set) {
			footballTeamDao.persist(t);
		}
	}

	public void executeRefreshFootballLeague() {
		log.info("truncate footballSeason table");
		footballSeasonDao.truncate();

		for(FootballLeague league : FootballLeague.values()) {
			if(league.getType().equals(FootballLeague.TYPE.LEAGUE)) {
				List<FootballSeason> seasonList = footballLeagueDto.getAllSeasons(league);

				seasonList.forEach( (season) -> {
					String rank1Team = footballTeamDto.getTeamNamesByLeague(league, season).get(0);
					List<FootballMatch> l = footballMatchDto.getAllMatchResults(league, season, footballTeamDao.getByName(rank1Team));
					if(!FootballMatch.MATCH_SEASON.CURRENT.getLabel().equals(season.getSeason())) season.setEnd(l.get(0).getMatchDate());
					if(l.size() > 0) season.setStart(l.get(l.size() - 1).getMatchDate());
					log.info("insert season: {}", season);
					footballSeasonDao.persist(season);
				});
			}
		}
	}
}
