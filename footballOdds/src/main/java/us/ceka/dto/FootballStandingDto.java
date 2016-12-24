package us.ceka.dto;

import java.util.List;

import us.ceka.domain.FootballStanding;

public interface FootballStandingDto extends AbstractDto<FootballStanding>{
	public List<FootballStanding> getLeagueStanding(String seasonId, String leagueId);
}
