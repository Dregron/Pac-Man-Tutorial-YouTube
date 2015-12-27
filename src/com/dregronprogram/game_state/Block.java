package com.dregronprogram.game_state;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dregronprogram.application.Renderer;

public class Block implements Renderer {

	private int xPos, yPos, width, height;
	private BufferedImage image;
	
	public Block(int xPos, int yPos, int width, int height, BufferedImage image) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.image = image;
	}
	
	@Override
	public void update(double delta) {
		
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getImage(), getxPos(), getyPos(), getWidth(), getHeight(), null);
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

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
}
