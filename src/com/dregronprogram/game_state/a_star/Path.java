package com.dregronprogram.game_state.a_star;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.display.Display;
import com.dregronprogram.utils.MathUtils;
import com.dregronprogram.utils.Vector2;

public class Path implements Renderer {
	
	private Map<Vector2, Node> nodes = new HashMap<>();

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
	
	public List<Node> findRandomPath(int startXPos, int startYPos) {
		Node[] nodeArray = new Node[getNodes().size()];
		Node node = getNodes().values().toArray(nodeArray)[new Random().nextInt(getNodes().size()-1)];
		return findPath(startXPos, startYPos, node.getxPos(), node.getyPos());
	}

	public List<Node> findPath(int startXPos, int startYPos, int goalXPos, int goalYPos) {
		Map<Node, Node> cameFrom = new LinkedHashMap<>();
		List<Node> openNodes = new LinkedList<>();
		List<Node> closedNodes = new LinkedList<>();
		Node startNode, goalNode;
		startNode = getNodes().get(new Vector2(startXPos, startYPos, 0, 0));
		goalNode = getNodes().get(new Vector2(goalXPos, goalYPos, 0, 0));
		startNode.setG_score(0);
		startNode.setF_score(getDistanceBetween(startNode, goalNode));
		openNodes.add(startNode);
		
		for (Node node : getNodes().values()) {
			node.clearColour();
		}
		
		while(!openNodes.isEmpty()) {
			Node current = getNodeWithLowestFScore(openNodes);
			
			closedNodes.add(current);
			openNodes.remove(current);

			if (MathUtils.isEqual(current.getxPos(), goalNode.getxPos(), 0) && MathUtils.isEqual(current.getyPos(), goalNode.getyPos(), 0)) {
				return reconstructPath(cameFrom, goalNode);
			}
			
			current.setColour(Color.CYAN);
			for (Node neighbor : getAdjacentBlocks(current)) {
				
				if (closedNodes.contains(neighbor)) {
					continue;
				}
				neighbor.setColour(Color.CYAN);
				int tentative_g_score = current.getG_score() + getDistanceBetween(current,neighbor);
				if (!openNodes.contains(neighbor)) {
					openNodes.add(neighbor);
				} else if (tentative_g_score >= neighbor.getG_score()) {
					continue;
				}
				
				cameFrom.put(neighbor, current);
				neighbor.setG_score(tentative_g_score);
				neighbor.setF_score(neighbor.getG_score() + getDistanceBetween(neighbor, goalNode));
			}
		}
		return new LinkedList<>();
	}
	
	private List<Node> reconstructPath(Map<Node, Node> cameFrom, Node current) {
		List<Node> totalPath = new LinkedList<>();
		totalPath.add(current);
		current.setColour(Color.red);
		while (cameFrom.containsKey(current)) {
			current = cameFrom.get(current);
			current.setColour(Color.red);
			totalPath.add(current);
		}
		return totalPath;
	}

	private Node getNodeWithLowestFScore(List<Node> openNodes) {
		int f_score = Integer.MAX_VALUE;
		Node n = null;
		for (Node node : openNodes) {
			if (node.getF_score() < f_score) {
				f_score = node.getF_score();
				n = node;
			}
		}
		return n;
	}

	public List<Node> getAdjacentBlocks(Node current) {
		List<Node> bs = new ArrayList<>();
		Vector2 vector = new Vector2(0, 0);
		vector.set(current.getxPos()+current.getWidth(), current.getyPos());
		addAdjacentBlock(current, bs, vector);
		vector.set(current.getxPos()-current.getWidth(), current.getyPos());
		addAdjacentBlock(current, bs, vector);
		vector.set(current.getxPos(), current.getyPos()+current.getHeight());
		addAdjacentBlock(current, bs, vector);
		vector.set(current.getxPos(), current.getyPos()-current.getHeight());
		addAdjacentBlock(current, bs, vector);
		return bs;
	}
	
	public boolean isAdjacentBlock(Node currentNode, Node prevNode) {
		return currentNode.equals(prevNode) || getAdjacentBlocks(currentNode).contains(prevNode);
	}
	
	private void addAdjacentBlock(Node current, List<Node> bs, Vector2 vector) {
		if (nodes.containsKey(vector)) {
			bs.add(nodes.get(vector));
		} else if (current.isAdjacentRightFloor()) {
			vector.set(0, current.getyPos());
			bs.add(nodes.get(vector));
		} else if (current.isAdjacentLeftFloor()) {
			vector.set((Display.WIDTH-current.getWidth()), current.getyPos());
			bs.add(nodes.get(vector));
		}
	}
	
	public int getDX(Node current, Node neighbor) {
		int targetX = neighbor.getxPos() / neighbor.getWidth();
		int currentX = current.getxPos() / current.getWidth();
		int targetXRightMirror = (Display.WIDTH / neighbor.getWidth())+targetX;
		int targetXLeftMirror = -(Display.WIDTH/neighbor.getWidth())+targetX;

		int normalTarget = targetX-currentX;
		int rightMirrorTarget = targetXRightMirror-currentX;
		int leftMirrorTarget = targetXLeftMirror-currentX;

		if (Math.abs(normalTarget) <= Math.abs(rightMirrorTarget) && Math.abs(normalTarget) <= Math.abs(leftMirrorTarget)) {
			return normalTarget;
		} else if (Math.abs(leftMirrorTarget) <= Math.abs(rightMirrorTarget)) {
			return leftMirrorTarget;
		} else {
			return rightMirrorTarget;
		}
	}

	public int getDY(Node current, Node neighbor) {
		int targetY = neighbor.getyPos() /  neighbor.getHeight();
		int currentY = current.getyPos() / current.getHeight();

		return targetY-currentY;
	}

	private int getDistanceBetween(Node current, Node neighbor) {
		return Math.abs(getDX(current, neighbor)) + Math.abs(getDY(current, neighbor));
	}
}
