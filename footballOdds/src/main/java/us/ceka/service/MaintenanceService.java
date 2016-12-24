package us.ceka.service;

public interface MaintenanceService extends GenericService{
	public void executeRefreshStanding();
	public void executeRefreshTeamTable();
	public void executeRefreshFootballLeague() ;

}
