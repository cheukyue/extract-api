package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="player")
public class Player extends AbstractObject<Player> implements Serializable{

	private static final long serialVersionUID = 6664334472226337155L;
	
	@EmbeddedId
	private PlayerId footballPlayerId;

	@Column(name="SEASON", nullable=false)
	private String season;
	
	@Column(name="LEAGUE_ID")
	private String leagueId;
	
	@Column(name="LEAGUE")
	private String league;
	
	@Column(name="TEAM", nullable=false)
	private String team;
	
	@Column(name="NAME", nullable=false)
	private String name;
	
	@Column(name="NAME_2ND_LANG")
	private String nameIn2ndLanguage;
	
	@Column(name="POSITION")
	private String position;
	
	@Column(name="ORIGIN")
	private String origin;
	
	@Column(name="MATCH_PLAYED", nullable=false)
	private int matchPlayed;
	
	@Column(name="LINEUP")
	private int lineup;
	
	@Column(name="MIN_PLAYED")
	private int minPlayed;
	
	@Column(name="OFFENSE")
	private float offense;
	
	@Column(name="PASS")
	private float pass;
	
	@Column(name="DRIBBLE")
	private float dribble;
	
	@Column(name="CROSS")
	private float cross;
	
	@Column(name="SHOT")
	private float shot;
	
	@Column(name="DEFENSE")
	private float defense;
	
	@Column(name="SAVE")
	private float save;
	
	@Column(name="YELLOW_CARD")
	private int yellow_card;
	
	@Column(name="RED_CARD")
	private int red_card;
	
	@Column(name="GOAL", nullable=false)
	private int goal;
	
	public Player() {}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameIn2ndLanguage() {
		return nameIn2ndLanguage;
	}
	public void setNameIn2ndLanguage(String nameIn2ndLanguage) {
		this.nameIn2ndLanguage = nameIn2ndLanguage;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public int getGoal() {
		return goal;
	}
	public void setGoal(int goal) {
		this.goal = goal;
	}
	public PlayerId getFootballPlayerId() {
		return footballPlayerId;
	}
	public void setFootballPlayerId(PlayerId footballPlayerId) {
		this.footballPlayerId = footballPlayerId;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getSeason() {
		return season;
	}
	public void setSeason(String season) {
		this.season = season;
	}
	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}

	public String getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(String leagueId) {
		this.leagueId = leagueId;
	}

}
