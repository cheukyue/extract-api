package us.ceka.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import us.ceka.domain.LineupPlayer.SIDE;

@Embeddable
public class LineupPlayerId extends AbstractObject<LineupPlayerId> implements Serializable{
	/*
	private static final long serialVersionUID = 6546507702235238593L;

	@Column(name="lineup_id")
	private int lineupId;
	
	@Enumerated(EnumType.STRING)
	private SIDE side;
	
	@Column(name="player_no")
	private int playerNo;
	
	public LineupPlayerId() {}
	
	public LineupPlayerId(int lineupId, SIDE side, int playerNumber) {
		this.lineupId = lineupId;
		this.side = side;
		this.playerNo = playerNumber;
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
	public int getPlayerNumber() {
		return playerNo;
	}
	public void setPlayerNumber(int playerNumber) {
		this.playerNo = playerNumber;
	}



	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(!(obj instanceof Match)) return false;
		
		LineupPlayerId id= (LineupPlayerId)obj;
		if(id !=null) {
			if(id.getLineupId() != lineupId) return false;
			if(!id.getSide().equals(side)) return false;
			if(id.getPlayerNumber() != playerNo) return false;
		}

		return true;
	}
	
	@Override
	public int hashCode() {
		int hashCode = 1;
		hashCode = 31 * hashCode + lineupId;
		if(side != null) hashCode = 31 * hashCode + side.hashCode();
		hashCode = 31 * hashCode + playerNo;
		
		return hashCode;
	}
	*/
}
