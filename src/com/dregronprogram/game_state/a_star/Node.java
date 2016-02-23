package com.dregronprogram.game_state.a_star;

import java.awt.Color;
import java.awt.Graphics2D;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.game_state.GameState;

public class Node implements Renderer {
	
	private String name;
	private int xPos, yPos, width, height;
	private Color colour;
	private int g_score = Integer.MAX_VALUE, f_score = Integer.MAX_VALUE;
	private boolean adjacentLeftFloor, adjacentRightFloor;
	
	public Node(int xPos, int yPos, int width, int height, String name) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.name = name;
		this.adjacentLeftFloor = false;
		this.adjacentRightFloor = false;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getWidth() {
		return width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getxPos() {
		return xPos;
	}

	public int getyPos() {
		return yPos;
	}
	
	public void clearColour() {
		colour = null;
	}

	public String getName() {
		return name;
	}

	@Override
	public void update(double delta) {
		
	}

	@Override
	public void draw(Graphics2D g) {
		if (getColour() != null) {
			g.setColor(getColour());
			g.fillRect(xPos, yPos, width, height);
		}
	}
	
	public void setColour(Color colour) {
		if (!GameState.debugMode) return;
		this.colour = colour;
	}
	
	public Color getColour() {
		return colour;
	}
	
	public void setF_score(int f_score) {
		this.f_score = f_score;
	}
	
	public int getF_score() {
		return f_score;
	}
	
	public void setG_score(int g_score) {
		this.g_score = g_score;
	}
	
	public int getG_score() {
		return g_score;
	}
	
	public void setAdjacentLeftFloor(boolean adjacentLeftFloor) {
		this.adjacentLeftFloor = adjacentLeftFloor;
	}
	
	public boolean isAdjacentLeftFloor() {
		return adjacentLeftFloor;
	}
	
	public void setAdjacentRightFloor(boolean adjacentRightFloor) {
		this.adjacentRightFloor = adjacentRightFloor;
	}
	
	public boolean isAdjacentRightFloor() {
		return adjacentRightFloor;
	}
}
