package us.ceka.dto;

import us.ceka.domain.MatchStatistics;

public interface MatchStatisticsDto extends AbstractDto<MatchStatistics>{
	public MatchStatistics getMatchStat(String matchId);
}
