package us.ceka.dao.impl;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import us.ceka.dao.TeamDao;
import us.ceka.domain.Team;

@Repository("teamDao")
public class TeamDaoImpl extends BaseDaoImpl<String, Team> implements TeamDao {
	
	public void truncate() {
		int rowsUpdated = getSession().createNativeQuery("truncate table team").executeUpdate();
		log.info("No. of rows affected in truncate query: {}", rowsUpdated);
	}
	
	public Team getByName(String name) {
		Query query = getSession().createQuery("FROM Team t where t.name = :name");
		query.setParameter("name", name);
		return query.getResultList().isEmpty() ? null : (Team) query.getSingleResult();  
	}
}
