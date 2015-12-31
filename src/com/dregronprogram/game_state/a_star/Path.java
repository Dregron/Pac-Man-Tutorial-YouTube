package com.dregronprogram.game_state.a_star;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.game_state.Direction;
import com.dregronprogram.utils.MathUtils;
import com.dregronprogram.utils.Vector2;

public class Path implements Renderer {
	
	private TreeMap<Integer, Node> openNodes = new TreeMap<Integer, Node>();
	private List<Node> closedNodes = new ArrayList<Node>();
	
	private Map<Vector2, Node> nodes = new HashMap<Vector2, Node>();
	
	private Node startNode, goalNode;

	public Path(Map<Vector2, Node> nodes) {
		this.nodes = nodes;
	}
	
	@Override
	public void update(double delta) {
		
	}

	@Override
	public void draw(Graphics2D g) {
		
	}
	
	public Map<Vector2, Node> getNodes() {
		return nodes;
	}
	
	public Map<Node, Direction> findPath(int startXPos, int startYPos, int goalXPos, int goalYPos, int width, int height) {
		Map<Node, Direction> queuedDirections = new LinkedHashMap<Node, Direction>();
		this.startNode = new Node(startXPos, startYPos, width, height);
		this.goalNode = new Node(goalXPos, goalYPos, width, height);
		this.openNodes.put(getDistanceBetween(startNode, goalNode), startNode);
		
		while(!openNodes.isEmpty()) {
			int key = openNodes.firstKey();
			Node current = openNodes.get(key);

			closedNodes.add(current);
			openNodes.remove(key);

			if (MathUtils.isEqual(current.getxPos(), goalXPos, width) && MathUtils.isEqual(current.getyPos(), goalYPos, height)) {
				queueDirection(queuedDirections, current);
				for (Node node : current.getPrevNodesNode()) {
					node.setColour(Color.CYAN);
					queueDirection(queuedDirections, node);
				}
				
				break;
			}
			
			for (Node neighbor : getAdjacentBlocks(current)) {
				
				if (closedNodes.contains(neighbor)) {
					continue;
				}

				if (!openNodes.values().contains(neighbor)) {
					openNodes.put(getDistanceBetween(neighbor, goalNode), neighbor);
					neighbor.setNeighborX(getDX(current, neighbor));
					neighbor.setNeighborY(getDY(current, neighbor));
					neighbor.getPrevNodesNode().add(current);
					neighbor.getPrevNodesNode().addAll(current.getPrevNodesNode());
				} 
			}
			
		}
		reset();
		return queuedDirections;
	}

	private void reset() {
		openNodes.clear();
		closedNodes.clear();
		startNode = null;
		goalNode = null;
		for (Node node : nodes.values()) {
			node.clear();
		}
	}

	private void queueDirection(Map<Node, Direction> queuedDirections, Node node) {
		if (node.getNeighborX() == 1) {
			queuedDirections.put(node, Direction.LEFT);
		} else if (node.getNeighborX() == -1) {
			queuedDirections.put(node, Direction.RIGHT);
		} else if (node.getNeighborY() == -1) {
			queuedDirections.put(node, Direction.DOWN);
		} else if (node.getNeighborY() == 1) {
			queuedDirections.put(node, Direction.UP);
		}
	}
	
	private List<Node> getAdjacentBlocks(Node current) {
		List<Node> bs = new ArrayList<Node>();
		Vector2 vector = new Vector2(current.getWidth()-2, current.getHeight()-2);
		vector.set(current.getxPos()+current.getWidth(), current.getyPos());
		addAdjacentBlock(bs, vector);
		vector.set(current.getxPos()-current.getWidth(), current.getyPos());
		addAdjacentBlock(bs, vector);
		vector.set(current.getxPos(), current.getyPos()+current.getHeight());
		addAdjacentBlock(bs, vector);
		vector.set(current.getxPos(), current.getyPos()-current.getHeight());
		addAdjacentBlock(bs, vector);
		return bs;
	}

	private void addAdjacentBlock(List<Node> bs, Vector2 vector) {
		Node node = nodes.get(vector);
		if (node != null) {
			bs.add(node);
		}
	}
	
	private int getDX(Node current, Node neighbor) {
		int targetX = current.getxPos() / current.getWidth();
		int currentX = neighbor.getxPos() / current.getWidth();
		return targetX - currentX;
	}
	
	private int getDY(Node current, Node neighbor) {
		int targetY = current.getyPos() /  current.getHeight();
		int currentY = neighbor.getyPos() / neighbor.getHeight();
		return targetY - currentY; 
	}
	
	private int getDistanceBetween(Node current, Node neighbor) {
		return Math.abs(getDX(current, neighbor)) + Math.abs(getDY(current, neighbor));
	}
}
