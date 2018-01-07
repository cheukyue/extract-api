package us.ceka.dto;

import java.util.List;
import java.util.Map;

import us.ceka.domain.League;
import us.ceka.domain.Match;
import us.ceka.domain.Season;
import us.ceka.domain.Team;

public interface MatchDto extends AbstractDto<Match>{
	public List<Match> getLatestMatchesByLeauge(League league);
	public Map<League, List<Match>> getAllLatestMatches();
	public Map<League, List<Match>> getLatestMatches(League... league);
	public Map<String, Object> getMatchUpStat(Match footballMatch);
	public List<Match> getAllMatchResults(League league, Season season, Team team);
}
