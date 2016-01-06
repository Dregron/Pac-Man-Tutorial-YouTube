package com.dregronprogram.game_state;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.utils.TickTimer;

public class PowerUp implements Renderer {

	private int xPos, yPos, width, height;
	private BufferedImage image;
	private TickTimer flash = new TickTimer(8);
	private boolean toggle = false;
	
	public PowerUp(int xPos, int yPos, int width, int height, BufferedImage image) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.image = image;
	}
	
	@Override
	public void update(double delta) {
		flash.tick(delta);
	}

	@Override
	public void draw(Graphics2D g) {
		if (flash.isEventReady()) {
			toggle = !toggle;
		}
		
		if (toggle) {
			g.drawImage(getImage(), getxPos()-(getImage().getWidth()/2), getyPos()-(getImage().getHeight()/2), getImage().getWidth(), getImage().getHeight(), null);
		} else {
			
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
