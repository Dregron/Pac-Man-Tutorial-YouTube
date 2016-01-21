package com.dregronprogram.game_state.ghost;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.display.Display;
import com.dregronprogram.game_state.Direction;
import com.dregronprogram.game_state.Player;
import com.dregronprogram.game_state.a_star.Node;
import com.dregronprogram.game_state.a_star.Path;
import com.dregronprogram.utils.Vector2;

public class Ghost implements Renderer {

	private List<Direction> direction = new LinkedList<Direction>();
	private ListIterator<Direction> sortedList;
	private BufferedImage ghostImage;
	private BufferedImage scaredGhostImage;
	private BufferedImage eyesImage;
	private Rectangle rectangle;
	private Path path;
	private Direction currentDirection;
	private int speed;
	private Player player;
	private boolean start = false;
	private GhostState ghostState;
	private boolean resetGhost = false, reseting = false;
	
	public Ghost(int xPos, int yPos, int width, int height, BufferedImage ghostImg, Map<Vector2, Node> nodes) {
		this.setRectangle(new Rectangle(xPos, yPos, width, height));
		this.setPath(new Path(nodes));
		this.ghostImage = ghostImg;
		this.ghostState = GhostState.IDLE;
		this.speed = 2;
	}

	@Override
	public void update(double delta) {

		switch(getGhostState()) {
		case ATTACK:
			updateMovement();
	 		if (direction.isEmpty()) {
	 			findPath(player.getRectangle().x, player.getRectangle().y, player.getRectangle().width, player.getRectangle().height);
	 			if (isReseting()) {
	 				setReseting(false);
	 			}
	 		}
			break;
		case IDLE:
			updateGhostWhileIdle();
			break;
		case RANDOM:
			updateMovement();
			if (direction.isEmpty()) {
				Random r = new Random();
	 			findPath(r.nextInt(Display.WIDTH-getRectangle().width)+getRectangle().width, r.nextInt(Display.HEIGHT-getRectangle().height)+getRectangle().height, player.getRectangle().width, player.getRectangle().height);
	 		}
			break;
		}
		
		if (getRectangle().x > Display.WIDTH-getWidth()) {
			getRectangle().x = getWidth();
		} else if (getRectangle().x <= -getWidth()) {
			getRectangle().x = Display.WIDTH-getWidth();
		}
	}

	private void updateGhostWhileIdle() {
		if (!isStart() || GhostState.IDLE.equals(getGhostState())) {
			if (isStart() && direction.isEmpty()) {
				setGhostState(GhostState.RANDOM);
			}
			
			if (direction.isEmpty() && GhostState.IDLE.equals(getGhostState())) {
				movement(Direction.DOWN, Direction.UP);
			} else if (direction.isEmpty()) {
				findPath(5*getRectangle().width, 9*getRectangle().height, getRectangle().width, getRectangle().height);
			} else {
				updateMovement();
			}
		}
	}

	private void updateMovement() {
		if (currentDirection != null) {
			switch (currentDirection) {
			case LEFT:
				getRectangle().x -= getWidth();
				break;
			case RIGHT:
				getRectangle().x += getWidth();
				break;
			case UP:
				getRectangle().y -= getHeight();
				break;
			case DOWN:
				getRectangle().y += getHeight();
				break;
			case STOP:
				break;
			}
			
			if (sortedList.hasPrevious()) {
				if (!isPathGhostReseting()) {
					currentDirection = sortedList.previous();
				}
			} else if (!sortedList.hasPrevious()) {
				if (!isPathGhostReseting()) {
					currentDirection = null;
					direction.clear();
				}
			}
		}
	}
	
	private boolean isPathGhostReseting() {
		if (isResetGhost()) {
			findPath(12*getRectangle().width, 9*getRectangle().height, getRectangle().width, getRectangle().height);
			setReseting(true);
			setResetGhost(false);
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (getPlayer().isSuperPacMan()) {
			g.drawImage(getGhostImage(), getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height, null);
		} else if (isReseting()) {
			g.drawImage(getGhostImage(), getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height, null);
		} else {
			g.drawImage(getGhostImage(), getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height, null);
		}
	}
	
	public BufferedImage getGhostImage() {
		return ghostImage;
	}
	
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	
	public Rectangle getRectangle() {
		return rectangle;
	}
	
	private void setPath(Path path) {
		this.path = path;
	}
	
	public Path getPath() {
		return path;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	private void findPath(int targetXPos, int targetYPos, int width, int height) {
		int dx = (targetXPos/width);
		int dy = (targetYPos/height);
		int targetX =dx*width;
		int targetY = dy*height;
		setPathIfValid(path.findPath(getRectangle().x, getRectangle().y, targetX, targetY));
	}

	private void movement(Direction... directions) {
		setPathIfValid(new LinkedList<Direction>(Arrays.asList(directions)));
	}

	private void setPathIfValid(List<Direction> findPath) {
		if (!findPath.isEmpty()) {
			this.direction = findPath;
			if (direction != null && !direction.isEmpty()) {		
				this.sortedList = new LinkedList<>(direction).listIterator(direction.size());
				this.currentDirection = sortedList.previous();
			}
		} 
	}
	
	public int getCenterXPos() {
		return getRectangle().x+(getRectangle().width/2);
	}
	
	public int getCenterYPos() {
		return getRectangle().y+(getRectangle().height/2);
	}
	
	public int getWidth() {
		return getRectangle().width;
	}
	
	public int getHeight() {
		return getRectangle().height;
	}
	
	public void setStart(boolean start) {
		this.start = start;
	}
	
	public boolean isStart() {
		return start;
	}
	
	public GhostState getGhostState() {
		return ghostState;
	}
	
	public void setGhostState(GhostState ghostState) {
		this.ghostState = ghostState;
	}
	
	public void setResetGhost(boolean resetGhost) {
		this.resetGhost = resetGhost;
	}
	
	public boolean isResetGhost() {
		return resetGhost;
	}
	
	public void setReseting(boolean reseting) {
		this.reseting = reseting;
	}
	
	public boolean isReseting() {
		return reseting;
	}
}
