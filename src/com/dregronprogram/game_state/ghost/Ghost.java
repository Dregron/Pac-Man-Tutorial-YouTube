package com.dregronprogram.game_state.ghost;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.display.Display;
import com.dregronprogram.game_state.a_star.Node;
import com.dregronprogram.game_state.a_star.Path;
import com.dregronprogram.utils.MathUtils;
import com.dregronprogram.utils.SpriteAnimation;
import com.dregronprogram.utils.Vector2;

public class Ghost implements Renderer {

	private Rectangle rectangle;
	private float xPos, yPos;
	private SpriteAnimation leftAnimation, rightAnimation, downAnimation, upAnimation;
	private Path path;
	private List<Node> nodes = new LinkedList<>();
	private Node currentTarget, prevTarget;
	private GhostState currentState;

	public Ghost(int xPos, int yPos, int width, int height, Map<Vector2, Node> nodes) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rectangle = new Rectangle(xPos, yPos, width, height);
		this.path = new Path(nodes);
		this.currentState = GhostState.RANDOM;
	}

	@Override
	public void update(double delta) {

		switch (currentState) {
			case IDLE:

				break;
			case RANDOM:
				if (nodes.isEmpty()) {
					nodes.addAll(path.findRandomPath((int) getXPos(), (int) getYPos()));
					currentTarget = nodes.get(nodes.size() - 1);
				} else {
					if (getXPos() >= Display.WIDTH + getRectangle().width) {
						setXPos(-getRectangle().width);
					} else if (getXPos() <= -getRectangle().width) {
						setXPos(Display.WIDTH + getRectangle().width);
					}

					float xSpeed = 32;//(float) (2 * delta);
					float ySpeed = 30;//(float) (2 * delta);
					if (prevTarget.isAdjacentLeftFloor()) {
						setXPos(getXPos() - xSpeed);
					} else if (prevTarget.isAdjacentRightFloor()) {
						setXPos(getXPos() + xSpeed);
					} else {
						setXPos(MathUtils.lerp(currentTarget.getxPos(), getXPos(), xSpeed));
					}
					
					setYPos(MathUtils.lerp(currentTarget.getyPos(), getYPos(), ySpeed));
				}

				if (MathUtils.isEqual(currentTarget.getxPos(), getRectangle().x, 1) && MathUtils.isEqual(currentTarget.getyPos(), getRectangle().y, 1) && !nodes.isEmpty()) {
					setXPos(currentTarget.getxPos());
					setYPos(currentTarget.getyPos());
					if (prevTarget == null || 
							(!(prevTarget.isAdjacentLeftFloor() && currentTarget.isAdjacentRightFloor()) &&
							!(prevTarget.isAdjacentRightFloor() && currentTarget.isAdjacentLeftFloor())) ) {
						prevTarget = currentTarget;
					}
					currentTarget = nodes.get(nodes.size() - 1);
					if (!path.isAdjacentBlock(currentTarget, prevTarget)) {
						throw new IllegalStateException("current node not adjacent from prev target \n"
														+ "prev xPos: " + prevTarget.getxPos()  + " prev yPos: " + prevTarget.getyPos() + " \n"
														+ "curr xPos: " + currentTarget.getxPos() + " curr yPos: " + currentTarget.getyPos());
					}
					nodes.remove(currentTarget);
				}
				break;
		}

		leftAnimation.update(delta);
		rightAnimation.update(delta);
		upAnimation.update(delta);
		downAnimation.update(delta);
	}

	@Override
	public void draw(Graphics2D g) {

		g.setColor(Color.RED);
		if (currentTarget != null) {
			if (currentTarget.getNeighborX() == 1) {
				rightAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			} else if (currentTarget.getNeighborX() == -1) {
				leftAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			}

			if (currentTarget.getNeighborY() == -1) {
				upAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			} else if (currentTarget.getNeighborY() == 1) {
				downAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			}
		}
		g.setPaintMode();
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

	public void setLeftAnimation(SpriteAnimation leftAnimation) {
		this.leftAnimation = leftAnimation;
	}

	public SpriteAnimation getLeftAnimation() {
		return leftAnimation;
	}

	public void setRightAnimation(SpriteAnimation rightAnimation) {
		this.rightAnimation = rightAnimation;
	}

	public SpriteAnimation getRightAnimation() {
		return rightAnimation;
	}

	public void setUpAnimation(SpriteAnimation upAnimation) {
		this.upAnimation = upAnimation;
	}

	public SpriteAnimation getUpAnimation() {
		return upAnimation;
	}

	public void setDownAnimation(SpriteAnimation downAnimation) {
		this.downAnimation = downAnimation;
	}

	public SpriteAnimation getDownAnimation() {
		return downAnimation;
	}
}
