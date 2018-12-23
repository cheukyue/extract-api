package us.ceka.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import us.ceka.util.BooleanStringConverter;

@Entity
@Table(name="lineup")
public class Lineup extends AbstractObject<Lineup> implements Serializable{

	private static final long serialVersionUID = 978245711915760299L;

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id")
	private int id;
	
	@Column(name="match_date", nullable=false)
	private LocalDateTime matchDate;
	
	@Column(name="home_team", nullable=false)
	private String homeTeam;
	
	@Column(name="away_team", nullable=false)
	private String awayTeam;
	
	@Column(name="league", nullable=false)
	private String league;
	
	@Column(name="season", nullable=false)
	private String season;
	
	@Column(name="home_formation", nullable=false)
	private String homeFormation;
	
	@Column(name="away_formation", nullable=false)
	private String awayFormation;
	
	@Column(name="home_lineup")
	private String homeLineup;
	
	@Column(name="home_lineup_no")
	private String homeLineupNo;
	
	@Column(name="away_lineup")
	private String awayLineup;
	
	@Column(name="away_lineup_no")
	private String awayLineupNo;
	@Column(name="home_subs")
	private String homeSubstitution;
	@Column(name="home_subs_no")
	private String homeSubstitutionNo;
	@Column(name="away_subs")
	private String awaySubstitution;
	@Column(name="away_subs_no")
	private String awaySubstitutionNo;
	
	@Column(name="finalized")
	@Convert(converter=BooleanStringConverter.class)
	private Boolean finalized;

	@Column(name="match_id")
	private String matchId;
	
	@OneToMany(mappedBy="lineup")
	private Set<LineupPlayer> players;

	public String getLeague() {
		return league;
	}

	public void setLeague(String league) {
		this.league = league;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	public String getHomeFormation() {
		return homeFormation;
	}

	public void setHomeFormation(String homeFormation) {
		this.homeFormation = homeFormation;
	}

	public String getAwayFormation() {
		return awayFormation;
	}

	public void setAwayFormation(String awayFormation) {
		this.awayFormation = awayFormation;
	}

	public String getHomeLineup() {
		return homeLineup;
	}

	public void setHomeLineup(String homeLineup) {
		this.homeLineup = homeLineup;
	}

	public String getHomeLineupNo() {
		return homeLineupNo;
	}

	public void setHomeLineupNo(String homeLineupNo) {
		this.homeLineupNo = homeLineupNo;
	}

	public String getAwayLineup() {
		return awayLineup;
	}

	public void setAwayLineup(String awayLineup) {
		this.awayLineup = awayLineup;
	}

	public String getAwayLineupNo() {
		return awayLineupNo;
	}

	public void setAwayLineupNo(String awayLineupNo) {
		this.awayLineupNo = awayLineupNo;
	}
	public String getHomeSubstitution() {
		return homeSubstitution;
	}

	public void setHomeSubstitution(String homeSubstitution) {
		this.homeSubstitution = homeSubstitution;
	}

	public String getHomeSubstitutionNo() {
		return homeSubstitutionNo;
	}

	public void setHomeSubstitutionNo(String homeSubstitutionNo) {
		this.homeSubstitutionNo = homeSubstitutionNo;
	}

	public String getAwaySubstitution() {
		return awaySubstitution;
	}

	public void setAwaySubstitution(String awaySubstitution) {
		this.awaySubstitution = awaySubstitution;
	}

	public String getAwaySubstitutionNo() {
		return awaySubstitutionNo;
	}

	public void setAwaySubstitutionNo(String awaySubstitutionNo) {
		this.awaySubstitutionNo = awaySubstitutionNo;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(LocalDateTime matchDate) {
		this.matchDate = matchDate;
	}

	public String getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		this.homeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		this.awayTeam = awayTeam;
	}

	public Set<LineupPlayer> getPlayers() {
		return players;
	}

	public void setPlayers(Set<LineupPlayer> players) {
		this.players = players;
	}

	public Boolean isFinalized() {
		return finalized;
	}

	public void setFinalized(Boolean finalized) {
		this.finalized = finalized;
	}

	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof Lineup)) return false;
		
		Lineup lineup = (Lineup)obj;
		if(lineup !=null) {
			if(!lineup.getMatchDate().equals(matchDate)) return false;
			if(!lineup.getHomeTeam().equals(homeTeam)) return false;
			if(!lineup.getAwayTeam().equals(awayTeam)) return false;
			
			if(!lineup.getHomeFormation().equals(homeFormation)) return false;
			if(!lineup.getAwayFormation().equals(awayFormation)) return false;
			if(!lineup.getHomeLineup().equals(homeLineup)) return false;
			if(!lineup.getAwayLineup().equals(awayLineup)) return false;
			
			if(!lineup.isFinalized().equals(finalized)) return false;
		}

		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		if(matchDate !=null) hashCode = 31 * hashCode + matchDate.hashCode();
		if(homeTeam != null) hashCode = 31 * hashCode + homeTeam.hashCode();
		if(awayTeam != null) hashCode = 31 * hashCode + awayTeam.hashCode();
		
		if(homeFormation != null) hashCode = 31 * hashCode + homeFormation.hashCode();
		if(awayFormation != null) hashCode = 31 * hashCode + awayFormation.hashCode();
		if(homeLineup != null) hashCode = 31 * hashCode + homeLineup.hashCode();
		if(awayLineup != null) hashCode = 31 * hashCode + awayLineup.hashCode();
		
		if(finalized!= null) hashCode = 31 * hashCode + finalized.hashCode();
		
		return hashCode;
	}

}
