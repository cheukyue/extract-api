package us.ceka.dao.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.MatchDao;
import us.ceka.domain.Match;
import us.ceka.domain.Matchup;

@Repository("matchDao")
public class MatchDaoImpl extends BaseDaoImpl<String, Match> implements MatchDao{
	
	@SuppressWarnings("unchecked")
	public List<Match> findByStatus(String... status) {
		Query q = getSession().createQuery("from Match m where m.status in :status", Match.class);
		q.setParameter("status", Arrays.asList(status));
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Match> getLatestMatch() {
		Query q = getSession().createQuery("from Match m where m.matchDate > now() order by m.matchDate");
		return q.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Match> getLastMatches(String team, Match.MATCH_AT matchAt, int numMatches) {
		String sql = String.format("from Match m where %s = :team and m.matchDate < now() order by m.matchDate desc", Match.MATCH_AT.HOME.equals(matchAt) ? "m.homeTeam" : "m.awayTeam");
		Query q = getSession().createQuery(sql);
		q.setParameter("team", team);
		q.setFirstResult(0);
		q.setMaxResults(numMatches - 1);
		return q.getResultList();
	}
	
	public Match getMatch(String hometeam, String awayTeam, LocalDateTime matchDate) {
		//Query q = getSession().createNativeQuery("select * from matches where home_team = :home and away_team =:away and DATE(match_date) = :date", FootballMatch.class);
		if(log.isDebugEnabled()) log.debug("home[{}], away[{}], start[{}], end[{}], status[{}]", 
				hometeam, awayTeam, matchDate.truncatedTo(ChronoUnit.DAYS), 
				matchDate.truncatedTo(ChronoUnit.DAYS).plusDays(1), Match.MATCH_STATUS.RESCHEDULED.getCode());
		Query q = getSession().createQuery("from Match m where m.homeTeam = :home and m.awayTeam = :away and m.matchDate >= :start and m.matchDate < :end and m.status <> :status");
		q.setParameter("home", hometeam);
		q.setParameter("away", awayTeam);
		q.setParameter("start", matchDate.truncatedTo(ChronoUnit.DAYS));
		q.setParameter("end", matchDate.truncatedTo(ChronoUnit.DAYS).plusDays(1));
		q.setParameter("status", Match.MATCH_STATUS.RESCHEDULED.getCode());
		return q.getResultList().isEmpty() ? null : (Match)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Matchup> getMatchup(String team, String league, Match.MATCH_AT matchAt) {
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
