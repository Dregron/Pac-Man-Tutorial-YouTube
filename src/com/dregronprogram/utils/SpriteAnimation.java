package com.dregronprogram.utils;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class SpriteAnimation {

	private ArrayList<BufferedImage> sprites = new ArrayList<BufferedImage>();
	private AffineTransform at = new AffineTransform();
	private byte currentSprite;

	private boolean loop = false;
	private boolean play = false;
	private boolean destoryAfterAnim = false;

	private TickTimer timer;
	private int animationSpeed;
	private double xPos, yPos, rotation;
	private int width, height;
	private int limit;

	public SpriteAnimation(double xPos, double yPos, int animationSpeed, List<BufferedImage> bufferedImages) {
		this.animationSpeed = animationSpeed;
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = bufferedImages.get(0).getWidth();
		this.height = bufferedImages.get(0).getHeight();
		
		this.sprites.addAll(bufferedImages);
		this.timer = new TickTimer(animationSpeed);
		this.limit = sprites.size() - 1;
	}

	public void draw(Graphics2D g) {
		if (isSpriteAnimDestroyed())
			return;
		
		at.setToIdentity();
		at.rotate(Math.toDegrees(rotation), getxPos() + (width / 2), getyPos() + (height / 2));
		at.translate(getxPos(), getyPos());
		
		g.drawImage(sprites.get(currentSprite), at, null);
	}

	public void update(double delta) {
		if (isSpriteAnimDestroyed())
			return;

		if (loop && !play) {
			loopAnimation();
			timer.tick(delta);
		}
		if (play && !loop) {
			playAnimation();
			timer.tick(delta);
		}
	}

	public void stopAnimation() {
		loop = false;
		play = false;
	}

	public void resetSprite() {
		loop = false;
		play = false;
		currentSprite = 0;
	}

	private void loopAnimation() {
		if (currentSprite == limit && timer.isEventReady()) {
			currentSprite = 0;
		}else if (currentSprite != limit && timer.isEventReady()) {
			currentSprite++;
		} 
	}

	private void playAnimation() {
		if (currentSprite != limit && !isDestoryAfterAnim() && timer.isEventReady()) {
			play = false;
			currentSprite = 0;
		} else if (currentSprite == limit && isDestoryAfterAnim() && timer.isEventReady()) {
			sprites = null;
		}else if (currentSprite != limit && timer.isEventReady()) {
			currentSprite++;
		}
	}
	
	public byte getCurrentSprite() {
		return currentSprite;
	}

	public void setCurrentSprite(byte currentSprite) {
		this.currentSprite = currentSprite;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public boolean isSpriteAnimDestroyed() {
		if (sprites == null)
			return true;

		return false;
	}

	public void setPlay(boolean play, boolean destoryAfterAnim) {
		if(loop) {
			loop = false;
		}
		
		this.play = play;
		this.setDestoryAfterAnim(destoryAfterAnim);
	}

	public double getxPos() {
		return xPos;
	}

	public void setxPos(double xPos) {
		this.xPos = xPos;
	}

	public double getyPos() {
		return yPos;
	}

	public void setyPos(double yPos) {
		this.yPos = yPos;
	}

	public boolean isDestoryAfterAnim() {
		return destoryAfterAnim;
	}

	public void setDestoryAfterAnim(boolean destoryAfterAnim) {
		this.destoryAfterAnim = destoryAfterAnim;
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

	public int getAnimationSpeed() {
		return animationSpeed;
	}

	public void setAnimationSpeed(int animationSpeed) {
		this.animationSpeed = animationSpeed;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		if(limit > 0) {
			this.limit = limit - 1;
		} else {
			this.limit = limit;
		}
	}
	
	public void resetLimit() {
		limit = sprites.size() - 1;
	}

	public boolean isPlay() {
		return play;
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
