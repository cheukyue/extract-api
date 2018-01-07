package us.ceka.dao;

import java.util.List;

import us.ceka.domain.Standing;
import us.ceka.domain.StandingId;

public interface StandingDao extends AbstractDao<StandingId, Standing>{
	public void truncate();
	public Standing getStanding(String league, String season, String team);
	public List<Standing> getStanding(String league, String season);

}
