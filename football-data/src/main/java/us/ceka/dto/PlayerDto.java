package us.ceka.dto;

import java.util.List;

import us.ceka.domain.Player;
import us.ceka.domain.Season;
import us.ceka.domain.TeamURL;


public interface PlayerDto extends AbstractDto<Player> {
	public List<Player> getPlayers(Season season, TeamURL url);

}
