package us.ceka.dao;

import us.ceka.domain.Team;

public interface TeamDao extends AbstractDao<String, Team>{
	
	public void truncate();
	public Team getByName(String name);
}
