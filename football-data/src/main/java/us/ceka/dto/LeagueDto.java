package us.ceka.dto;

import java.util.List;

import us.ceka.domain.League;
import us.ceka.domain.Season;

public interface LeagueDto extends AbstractDto<League>{
	public List<Season> getAllSeasons(League league);

}
