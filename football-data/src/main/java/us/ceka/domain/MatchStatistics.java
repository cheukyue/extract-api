package us.ceka.domain;

import java.io.Serializable;

public class MatchStatistics implements Serializable{

	private static final long serialVersionUID = -3226693772898736145L;
	private int homeShot;
	private int awayShot;
	private int homeShotOnTarget;
	private int awayShotOnTarget;
	private int homePossesion;
	private int awayPossesion;
	
	public int getHomeShot() {
		return homeShot;
	}
	public void setHomeShot(int homeShot) {
		this.homeShot = homeShot;
	}
	public int getAwayShot() {
		return awayShot;
	}
	public void setAwayShot(int awayShot) {
		this.awayShot = awayShot;
	}
	public int getHomeShotOnTarget() {
		return homeShotOnTarget;
	}
	public void setHomeShotOnTarget(int homeShotOnTarget) {
		this.homeShotOnTarget = homeShotOnTarget;
	}
	public int getAwayShotOnTarget() {
		return awayShotOnTarget;
	}
	public void setAwayShotOnTarget(int awayShotOnTarget) {
		this.awayShotOnTarget = awayShotOnTarget;
	}
	public int getHomePossesion() {
		return homePossesion;
	}
	public void setHomePossesion(int homePossesion) {
		this.homePossesion = homePossesion;
	}
	public int getAwayPossesion() {
		return awayPossesion;
	}
	public void setAwayPossesion(int awayPossesion) {
		this.awayPossesion = awayPossesion;
	}

}
