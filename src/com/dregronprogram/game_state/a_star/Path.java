package com.dregronprogram.game_state.a_star;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.game_state.Direction;
import com.dregronprogram.utils.MathUtils;
import com.dregronprogram.utils.Vector2;

public class Path implements Renderer {
	
	private Map<Vector2, Node> nodes = new HashMap<Vector2, Node>();
	
	

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
	
	public List<Direction> findPath(int startXPos, int startYPos, int goalXPos, int goalYPos, int width, int height) {
		List<Direction> queuedDirections = new LinkedList<Direction>();
		List<Node> openNodes = new LinkedList<Node>();
		List<Node> closedNodes = new LinkedList<Node>();
		Node startNode, goalNode;
		startNode = new Node(startXPos, startYPos, width, height, "start");
		goalNode = new Node(goalXPos, goalYPos, width, height, "goal");
		startNode.setG_score(0);
		startNode.setF_score(getDistanceBetween(startNode, goalNode));
		openNodes.add(startNode);
		
		for (Node node : nodes.values()) {
			node.clearColour();
			node.clear();
		}
		
		while(!openNodes.isEmpty()) {
			int f_score = Integer.MAX_VALUE;
			Node n = null;
			for (Node node : openNodes) {
				if (node.getF_score() < f_score) {
					f_score = node.getF_score();
					n = node;
				}
			}
			Node current = n;
			
			closedNodes.add(current);
			openNodes.remove(current);

			if (MathUtils.isEqual(current.getxPos(), goalXPos, 0) && MathUtils.isEqual(current.getyPos(), goalYPos, 0)) {
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
				int tentative_g_score = current.getG_score() + getDistanceBetween(current,neighbor);
				if (!openNodes.contains(neighbor)) {
					openNodes.add(neighbor);
				} else if (tentative_g_score >= neighbor.getG_score()) {
					continue;
				}
				
				neighbor.setG_score(tentative_g_score);
				neighbor.setF_score(neighbor.getG_score() + getDistanceBetween(neighbor, goalNode));
				neighbor.setNeighborX(getDX(current, neighbor));
				neighbor.setNeighborY(getDY(current, neighbor));
				neighbor.getPrevNodesNode().add(current);
				neighbor.getPrevNodesNode().addAll(current.getPrevNodesNode());
			}
		}
		return queuedDirections;
	}

	private void queueDirection(List< Direction> queuedDirections, Node node) {
		if (node.getNeighborX() == 1) {
			queuedDirections.add(Direction.LEFT);
		} else if (node.getNeighborX() == -1) {
			queuedDirections.add(Direction.RIGHT);
		} else if (node.getNeighborY() == -1) {
			queuedDirections.add(Direction.DOWN);
		} else if (node.getNeighborY() == 1) {
			queuedDirections.add(Direction.UP);
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
	
	private void check(Node current, List<Node> bs, Vector2 vector) {
		vector.set(current.getxPos(), current.getyPos());
		if (vector.equals(new Vector2(24*current.getWidth(), 9*current.getHeight(), current.getWidth()-2, current.getHeight()-2))) {
			Node node = nodes.get(new Vector2(0, 9*current.getHeight(), current.getWidth()-2, current.getHeight()-2));
			if (node != null) {
				bs.add(node);
			}
		} else if (vector.equals(new Vector2(0, 9*current.getHeight(), current.getWidth()-2, current.getHeight()-2))) {
			Node node = nodes.get(new Vector2(24*current.getWidth(), 9*current.getHeight(), current.getWidth()-2, current.getHeight()-2));
			if (node != null) {
				bs.add(node);
			}
		}
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
