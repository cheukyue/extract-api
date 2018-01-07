package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="standing")
public class Standing extends AbstractObject<Standing> implements Serializable{

	private static final long serialVersionUID = 4833452795415446032L;
	
	public Standing() {}
	
	@EmbeddedId
	private StandingId standingId;
	
	@Column(name="LEAGUE", nullable=false)
	private String league;
	
	@Column(name="SEASON", nullable=false)
	private String season;
	
	@Column(name="team", nullable=false)
	private String team;
	
	@Column(name="RANK")
	private int rank;
	
	@Column(name="PLAYED")
	private int played;
	
	@Column(name="WIN")
	private int win;
	
	@Column(name="DRAW")
	private int draw;
	
	@Column(name="LOSE")
	private int lose;
	
	@Column(name="GOAL_FOR")
	private int goalFor;
	
	@Column(name="GOAL_AGAINST")
	private int goalAgainst;
	
	@Column(name="POINT")
	private int point;
	

	public int getRank() {
		return rank;
	}
	public int getWin() {
		return win;
	}
	public int getDraw() {
		return draw;
	}
	public int getLose() {
		return lose;
	}
	public int getGoalFor() {
		return goalFor;
	}
	public int getGoalAgainst() {
		return goalAgainst;
	}
	public int getPoint() {
		return point;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}
	public void setWin(int win) {
		this.win = win;
	}
	public void setDraw(int draw) {
		this.draw = draw;
	}
	public void setLose(int lose) {
		this.lose = lose;
	}
	public void setGoalFor(int goalFor) {
		this.goalFor = goalFor;
	}
	public void setGoalAgainst(int goalAgainst) {
		this.goalAgainst = goalAgainst;
	}
	public void setPoint(int point) {
		this.point = point;
	}
	public int getPlayed() {
		return played;
	}
	public void setPlayed(int played) {
		this.played = played;
	}

	public String getLeague() {
		return league;
	}

	public String getSeason() {
		return season;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public void setSeason(String season) {
		this.season = season;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public StandingId getFootballStandingId() {
		return standingId;
	}
	public void setFootballStandingId(StandingId footballStandingId) {
		this.standingId = footballStandingId;
	}


	
	

}
