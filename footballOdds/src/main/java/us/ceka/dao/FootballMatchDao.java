package us.ceka.dao;

import java.time.LocalDateTime;
import java.util.List;

import us.ceka.domain.FootballMatch;
import us.ceka.domain.FootballMatchup;

public interface FootballMatchDao extends AbstractDao<String, FootballMatch>{
	public List<FootballMatch> findByStatus(String... status);
	public List<FootballMatch> getLatestMatch();
	public List<FootballMatch> getLastMatches(String team, FootballMatch.MATCH_AT matchAt, int numMatches);
	public FootballMatch getMatch(String hometeam, String awayTeam, LocalDateTime matchDate);
	public List<FootballMatchup> getMatchup(String team, String league, FootballMatch.MATCH_AT matchAt);

}
