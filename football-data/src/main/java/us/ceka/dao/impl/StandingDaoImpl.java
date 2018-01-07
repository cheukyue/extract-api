package us.ceka.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.StandingDao;
import us.ceka.domain.Standing;
import us.ceka.domain.StandingId;

@Repository("standingDao")
public class StandingDaoImpl extends BaseDaoImpl<StandingId, Standing> implements StandingDao {
	
	public void truncate() {
		int rowsUpdated = getSession().createNativeQuery("truncate table standing").executeUpdate();
		log.info("No. of rows affected in truncate query: {}", rowsUpdated);
	}
	
	public Standing getStanding(String league, String season, String team) {
		Query q = getSession().createQuery("from Standing fs where fs.league = :league and fs.season = :season and fs.team = :team");
		q.setParameter("league", league);
		q.setParameter("season", season);
		q.setParameter("team", team);
		return q.getResultList().isEmpty() ? null : (Standing)q.getSingleResult();
	}
	
	@SuppressWarnings("unchecked")
	public List<Standing> getStanding(String league, String season) {
		Query q = getSession().createQuery("from Standing fs where fs.league = :league and fs.season = :season");
		q.setParameter("league", league);
		q.setParameter("season", season);
		return q.getResultList();
	}

}
