package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="team_name")
public class TeamName extends AbstractObject<TeamName> implements Serializable{

	private static final long serialVersionUID = 1046358271812446819L;
	
	@Id
	@Column(name="team_id")
	private String id;

	@Column(name="name", nullable=false)
	private String name;
	
	@Basic
	@Column(name="league_id")
	@Convert( converter=LeagueConverter.class )
	private League leagueId;
	
	@Column(name="league")
	private String league;
	
	@Column(name="teamfeed")
	private String teamfeed;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public League getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(League leagueId) {
		this.leagueId = leagueId;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getTeamfeed() {
		return teamfeed;
	}

	public void setTeamfeed(String teamfeed) {
		this.teamfeed = teamfeed;
	}

}