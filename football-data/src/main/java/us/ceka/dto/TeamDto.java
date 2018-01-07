package us.ceka.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import us.ceka.domain.League;
import us.ceka.domain.Season;
import us.ceka.domain.Team;

public interface TeamDto extends AbstractDto<Team> {
	public List<String> getTeamNamesByLeague(League league, Season season);
	public List<Team> getAllTeams();
	public Map<String, Object> getResult(LocalDateTime fromDate, LocalDateTime toDate, String teamId);
	public Map<String, Object> getResult(LocalDateTime date, String teamId);
}
