package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="team_url")
public class TeamURL extends AbstractObject<TeamURL> implements Serializable{

	private static final long serialVersionUID = -3869784298012313906L;
	
	@Id
	@Column(name="team_id")
	private String teamId;
	
	@Column(name="team")
	private String team;
	
	@Column(name="team_url")
	private String teamURL;
	
	@Column(name="player_url")
	private String playerURL;

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public String getTeamURL() {
		return teamURL;
	}

	public void setTeamURL(String teamURL) {
		this.teamURL = teamURL;
	}

	public String getPlayerURL() {
		return playerURL;
	}

	public void setPlayerURL(String playerURL) {
		this.playerURL = playerURL;
	}

}
