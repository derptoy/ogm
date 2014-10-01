package cz.roller.game.level;

import java.util.List;

public class Level {
	
	private String name;
	private String icon;
	private float startx;
	private float starty;
	private float[] time;
	private int earnedStars;
	private int trackSplines;
	private float trackWidth;
	private int levelWidth;
	private float flagX;
	private float flagY;
	private List<Float> track;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconUrl() {
		return icon;
	}
	public void setIconUrl(String iconUrl) {
		this.icon = iconUrl;
	}
	public float getStartx() {
		return startx;
	}
	public void setStartx(float startx) {
		this.startx = startx;
	}
	public float getStarty() {
		return starty;
	}
	public void setStarty(float starty) {
		this.starty = starty;
	}
	public int getEarnedStars() {
		return earnedStars;
	}
	public void setEarnedStars(int earnedStars) {
		this.earnedStars = earnedStars;
	}
	public List<Float> getTrack() {
		return track;
	}
	public void setTrack(List<Float> track) {
		this.track = track;
	}
	public int getTrackSplines() {
		return trackSplines;
	}
	public void setTrackSplines(int trackSplines) {
		this.trackSplines = trackSplines;
	}
	public float getTrackWidth() {
		return trackWidth;
	}
	public void setTrackWidth(float trackWidth) {
		this.trackWidth = trackWidth;
	}
	public int getLevelWidth() {
		return levelWidth;
	}
	public void setLevelWidth(int levelWidth) {
		this.levelWidth = levelWidth;
	}
	public float getFlagX() {
		return flagX;
	}
	public void setFlagX(float flagX) {
		this.flagX = flagX;
	}
	public float getFlagY() {
		return flagY;
	}
	public void setFlagY(float flagY) {
		this.flagY = flagY;
	}
	public float[] getTimes() {
		return time;
	}
	public void setTimes(float[] times) {
		this.time = times;
	}

	
}
