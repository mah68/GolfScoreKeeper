package com.mikedougandnixon.scorekeeper;

import java.util.ArrayList;
import java.util.List;

public class Player {
	private String myName;
	private int[] scoreList;
	
	
	public Player(String name) {
		myName = name;
		scoreList = new int[18];
	}
	
	public void setScore(Integer hole, Integer score) {
		scoreList[hole-1] = score;
	}
	
	public int getScore(Integer hole) {
		return scoreList[hole-1];
	}
	
	public Integer getTotalScore() {
		Integer total = 0;
		for (int i=0; i<scoreList.length; i++) {
			total += scoreList[i];
		}
		return total;
	}
	
	public Integer getScoreAtHole(Integer hole) {
		return scoreList[hole];
	}
	
	public String getName() {
		return myName;
	}
	
	
}
