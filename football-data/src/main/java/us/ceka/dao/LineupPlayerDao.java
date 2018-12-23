package us.ceka.dao;

import java.util.List;

import us.ceka.domain.LineupPlayer;

public interface LineupPlayerDao extends AbstractDao<Integer, LineupPlayer>{
	public List<LineupPlayer> getPlayersByLineupId(int lineupId);

}
