package us.ceka.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.OddsDao;
import us.ceka.domain.Odds;
import us.ceka.domain.OddsId;

@Repository("oddsDao")
public class OddsDaoImpl extends BaseDaoImpl<OddsId, Odds> implements OddsDao {
	
	public Odds findById(OddsId id) {
		return getByKey(id);
	}
	
	public Odds findRecentOddsRecord(String matchId) {
		Query q = getSession().createQuery("from Odds f where f.oddsId.matchId = :matchId and f.dateCreated = (select max(f.dateCreated) from Odds f where f.oddsId.matchId = :matchId)" );
		q.setParameter("matchId", matchId);
		return q.getResultList().isEmpty() ? null : (Odds)q.getSingleResult();
	}
	
	public Odds findInitialOddsRecord(String matchId) {
		Query q = getSession().createQuery("from Odds f where f.oddsId.matchId = :matchId and f.dateCreated = (select min(f.dateCreated) from Odds f where f.oddsId.matchId = :matchId)" );
		q.setParameter("matchId", matchId);
		return q.getResultList().isEmpty() ? null : (Odds)q.getSingleResult();
	}
	
}
