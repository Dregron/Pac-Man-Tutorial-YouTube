package com.dregronprogram.game_state.ghost;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.display.Display;
import com.dregronprogram.game_state.Player;
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
	private Player player;

	public Ghost(int xPos, int yPos, int width, int height, Map<Vector2, Node> nodes, Player player) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rectangle = new Rectangle(xPos, yPos, width, height);
		this.path = new Path(nodes);
		this.player = player;
		this.currentState = GhostState.TARGET_PLAYER;
	}

	@Override
	public void update(double delta) {

		switch (currentState) {
			case IDLE:

				break;
			case SEARCH:
				if (nodes.isEmpty()) {
					nodes.addAll(path.findRandomPath((int) getXPos(), (int) getYPos()));
					currentTarget = nodes.get(nodes.size() - 1);
				} else {
					movement(delta);
				}

				updateToNextTarget();
				break;
			case TARGET_PLAYER:
				if (nodes.isEmpty()) {
					int xPlayer = (getPlayer().getRectangle().x/getPlayer().getRectangle().width) * getPlayer().getRectangle().width;
					int yPlayer = (getPlayer().getRectangle().y/getPlayer().getRectangle().height) * getPlayer().getRectangle().height;
					nodes.addAll(path.findPath((int) getXPos(), (int) getYPos(), xPlayer, yPlayer));
					currentTarget = nodes.get(nodes.size() - 1);
				} else {
					movement(delta);
				}

				updateToNextTarget();
				break;
		}

		leftAnimation.update(delta);
		rightAnimation.update(delta);
		upAnimation.update(delta);
		downAnimation.update(delta);
	}

	private void updateToNextTarget() {
		if (MathUtils.isEqual(currentTarget.getxPos(), getRectangle().x, 1) && MathUtils.isEqual(currentTarget.getyPos(), getRectangle().y, 1) && !nodes.isEmpty()) {
            setXPos(currentTarget.getxPos());
            setYPos(currentTarget.getyPos());
            prevTarget = currentTarget;
            currentTarget = nodes.get(nodes.size() - 1);
            if (!path.isAdjacentBlock(currentTarget, prevTarget)) {
                throw new IllegalStateException("current node not adjacent from prev target \n"
                        + "prev xPos: " + prevTarget.getxPos()  + " prev yPos: " + prevTarget.getyPos() + " \n"
                        + "curr xPos: " + currentTarget.getxPos() + " curr yPos: " + currentTarget.getyPos());
            }
            nodes.remove(currentTarget);
        }
	}

	private void movement(double delta) {
		if (getXPos() >= Display.WIDTH + getRectangle().width) {
            setXPos(-getRectangle().width);
        } else if (getXPos() <= -getRectangle().width) {
            setXPos(Display.WIDTH + getRectangle().width);
        }

		float xSpeed = (float) (2 * delta);
		float ySpeed = (float) (2 * delta);
		if (path.getDX(currentTarget, prevTarget) == -1) {
            setXPos(getXPos() + xSpeed);
        } else if (path.getDX(currentTarget, prevTarget) == 1) {
            setXPos(getXPos() - xSpeed);
        }

		if (path.getDY(currentTarget, prevTarget) == -1) {
            setYPos(getYPos() + ySpeed);
        } else if (path.getDY(currentTarget, prevTarget) == 1) {
            setYPos(getYPos() - ySpeed);
        }
	}

	@Override
	public void draw(Graphics2D g) {

		g.setColor(Color.RED);
		if (currentTarget != null && prevTarget != null) {
			if (path.getDX(currentTarget, prevTarget) == -1) {
				rightAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			} else if (path.getDX(currentTarget, prevTarget) == 1) {
				leftAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			}

			if (path.getDY(currentTarget, prevTarget) == 1) {
				upAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			} else if (path.getDY(currentTarget, prevTarget) == -1) {
				downAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			}
		} else {
			leftAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
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

	public Player getPlayer() {
		return player;
	}
}
