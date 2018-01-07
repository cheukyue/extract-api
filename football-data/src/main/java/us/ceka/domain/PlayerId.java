package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;

public class PlayerId extends AbstractObject<PlayerId> implements Serializable{

	private static final long serialVersionUID = 3494953747183895664L;
	
	@Column(name="SEASON_ID", nullable=false)
	private String seasonId;
	
	@Column(name="TEAM_ID", nullable=false)	
	private String teamId;
	
	@Column(name="PLAYER_NO", nullable=false)
	private int playerNo;
	
	public PlayerId () {};
	public PlayerId (String seasonId, String teamId, int playerNo) {
		this.seasonId = seasonId;
		this.teamId = teamId;
		this.playerNo = playerNo;
	};

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public int getPlayerNo() {
		return playerNo;
	}

	public void setPlayerNo(int playerNo) {
		this.playerNo = playerNo;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	} 
	
	@Override
	public boolean equals (Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(! (obj instanceof PlayerId)) return false;
		PlayerId id = (PlayerId)obj;
		if(id.getSeasonId() == null  || id.getTeamId() == null || id.getPlayerNo() == 0) return false;
		if(!id.getSeasonId().equals(seasonId)) return false;
		if(!id.getTeamId().equals(teamId)) return false;
		if(id.getPlayerNo() != playerNo) return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		if(seasonId != null) hashCode = 31 * hashCode + seasonId.hashCode();
		if(teamId != null) hashCode = 31 * hashCode + teamId.hashCode();
		hashCode = 31 * hashCode + playerNo;
		return hashCode;
	}

}
