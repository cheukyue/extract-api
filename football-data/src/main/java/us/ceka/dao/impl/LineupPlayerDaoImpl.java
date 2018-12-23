package us.ceka.dao.impl;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.LineupPlayerDao;
import us.ceka.domain.LineupPlayer;

@Repository("lineupPlayerDao")
public class LineupPlayerDaoImpl extends BaseDaoImpl<Integer, LineupPlayer> implements LineupPlayerDao {
	
	@SuppressWarnings("unchecked")
	public List<LineupPlayer> getPlayersByLineupId(int lineupId) {
		Query q = getSession().createQuery("from LineupPlayer lp where lp.lineupId = :id");
		q.setParameter("id", lineupId);
		return q.getResultList();
	}

}
