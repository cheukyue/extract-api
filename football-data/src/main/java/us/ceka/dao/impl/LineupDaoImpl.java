package us.ceka.dao.impl;

import java.time.LocalDateTime;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.LineupDao;
import us.ceka.domain.Lineup;

@Repository("lineupDao")
public class LineupDaoImpl extends BaseDaoImpl<Integer, Lineup> implements LineupDao {
	
	public Lineup getByDateTeam(LocalDateTime matchDate, String homeTeam, String awayTeam) {
		Query q = getSession().createQuery("from Lineup l where l.matchDate >= :matchDate and l.matchDate <= :matchDateAdj and l.homeTeam = :homeTeam and l.awayTeam = :awayTeam");
		q.setParameter("matchDate", matchDate.minusHours(1));
		q.setParameter("matchDateAdj", matchDate.plusHours(1));
		q.setParameter("homeTeam", homeTeam);
		q.setParameter("awayTeam", awayTeam);
		return q.getResultList().isEmpty() ? null : (Lineup)q.getSingleResult();
	}

}
