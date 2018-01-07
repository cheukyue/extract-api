package us.ceka.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.FootballStandingDao;
import us.ceka.domain.FootballStanding;
import us.ceka.domain.FootballStandingId;

@Repository("footballStandingDao")
public class FootballStandingDaoImpl extends FootballDaoImpl<FootballStandingId, FootballStanding> implements FootballStandingDao {
	
	public void truncate() {
		int rowsUpdated = getSession().createNativeQuery("truncate table football_standing").executeUpdate();
		log.info("No. of rows affected in truncate query: {}", rowsUpdated);
	}
	
	public FootballStanding getStanding(String league, String season, String team) {
		Query q = getSession().createQuery("from FootballStanding fs where fs.league = :league and fs.season = :season and fs.footballStandingId.team = :team");
		q.setParameter("league", league);
		q.setParameter("season", season);
		q.setParameter("team", team);
		return q.getResultList().isEmpty() ? null : (FootballStanding)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<FootballStanding> getStanding(String league, String season) {
		Query q = getSession().createQuery("from FootballStanding fs where fs.league = :league and fs.season = :season");
		q.setParameter("league", league);
		q.setParameter("season", season);
		return q.getResultList();
	}

}
