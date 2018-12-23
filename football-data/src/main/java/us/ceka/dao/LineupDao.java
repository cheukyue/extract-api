package us.ceka.dao;

import java.time.LocalDateTime;

import us.ceka.domain.Lineup;

public interface LineupDao extends AbstractDao<Integer, Lineup>{

	public Lineup getByDateTeam(LocalDateTime matchDate, String homeTeam, String awayTeam);

}
