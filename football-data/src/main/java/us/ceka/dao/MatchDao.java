package us.ceka.dao;

import java.time.LocalDateTime;
import java.util.List;

import us.ceka.domain.Match;
import us.ceka.domain.Matchup;


public interface MatchDao extends AbstractDao<String, Match>{
	public List<Match> findByStatus(String... status);
	public List<Match> getLatestMatch();
	public List<Match> getLastMatches(String team, Match.MATCH_AT matchAt, int numMatches);
	public Match getMatch(String hometeam, String awayTeam, LocalDateTime matchDate);
	public List<Matchup> getMatchup(String team, String league, Match.MATCH_AT matchAt);

}
