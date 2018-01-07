package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class StandingId extends AbstractObject<StandingId> implements Serializable{

	private static final long serialVersionUID = -5456862916420599968L;
	
	@Column(name="LEAGUE_ID", nullable=false)
	private String leagueId;
	
	@Column(name="SEASON_ID", nullable=false)
	private String seasonId;
	
	@Column(name="TEAM_ID", nullable=false)	
	private String teamId;
	
	public StandingId() {};

	public StandingId(String leagueId, String seasonId, String teamId) {
		this.leagueId = leagueId;
		this.seasonId = seasonId;
		this.teamId = teamId;
	}

	public String getLeagueId() {
		return leagueId;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setLeagueId(String leagueId) {
		this.leagueId = leagueId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public void setTeam(String team) {
		this.teamId = team;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(! (obj instanceof StandingId)) return false;
		StandingId id = (StandingId)obj;
		if(id.getSeasonId() == null  || id.getLeagueId() == null || id.getTeamId() == null) return false;
		if(!id.getSeasonId().equals(seasonId)) return false;
		if(!id.getLeagueId().equals(leagueId)) return false;
		if(!id.getTeamId().equals(teamId)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		if(seasonId != null) hashCode = 31 * hashCode + seasonId.hashCode();
		if(leagueId != null) hashCode = 31 * hashCode + leagueId.hashCode();
		if(teamId != null) hashCode = 31 * hashCode + teamId.hashCode();
		return hashCode;
	}

}
