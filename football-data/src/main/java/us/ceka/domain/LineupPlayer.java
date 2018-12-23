package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="lineup_player")
public class LineupPlayer extends AbstractObject<Lineup> implements Serializable{
	public enum SIDE { HOME, AWAY }

	private static final long serialVersionUID = -2391954271020840310L;
	
	@Id
	@GeneratedValue(strategy= GenerationType.AUTO, generator="native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name="id")
	private int id;
	
	@Column(name="lineup_id")
	private int lineupId;
	
	@Enumerated(EnumType.STRING)
	private SIDE side;
	
	@Column(name="player_no")
	private int playerNo;

	@ManyToOne
	@JoinColumn(name = "lineup_id", nullable = false)
	@MapsId("lineupId")
	private Lineup lineup;
	
	@Column(name="name", nullable=false)
	private String name;
	
	@Column(name="position", nullable=false)
	private String position;
	
	@Column(name="team", nullable=false)
	private String team;

	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	public Lineup getLineup() {
		return lineup;
	}
	public void setLineup(Lineup lineup) {
		this.lineup = lineup;
	}
	public int getLineupId() {
		return lineupId;
	}
	public void setLineupId(int lineupId) {
		this.lineupId = lineupId;
	}
	public SIDE getSide() {
		return side;
	}
	public void setSide(SIDE side) {
		this.side = side;
	}
	public int getPlayerNo() {
		return playerNo;
	}
	public void setPlayerNo(int playerNo) {
		this.playerNo = playerNo;
	}

}
