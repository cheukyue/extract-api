package us.ceka.dao;

import java.util.List;

import us.ceka.domain.FootballStanding;
import us.ceka.domain.FootballStandingId;

public interface FootballStandingDao extends AbstractDao<FootballStandingId, FootballStanding>{
	public void truncate();
	public FootballStanding getStanding(String league, String season, String team);
	public List<FootballStanding> getStanding(String league, String season);

}
