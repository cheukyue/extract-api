package us.ceka.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.SeasonDao;
import us.ceka.domain.League;
import us.ceka.domain.Season;

@Repository("seasonDao")
public class SeasonDaoImpl extends BaseDaoImpl<String, Season> implements SeasonDao{
	
	public void truncate() {
		int rowsUpdated = getSession().createNativeQuery("truncate table season").executeUpdate();
		log.info("No. of rows affected in truncate query: {}", rowsUpdated);
	}
	
	public Season getLatestSeason(League league) {
		Query q = getSession().createQuery("from Season s where s.season = (select max(fs.season) from Season fs where fs.leagueId = :league) and s.leagueId = :league");
		q.setParameter("league", league);
		return q.getResultList().isEmpty() ? null : (Season)q.getSingleResult(); 
	}
	
	@SuppressWarnings("unchecked")
	public List<Season> getSeasons(League league, int limit) {
		Query q =  getSession().createQuery("from Season s where s.leagueId = :league order by season desc");
		q.setParameter("league", league);
		q.setMaxResults(limit);
		return q.getResultList();
	}	 
	
}
