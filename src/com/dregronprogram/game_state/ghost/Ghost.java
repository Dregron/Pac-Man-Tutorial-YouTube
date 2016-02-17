package com.dregronprogram.game_state.ghost;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.game_state.a_star.Node;
import com.dregronprogram.game_state.a_star.Path;
import com.dregronprogram.utils.MathUtils;
import com.dregronprogram.utils.Vector2;

public class Ghost implements Renderer {

	private Rectangle rectangle;
	private float xPos, yPos;
	private BufferedImage idleGhost;
	private Path path;
	private List<Node> nodes = new LinkedList<>();
	private Node currentTarget;


	public Ghost(int xPos, int yPos, int width, int height, BufferedImage idleGhost, Map<Vector2, Node> nodes) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rectangle = new Rectangle(xPos, yPos, width, height);
		this.path = new Path(nodes);
		this.idleGhost = idleGhost;
	}

	@Override
	public void update(double delta) {
		if (nodes.isEmpty()) {
			nodes.addAll(path.findRandomPath((int) getXPos(),(int) getYPos()));
			currentTarget = nodes.get(nodes.size()-1);
		} else {
			setXPos(MathUtils.lerp(currentTarget.getxPos(), getXPos(), (float) (2*delta)));
			setYPos(MathUtils.lerp(currentTarget.getyPos(), getYPos(), (float) (2*delta)));
		}

		if (MathUtils.isEqual(currentTarget.getxPos(), getRectangle().x, 1) && MathUtils.isEqual(currentTarget.getyPos(), getRectangle().y, 1) && !nodes.isEmpty()) {
			setXPos(currentTarget.getxPos());
			setYPos(currentTarget.getyPos());
			currentTarget = nodes.get(nodes.size()-1);
			nodes.remove(currentTarget);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(getIdleGhost(), getRectangle().x,getRectangle().y, null);
	}

	public BufferedImage getIdleGhost() {
		return idleGhost;
	}

	public Path getPath() {
		return path;
	}

	public float getXPos() {
		return xPos;
	}

	public float getYPos() {
		return yPos;
	}

	public void setXPos(float xPos) {
		this.xPos = xPos;
		this.getRectangle().x = (int) xPos;
	}

	public void setYPos(float yPos) {
		this.yPos = yPos;
		this.getRectangle().y = (int) yPos;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
}
