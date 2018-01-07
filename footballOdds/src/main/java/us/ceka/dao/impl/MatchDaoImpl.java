package us.ceka.dao.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.FootballMatchDao;
import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballMatchup;

@Repository("footballMatchDao")
public class FootballMatchDaoImpl extends FootballDaoImpl<String, FootballMatch> implements FootballMatchDao{
	
	@SuppressWarnings("unchecked")
	public List<FootballMatch> findByStatus(String... status) {
		Query q = getSession().createQuery("from FootballMatch m where m.status in :status", FootballMatch.class);
		q.setParameter("status", Arrays.asList(status));
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FootballMatch> getLatestMatch() {
		Query q = getSession().createQuery("from FootballMatch m where m.matchDate > now() order by m.matchDate");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<FootballMatch> getLastMatches(String team, FootballMatch.MATCH_AT matchAt, int numMatches) {
		String sql = String.format("from FootballMatch m where %s = :team and m.matchDate < now() order by m.matchDate desc", FootballMatch.MATCH_AT.HOME.equals(matchAt) ? "m.homeTeam" : "m.awayTeam");
		Query q = getSession().createQuery(sql);
		q.setParameter("team", team);
		q.setFirstResult(0);
		q.setMaxResults(numMatches - 1);
		return q.getResultList();
	}
	
	public FootballMatch getMatch(String hometeam, String awayTeam, LocalDateTime matchDate) {
		//Query q = getSession().createNativeQuery("select * from football_match where home_team = :home and away_team =:away and DATE(match_date) = :date", FootballMatch.class);
		Query q = getSession().createQuery("from FootballMatch m where m.homeTeam = :home and m.awayTeam = :away and m.matchDate >= :start and m.matchDate < :end and m.status <> :status");
		q.setParameter("home", hometeam);
		q.setParameter("away", awayTeam);
		q.setParameter("start", matchDate.truncatedTo(ChronoUnit.DAYS));
		q.setParameter("end", matchDate.truncatedTo(ChronoUnit.DAYS).plusDays(1));
		q.setParameter("status", FootballMatch.MATCH_STATUS.RESCHEDULED.getCode());
		return q.getResultList().isEmpty() ? null : (FootballMatch)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<FootballMatchup> getMatchup(String team, String league, FootballMatch.MATCH_AT matchAt) {
		if(matchAt == null) return null;
		String namedQuery = null;
		switch(matchAt) {
			case HOME: namedQuery = "home_team_matchup"; break;
			case AWAY: namedQuery = "away_team_matchup"; break;
			case NEUTRAL: return null;
		}
		if(log.isDebugEnabled()) log.debug("call {}('{}', '{}', {});", matchAt, team, league, 2);
		Query q = getSession().createNamedQuery(namedQuery).setParameter("team", team).setParameter("league", league).setParameter("numSeason", 2);
		return q.getResultList();
	}
	
}
