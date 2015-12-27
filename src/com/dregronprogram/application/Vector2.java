package com.dregronprogram.application;

public class Vector2 {

	private int xPos, yPos;
	
	public Vector2() {
	}
	
	public Vector2(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public void set(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public int getxPos() {
		return xPos;
	}
	
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	
	public int getyPos() {
		return yPos;
	}
	
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	
	@Override
	public String toString() {
		return getxPos() + ", " + getyPos();
	}
}
