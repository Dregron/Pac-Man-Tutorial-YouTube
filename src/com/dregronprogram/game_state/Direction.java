package com.dregronprogram.game_state;

public enum Direction {

	LEFT, RIGHT, UP, DOWN, STOP;

	private int steps;
	
	public void setSteps(int steps) {
		this.steps = steps;
	}
	
	public int getSteps() {
		return steps;
	}
	
}
