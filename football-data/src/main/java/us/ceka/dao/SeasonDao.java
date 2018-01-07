package us.ceka.dao;

import java.util.List;

import us.ceka.domain.League;
import us.ceka.domain.Season;

public interface SeasonDao extends AbstractDao<String, Season>{
	public void truncate();
	public Season getLatestSeason(League league);
	public List<Season> getSeasons(League league, int limit);
}
