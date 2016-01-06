package com.dregronprogram.game_state.ghost;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

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
	private Rectangle rectangle;
	private Path path;
	private Direction currentDirection;
	private int speed, stam;
	private Player player;
	private boolean start = false;
	private GhostState ghostState;
	
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
	 		}
	 		if (getRectangle().x > Display.WIDTH) {
				getRectangle().x = -getRectangle().width;
				findPath(player.getRectangle().x, player.getRectangle().y, player.getRectangle().width, player.getRectangle().height);
			} else if (getRectangle().x <= -getRectangle().width) {
				getRectangle().x = Display.WIDTH;
				findPath(player.getRectangle().x, player.getRectangle().y, player.getRectangle().width, player.getRectangle().height);
			}
			break;
		case IDLE:
			updateGhostWhileIdle();
			break;
		case RANDOM:
			break;
		}
	}

	private void updateGhostWhileIdle() {
		if (!isStart() || GhostState.IDLE.equals(getGhostState())) {
			if (isStart() && direction.isEmpty()) {
				setGhostState(GhostState.ATTACK);
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
				getRectangle().x -= speed;
				stam -= speed;
				break;
			case RIGHT:
				getRectangle().x += speed;
				stam -= speed;
				break;
			case UP:
				getRectangle().y -= speed;
				stam -= speed;
				break;
			case DOWN:
				getRectangle().y += speed;
				stam -= speed;
				break;
			case STOP:
				break;
			}
			
			if (sortedList.hasPrevious() && stam == 0) {
				currentDirection = sortedList.previous();
				setStam();
			} else if (!sortedList.hasPrevious() && stam == 0) {
				currentDirection = null;
				direction.clear();
			}
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		if (getPlayer().isSuperPacMan()) {
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
		setPathIfValid(path.findPath(getRectangle().x, getRectangle().y, targetX, targetY, width, height));
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
				setStam();
			}
		} else {
			throw new RuntimeException("Path finding fucked up!");
		}
	}
	
	private void setStam() {
		switch (currentDirection) {
		case LEFT:
			stam = getRectangle().width;
			break;
		case RIGHT:
			stam = getRectangle().width;
			break;
		case UP:
			stam = getRectangle().height;
			break;
		case DOWN:
			stam = getRectangle().height;
			break;
		case STOP:
			break;
		}
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
}
