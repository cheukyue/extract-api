package us.ceka.dto;

import java.time.LocalDateTime;
import java.util.List;

import us.ceka.domain.Lineup;

public interface LineupDto extends AbstractDto<Lineup>{
	public List<Lineup> getHistoricLineupList(String team, LocalDateTime dateSince);
	public List<Lineup> getLineupList(String team);
}
