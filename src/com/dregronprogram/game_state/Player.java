package com.dregronprogram.game_state;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.application.SpriteAnimation;

public class Player implements Renderer, KeyListener {
	
	private SpriteAnimation playerAnimation;
	private Rectangle rectangle;
	private DIRECTION currentDirection, queuedDirection;
	private int speed;
	
	private enum DIRECTION {LEFT, RIGHT, UP, DOWN}
	
	private List<Block> blocks = new ArrayList<Block>();
	
	public Player(List<BufferedImage> playerImg) {
		this.playerAnimation = new SpriteAnimation(0, 0, 8, playerImg);
		this.rectangle = new Rectangle(0, 0, playerImg.get(0).getWidth(), playerImg.get(0).getHeight());
		this.speed = 2;
		this.currentDirection = DIRECTION.RIGHT;
	
		this.playerAnimation.setLoop(true);
	}

	@Override
	public void update(double delta) {
		if (isAboutToCollide()) {
			while(!attemptChangeDirection()) {
				queuedDirection = DIRECTION.values()[new Random().nextInt(DIRECTION.values().length)];
			}
		}
		
		switch (currentDirection) {
			case LEFT:
				getRectangle().x -= speed;
				break;
			case RIGHT:
				getRectangle().x += speed;
				break;
			case UP:
				getRectangle().y -= speed;
				break;
			case DOWN:
				getRectangle().y += speed;
				break;
		}
		attemptChangeDirection();
		playerAnimation.setxPos(getRectangle().x);
		playerAnimation.setyPos(getRectangle().y);
		playerAnimation.update(delta);
	}

	private boolean attemptChangeDirection() {
		if (changeDirection()) {
			currentDirection = queuedDirection;
			queuedDirection = null;
			switch (currentDirection) {
				case LEFT:
					playerAnimation.setRotation(Math.toDegrees(.2f));
					break;
				case RIGHT:
					playerAnimation.setRotation(Math.toDegrees(0));
					break;
				case UP:
					playerAnimation.setRotation(Math.toDegrees(.3f));
					break;
				case DOWN:
					playerAnimation.setRotation(Math.toDegrees(.1f));
					break;
			
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		playerAnimation.draw(g);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			queuedDirection = DIRECTION.LEFT;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			queuedDirection = DIRECTION.RIGHT;
		} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			queuedDirection = DIRECTION.UP;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			queuedDirection = DIRECTION.DOWN;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void setXPos(int xPos) {
		this.rectangle.x = xPos;
	}
	
	public void setYPos(int yPos) {
		this.rectangle.y = yPos;
	}
	
	public List<Block> getBlocks() {
		return blocks;
	}
	
	public void setBlocks(Collection<Block> blocks) {
		this.blocks.clear();
		this.blocks.addAll(blocks);
	}
	
	private boolean isAboutToCollide() {
			switch (currentDirection) {
			case LEFT:
				for (Block b : blocks) {
					if (getRectangle().intersects(b.getxPos()+1, b.getyPos(), b.getWidth(), b.getHeight())) {
						return true;
					}
				}
				break;
			case RIGHT:
				for (Block b : blocks) {
					if (getRectangle().intersects(b.getxPos()-1, b.getyPos(), b.getWidth(), b.getHeight())) {
						return true;
					}
				}
				break;
			case UP:
				for (Block b : blocks) {
					if (getRectangle().intersects(b.getxPos(), b.getyPos()+1, b.getWidth(), b.getHeight())) {
						return true;
					}
				}
				break;
			case DOWN:
				for (Block b : blocks) {
					if (getRectangle().intersects(b.getxPos(), b.getyPos()-1, b.getWidth(), b.getHeight())) {
						return true;
					}
				}
				break;
			}
		return false;
	}
	
	private boolean changeDirection() {
		if (queuedDirection == null || getBlocks().isEmpty()) return false;
		
		switch (queuedDirection) {
			case LEFT:
				for (Block b : blocks) {
					if (getRectangle().intersects(b.getxPos()+b.getWidth(), b.getyPos(), b.getWidth(), b.getHeight())) {
						return false;
					}
				}
				break;
			case RIGHT:
				for (Block b : blocks) {
					if (getRectangle().intersects(b.getxPos()-b.getWidth(), b.getyPos(), b.getWidth(), b.getHeight())) {
						return false;
					}
				}
				break;
			case UP:
				for (Block b : blocks) {
					if (getRectangle().intersects(b.getxPos(), b.getyPos()+b.getHeight(), b.getWidth(), b.getHeight())) {
						return false;
					}
				}
				break;
			case DOWN:
				for (Block b : blocks) {
					if (getRectangle().intersects(b.getxPos(), b.getyPos()-b.getHeight(), b.getWidth(), b.getHeight())) {
						return false;
					}
				}
				break;
		}
		return true;
	}
}
