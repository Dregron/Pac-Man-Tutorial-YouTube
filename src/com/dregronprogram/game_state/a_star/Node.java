package com.dregronprogram.game_state.a_star;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.LinkedList;
import java.util.List;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.game_state.GameState;

public class Node implements Renderer {
	
	private String name;
	private int xPos, yPos, width, height;
	private List<Node> adjacentNode = new LinkedList<Node>();
	private List<Node> node = new LinkedList<Node>();
	private int neighborX, neighborY;
	private Color colour;
	private int g_score = Integer.MAX_VALUE, f_score = Integer.MAX_VALUE;
	
	public Node(int xPos, int yPos, int width, int height, String name) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.name = name;
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
	
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	
	public int getxPos() {
		return xPos;
	}
	
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	
	public int getyPos() {
		return yPos;
	}
	
	public void setAdjacentNode(List<Node> adjacentNode) {
		this.adjacentNode = adjacentNode;
	}
	
	public List<Node> getAdjacentNode() {
		return adjacentNode;
	}
	
	public List<Node> getPrevNodesNode() {
		return node;
	}
	
	public void setNeighborX(int neighborX) {
		this.neighborX = neighborX;
	}
	
	public int getNeighborX() {
		return neighborX;
	}
	
	public void setNeighborY(int neighborY) {
		this.neighborY = neighborY;
	}
	
	public int getNeighborY() {
		return neighborY;
	}

	public void clear() {
		node.clear();
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
		g.setColor(Color.WHITE);
		g.drawString(getxPos() + "", getxPos(), getyPos());
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
}
