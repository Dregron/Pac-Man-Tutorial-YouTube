package com.dregronprogram.game_state.ghost;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.game_state.Direction;
import com.dregronprogram.game_state.Player;
import com.dregronprogram.game_state.a_star.Node;
import com.dregronprogram.game_state.a_star.Path;
import com.dregronprogram.utils.Vector2;

public class Ghost implements Renderer {

	private Map<Node, Direction> direction = new LinkedHashMap<Node, Direction>();
	private ListIterator<Node> sortedList;
	private BufferedImage ghostImage;
	private Rectangle rectangle;
	private Path path;
	private Node currentNode;
	private int speed;
	private Player player;
	
	public Ghost(int xPos, int yPos, int width, int height, BufferedImage bufferedImage, Map<Vector2, Node> nodes) {
		this.setRectangle(new Rectangle(xPos, yPos, width, height));
		this.setPath(new Path(nodes));
		this.ghostImage = bufferedImage;
		this.speed = 2;
	}

	@Override
	public void update(double delta) {
		
		if (currentNode != null) {
			switch (direction.get(currentNode)) {
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
		
			if (	currentNode.getxPos() == getRectangle().x 
					&& currentNode.getyPos() == getRectangle().y 
					&& sortedList.hasPrevious()) {
				
				currentNode = sortedList.previous();
			} else if (	currentNode.getxPos() == getRectangle().x 
						&& currentNode.getyPos() == getRectangle().y 
						&& !sortedList.hasPrevious()) {
				currentNode = null;
				for (Node node : direction.keySet()) {
					node.clearColour();
				}
				direction.clear();
			}
		}
		
 		if (direction.isEmpty()) {
 			findPath(player.getRectangle().x, player.getRectangle().y, player.getRectangle().width, player.getRectangle().height);
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
		findPath(player.getRectangle().x, player.getRectangle().y, player.getRectangle().width, player.getRectangle().height);
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	private void findPath(int targetXPos, int targetYPos, int width, int height) {
		Map<Node, Direction> findPath = path.findPath(getRectangle().x, getRectangle().y, targetXPos, targetYPos, width, height);
		if (!findPath.isEmpty()) {
			this.direction = findPath;
			if (direction != null && !direction.isEmpty()) {		
				this.sortedList = new LinkedList<>(direction.keySet()).listIterator(direction.size());
				this.currentNode = sortedList.previous();
			}
		}
	}
}
