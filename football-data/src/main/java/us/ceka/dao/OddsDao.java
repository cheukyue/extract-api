package us.ceka.dao;

import us.ceka.domain.Odds;
import us.ceka.domain.OddsId;

public interface OddsDao extends AbstractDao<OddsId, Odds>{
	public Odds findById(OddsId id);
	public Odds findRecentOddsRecord(String matchId);
	public Odds findInitialOddsRecord(String matchId);
}
