package com.dregronprogram.utils;

import static com.dregronprogram.utils.MathUtils.isEqual;

public class Vector2 {

	private int xPos, yPos, xTolerances, yTolerances;

	public Vector2(int xTolerances, int yTolerances) {
		this.xTolerances = xTolerances;
		this.yTolerances = yTolerances;
	}
	
	public Vector2(int xPos, int yPos, int xTolerances, int yTolerances) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.xTolerances = xTolerances;
		this.yTolerances = yTolerances;
	}
	
	public void set(int xPos, int yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public int getxPos() {
		return xPos;
	}
	
	public int getyPos() {
		return yPos;
	}
	
	public int getXTolerances() {
		return xTolerances;
	}
	
	public int getYTolerances() {
		return yTolerances;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Vector2) {
			Vector2 equalObj = (Vector2) obj;
			return isEqual(getxPos(), equalObj.getxPos(), getXTolerances()) && isEqual(getyPos(), equalObj.getyPos(), getYTolerances());
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
