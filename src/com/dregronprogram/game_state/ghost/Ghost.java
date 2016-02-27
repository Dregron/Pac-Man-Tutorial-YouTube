package com.dregronprogram.game_state.ghost;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.display.Display;
import com.dregronprogram.game_state.GameState;
import com.dregronprogram.game_state.Player;
import com.dregronprogram.game_state.a_star.Node;
import com.dregronprogram.game_state.a_star.Path;
import com.dregronprogram.utils.MathUtils;
import com.dregronprogram.utils.SpriteAnimation;
import com.dregronprogram.utils.Vector2;

public class Ghost implements Renderer {

	private Rectangle rectangle;
	private float xPos, yPos, delay;
	private SpriteAnimation leftAnimation, rightAnimation, downAnimation, upAnimation, scaredGhost;
	private Path path;
	private List<Node> nodes = new LinkedList<>();
	private Node currentTarget, prevTarget;
	private GhostState currentState;
	private Player player;

	private Ellipse2D ellipse;

	public Ghost(int xPos, int yPos, int width, int height, Map<Vector2, Node> nodes, Player player, float delay) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.rectangle = new Rectangle(xPos, yPos, width, height);
		this.path = new Path(nodes);
		this.player = player;
		this.currentState = GhostState.BEGIN;
		this.ellipse = new Ellipse2D.Double(xPos, yPos, width*8, height*8);
		this.delay = delay;
	}

	@Override
	public void update(double delta) {
		if (delay > 0) {
			if (nodes.isEmpty()) {
				int randomNum = new Random().nextInt((15 - 9) + 1) + 9;
				this.nodes.addAll(getPath().findPath(getRectangle().x, getRectangle().y, randomNum * getRectangle().width, 10 * getRectangle().height));
				this.currentTarget = this.nodes.get(nodes.size() - 1);
			} else {
				movement(delta);
			}
			updateToNextTarget();
			delay -= delta * 1;
			return;
		}

		switch (currentState) {
			case BEGIN:
				if (nodes.isEmpty()) {
					this.nodes.addAll(getPath().findPath(getRectangle().x, getRectangle().y, 5 * getRectangle().width, 9 * getRectangle().height));
					this.currentTarget = this.nodes.get(nodes.size() - 1);
				} else {
					movement(delta);
				}
				
				updateToNextTarget();
				
				if (nodes.isEmpty()) {
					setCurrentState(GhostState.SEARCH);
				}
			break;
			case IDLE:

				break;
			case SEARCH:
				if (nodes.isEmpty()) {
					nodes.addAll(getPath().findRandomPath((int) getXPos(), (int) getYPos()));
					currentTarget = nodes.get(nodes.size() - 1);
				} else {
					movement(delta);
				}

				switchState();
				updateToNextTarget();
				break;
			case SCARED:
				if (nodes.isEmpty()) {
					nodes.addAll(getPath().findRandomPath((int) getXPos(), (int) getYPos()));
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
					nodes.addAll(getPath().findPath((int) getXPos(), (int) getYPos(), xPlayer, yPlayer));
					currentTarget = nodes.get(nodes.size() - 1);
				} else {
					movement(delta);
				}

				switchState();
				updateToNextTarget();
				break;
		}

		if (getPlayer().isSuperPacMan()) {
			setCurrentState(GhostState.SCARED);
		} else if (getCurrentState().equals(GhostState.SCARED)) {
			switchState();
		}

		leftAnimation.update(delta);
		rightAnimation.update(delta);
		upAnimation.update(delta);
		downAnimation.update(delta);
		scaredGhost.update(delta);
	}

	private void updateToNextTarget() {
		if (MathUtils.isEqual(currentTarget.getxPos(), getRectangle().x, 1) && MathUtils.isEqual(currentTarget.getyPos(), getRectangle().y, 1) && !nodes.isEmpty()) {
            setXPos(currentTarget.getxPos());
            setYPos(currentTarget.getyPos());
            prevTarget = currentTarget;
            currentTarget = nodes.get(nodes.size() - 1);
            if (!getPath().isAdjacentBlock(currentTarget, prevTarget)) {
                throw new IllegalStateException("current node not adjacent from prev target \n"
                        + "prev xPos: " + prevTarget.getxPos()  + " prev yPos: " + prevTarget.getyPos() + " \n"
                        + "curr xPos: " + currentTarget.getxPos() + " curr yPos: " + currentTarget.getyPos());
            }
            nodes.remove(currentTarget);
        }
		
	}

	private void switchState() {
		if (!getCurrentState().equals(GhostState.TARGET_PLAYER) && getEllipse().intersects(getPlayer().getRectangle())) {
			setCurrentState(GhostState.TARGET_PLAYER);
			nodes.clear();
			nodes.add(0, prevTarget);
			nodes.add(1, currentTarget);
		} else if (!getCurrentState().equals(GhostState.SEARCH) && !getEllipse().intersects(getPlayer().getRectangle())) {
			setCurrentState(GhostState.SEARCH);
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
		if (getPath().getDX(currentTarget, prevTarget) == -1) {
            setXPos(getXPos() + xSpeed);
        } else if (getPath().getDX(currentTarget, prevTarget) == 1) {
            setXPos(getXPos() - xSpeed);
        }

		if (getPath().getDY(currentTarget, prevTarget) == -1) {
            setYPos(getYPos() + ySpeed);
        } else if (getPath().getDY(currentTarget, prevTarget) == 1) {
            setYPos(getYPos() - ySpeed);
        }
	}

	@Override
	public void draw(Graphics2D g) {

		if (GameState.debugMode) {
			g.drawOval( (int) getEllipse().getX(),
						(int) getEllipse().getY(),
						(int) getEllipse().getWidth(),
						(int) getEllipse().getHeight());
		}

		if (getCurrentState().equals(GhostState.SCARED)) {
			scaredGhost.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
		} else if (currentTarget != null && prevTarget != null) {
			if (getPath().getDX(currentTarget, prevTarget) == -1) {
				rightAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			} else if (getPath().getDX(currentTarget, prevTarget) == 1) {
				leftAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			}

			if (getPath().getDY(currentTarget, prevTarget) == 1) {
				upAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			} else if (getPath().getDY(currentTarget, prevTarget) == -1) {
				downAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
			}
			
			if (getPath().getDX(currentTarget, prevTarget) == 0 && getPath().getDY(currentTarget, prevTarget) == 0) {
				leftAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
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
		this.getEllipse().setFrame(xPos-(getEllipse().getWidth()/2)+(getRectangle().width/2), 
									getEllipse().getY(), 
									getEllipse().getWidth(), 
									getEllipse().getHeight());
	}

	public void setYPos(float yPos) {
		this.yPos = yPos;
		this.getRectangle().y = (int) yPos;
		this.getEllipse().setFrame(getEllipse().getX(), 
									yPos-(getEllipse().getHeight()/2)+(getRectangle().height/2), 
									getEllipse().getWidth(),
									getEllipse().getHeight());
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
	
	public Ellipse2D getEllipse() {
		return ellipse;
	}
	
	public GhostState getCurrentState() {
		return currentState;
	}
	
	public void setCurrentState(GhostState currentState) {
		this.currentState = currentState;
	}

	public void setScaredGhost(SpriteAnimation scaredGhost) {
		this.scaredGhost = scaredGhost;
	}

	public SpriteAnimation getScaredGhost() {
		return scaredGhost;
	}
}
