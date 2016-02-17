package com.dregronprogram.game_state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.utils.TickTimer;

public class Block implements Renderer {

	private int xPos, yPos, width, height;
	private BufferedImage image;
	private TickTimer tickTimer = new TickTimer(10);
	private boolean toggle = false;
	
	public Block(int xPos, int yPos, int width, int height, BufferedImage image) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.image = image;
	}
	
	@Override
	public void update(double delta) {
		tickTimer.tick(delta);
	}

	@Override
	public void draw(Graphics2D g) {
		if (toggle) {
			g.setXORMode(Color.LIGHT_GRAY);
			g.drawImage(getImage(), getxPos(), getyPos(), getWidth(), getHeight(), null);
			g.setPaintMode();
		} else {
			g.drawImage(getImage(), getxPos(), getyPos(), getWidth(), getHeight(), null);
		}
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
