package com.dregronprogram.utils;

import static com.dregronprogram.utils.MathUtils.isEqual;

public class Vector2 {

	private int xPos, yPos, width, height;
	
	public Vector2(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Vector2(int xPos, int yPos, int width, int height) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
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
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector2) {
			Vector2 equalObj = (Vector2) obj;
			return isEqual(getxPos(), equalObj.getxPos(), getWidth()) && isEqual(getyPos(), equalObj.getyPos(), getHeight());
		}
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return 1;
	}
	
	
	@Override
	public String toString() {
		return getxPos() + ", " + getyPos();
	}
}
