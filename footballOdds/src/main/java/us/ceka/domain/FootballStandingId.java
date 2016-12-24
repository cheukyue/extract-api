package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;


public class FootballStandingId extends AbstractObject<FootballStandingId> implements Serializable{

	private static final long serialVersionUID = -5456862916420599968L;
	
	@Column(name="LEAGUE_ID", nullable=false)
	private String leagueId;
	
	@Column(name="SEASON_ID", nullable=false)
	private String seasonId;
	
	@Column(name="TEAM", nullable=false)	
	private String team;
	
	public FootballStandingId() {};

	public FootballStandingId(String leagueId, String seasonId, String team) {
		this.leagueId = leagueId;
		this.seasonId = seasonId;
		this.team = team;
	}

	public String getLeagueId() {
		return leagueId;
	}

	public String getSeasonId() {
		return seasonId;
	}

	public String getTeam() {
		return team;
	}

	public void setLeagueId(String leagueId) {
		this.leagueId = leagueId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public void setTeam(String team) {
		this.team = team;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(! (obj instanceof FootballStandingId)) return false;
		FootballStandingId id = (FootballStandingId)obj;
		if(id.getSeasonId() == null  || id.getLeagueId() == null || id.getTeam() == null) return false;
		if(!id.getSeasonId().equals(seasonId)) return false;
		if(!id.getLeagueId().equals(leagueId)) return false;
		if(!id.getTeam().equals(team)) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		if(seasonId != null) hashCode = 31 * hashCode + seasonId.hashCode();
		if(leagueId != null) hashCode = 31 * hashCode + leagueId.hashCode();
		if(team != null) hashCode = 31 * hashCode + team.hashCode();
		return hashCode;
	}

}
