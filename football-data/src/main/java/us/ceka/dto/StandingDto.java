package us.ceka.dto;

import java.util.List;

import us.ceka.domain.Standing;

public interface StandingDto extends AbstractDto<Standing>{
	public List<Standing> getLeagueStanding(String seasonId, String leagueId);
}
