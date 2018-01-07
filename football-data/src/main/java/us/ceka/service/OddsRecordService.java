package us.ceka.service;


public interface OddsRecordService extends GenericService {

	public void executeRecordOdds();
	public void executeUpdateMatchResult();
	public void executeUpdateTeamMatches();
	public void executeUpdateTeamMatchesWithOdds();
}
