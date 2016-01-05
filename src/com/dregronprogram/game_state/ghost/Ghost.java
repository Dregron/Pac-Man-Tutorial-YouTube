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
	private Rectangle rectangle;
	private Path path;
	private Direction currentDirection;
	private int speed, stam;
	private Player player;
	private boolean start = false;
	
	public Ghost(int xPos, int yPos, int width, int height, BufferedImage bufferedImage, Map<Vector2, Node> nodes) {
		this.setRectangle(new Rectangle(xPos, yPos, width, height));
		this.setPath(new Path(nodes));
		this.ghostImage = bufferedImage;
		this.speed = 2;
	}

	@Override
	public void update(double delta) {
		if (!isStart()) return;
		
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
		
 		while (direction.isEmpty()) {
 			findPath(player.getRectangle().x, player.getRectangle().y, player.getRectangle().width, player.getRectangle().height);
 		}
 		
 		if (getRectangle().x > Display.WIDTH-getRectangle().width) {
			getRectangle().x = 0;
		} else if (getRectangle().x <= 0) {
			getRectangle().x = Display.WIDTH-getRectangle().width;
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getGhostImage(), getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height, null);
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
		List<Direction> findPath = path.findPath(getRectangle().x, getRectangle().y, targetX, targetY, width, height);
		if (!findPath.isEmpty()) {
			this.direction = findPath;
			if (direction != null && !direction.isEmpty()) {		
				this.sortedList = new LinkedList<>(direction).listIterator(direction.size());
				this.currentDirection = sortedList.previous();
				setStam();
			}
		}
		
	}

	private void start() {
		List<Direction> findPath = new LinkedList<Direction>(Arrays.asList( Direction.LEFT, Direction.LEFT, Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.LEFT, Direction.UP));
		if (!findPath.isEmpty()) {
			this.direction = findPath;
			if (direction != null && !direction.isEmpty()) {		
				this.sortedList = new LinkedList<>(direction).listIterator(direction.size());
				this.currentDirection = sortedList.previous();
				setStam();
			}
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
		if (start) {
			start();
		}
		this.start = start;
	}
	
	public boolean isStart() {
		return start;
	}
}
