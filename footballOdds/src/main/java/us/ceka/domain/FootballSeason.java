package us.ceka.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="football_season")
public class FootballSeason extends AbstractObject<FootballSeason> implements Serializable{

	private static final long serialVersionUID = -4398738267182339700L;

	@Id
	@Column(name="SEASON_ID", nullable=false)
	private String seasonId;
	
	@Column(name="SEASON", nullable=false)
	private String season;
	
	@Basic
	@Column(name="LEAGUE_ID")
	@Convert( converter=FootballLeagueConverter.class )
	private FootballLeague leagueId;
	
	@Column(name="LEAGUE")
	private String league;
	
	@Column(name="LEAGUE_NAME")
	private String leagueName;
	
	@Column(name="START")
	private LocalDateTime start;
	
	@Column(name="END")
	private LocalDateTime end;
	
	public FootballSeason() {}

	public String getSeasonId() {
		return seasonId;
	}

	public void setSeasonId(String seasonId) {
		this.seasonId = seasonId;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getLeagueName() {
		return leagueName;
	}

	public void setLeagueName(String leagueName) {
		this.leagueName = leagueName;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public FootballLeague getLeagueId() {
		return leagueId;
	}

	public void setLeagueId(FootballLeague leagueId) {
		this.leagueId = leagueId;
	}

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

}
