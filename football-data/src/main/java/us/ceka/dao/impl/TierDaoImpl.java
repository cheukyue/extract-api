package us.ceka.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import us.ceka.dao.TierDao;
import us.ceka.domain.League;
import us.ceka.domain.Tier;

@Repository("tierDao")
public class TierDaoImpl extends BaseDaoImpl<String, Tier> implements TierDao{
	
	public Map<String, Integer> getNumTier(League league) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		getSession().createQuery("select count(1) from Tier ft where ft.league = :league").getResultList();
		return map;
	}
}
