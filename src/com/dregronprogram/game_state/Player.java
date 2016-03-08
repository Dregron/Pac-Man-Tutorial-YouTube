package com.dregronprogram.game_state;

import static com.dregronprogram.game_state.Direction.DOWN;
import static com.dregronprogram.game_state.Direction.LEFT;
import static com.dregronprogram.game_state.Direction.RIGHT;
import static com.dregronprogram.game_state.Direction.STOP;
import static com.dregronprogram.game_state.Direction.UP;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.display.Display;
import com.dregronprogram.utils.SpriteAnimation;
import com.dregronprogram.utils.TickTimer;
import com.dregronprogram.utils.Vector2;

public class Player implements Renderer, KeyListener {
	
	private SpriteAnimation playerAnimation;
	private BufferedImage healthSprite;
	private Rectangle rectangle;
	private Direction currentDirection, queuedDirection;
	private int speed;
	
	private Map<Vector2, Block> blocks = new HashMap<>();
	private Map<Vector2, Food> foods = new HashMap<>();
	private Map<Vector2, PowerUp> powerUps = new HashMap<>();
	
	private Font scoreFont = new Font("Arial", Font.ITALIC, 18);
	private int SCORE = 0;
	private int addScore;
	private int health;
	private boolean superPacMan;
	private TickTimer superTimer;
	
	public Player(List<BufferedImage> playerImg) {
		this.playerAnimation = new SpriteAnimation(0, 0, 8, playerImg);
		this.healthSprite = playerImg.get(1);
		this.rectangle = new Rectangle(0, 0, playerImg.get(0).getWidth(), playerImg.get(0).getHeight());
		this.speed = 2;
		this.health = 3;
		this.currentDirection = RIGHT;
	
		this.playerAnimation.setLoop(true);
		this.superTimer = new TickTimer(300);
	}

	@Override
	public void update(double delta) {
		if (isAboutToCollide()) {
			currentDirection = STOP;
		}
		
		switch (currentDirection) {
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
			case STOP:
				break;
		}
		attemptChangeDirection();
		eatFood();
		powerLogic(delta);
 		playerAnimation.update(delta);
		
		if (getRectangle().x > Display.WIDTH) {
			getRectangle().x = -getRectangle().width;
		} else if (getRectangle().x <= -getRectangle().width) {
			getRectangle().x = Display.WIDTH;
		}
		
		if (addScore > 0) {
			SCORE++;
			addScore--;
		}
	}

	private void powerLogic(double delta) {
		for (PowerUp powerUp : getAdjacentPowerUp())  {
			if (getRectangle().intersects(powerUp.getxPos(), powerUp.getyPos(), powerUp.getWidth(), powerUp.getHeight())) {
				addScore += 15;
				powerUps.remove(new Vector2(powerUp.getxPos(), powerUp.getyPos(), 5, 5));
				setSuperPacMan(true);
			}
		}
		
		if (isSuperPacMan()) {
			superTimer.tick(delta);
			if (superTimer.isEventReady()) {
				setSuperPacMan(false);
			}
		}
	}
	
	private void eatFood() {
		for (Food food : getAdjacentFoods())  {
			if (getRectangle().intersects(food.getxPos(), food.getyPos(), food.getWidth(), food.getHeight())) {
				addScore += 5;
				foods.remove(new Vector2(food.getxPos(), food.getyPos(), 5, 5));
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.YELLOW);
		g.setFont(scoreFont);
		String txtScore = "Score: " + SCORE;
		g.drawString(txtScore, (Display.WIDTH/2)-(g.getFontMetrics().stringWidth(txtScore)/2), g.getFontMetrics().getHeight());
		
		for (int i = 0; i < health; i++) {
			g.drawImage(healthSprite, i*(healthSprite.getWidth()), Display.HEIGHT-(healthSprite.getHeight()-5), healthSprite.getWidth()-10, healthSprite.getHeight()-10, null);
		}
		
		playerAnimation.draw(g, getRectangle().x, getRectangle().y, getRectangle().width, getRectangle().height);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			queuedDirection = LEFT;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			queuedDirection = RIGHT;
		} else if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			queuedDirection = UP;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			queuedDirection = DOWN;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	public void setXPos(int xPos) {
		this.rectangle.x = xPos;
	}
	
	public void setYPos(int yPos) {
		this.rectangle.y = yPos;
	}
	
	public void setBlocks(Map<Vector2, Block> blocks) {
		this.blocks = blocks;
	}
	
	public Map<Vector2, Block> getBlocks() {
		return blocks;
	}
	
	public void setPowerUps(Map<Vector2, PowerUp> powerUps) {
		this.powerUps = powerUps;
	}
	
	public void setFoods(Map<Vector2, Food> foods) {
		this.foods = foods;
	}
	
	private List<Food> getAdjacentFoods() {
		List<Food> bs = new ArrayList<>();
		Vector2 vector = new Vector2(30, 28);
		vector.set(getRectangle().x+getRectangle().width, getRectangle().y);
		addAdjacentFood(bs, vector);
		vector.set(getRectangle().x-getRectangle().width, getRectangle().y);
		addAdjacentFood(bs, vector);
		vector.set(getRectangle().x, getRectangle().y+getRectangle().height);
		addAdjacentFood(bs, vector);
		vector.set(getRectangle().x, getRectangle().y-getRectangle().height);
		addAdjacentFood(bs, vector);
		return bs;
	}
	
	private List<PowerUp> getAdjacentPowerUp() {
		List<PowerUp> bs = new ArrayList<>();
		Vector2 vector = new Vector2(30, 28);
		vector.set(getRectangle().x+getRectangle().width, getRectangle().y);
		addAdjacentPowerUp(bs, vector);
		vector.set(getRectangle().x-getRectangle().width, getRectangle().y);
		addAdjacentPowerUp(bs, vector);
		vector.set(getRectangle().x, getRectangle().y+getRectangle().height);
		addAdjacentPowerUp(bs, vector);
		vector.set(getRectangle().x, getRectangle().y-getRectangle().height);
		addAdjacentPowerUp(bs, vector);
		return bs;
	}

	private void addAdjacentFood(List<Food> bs, Vector2 vector) {
		Food food = foods.get(vector);
		if (food != null) {
			bs.add(food);
		}
	}
	
	private void addAdjacentPowerUp(List<PowerUp> bs, Vector2 vector) {
		PowerUp powerUp = powerUps.get(vector);
		if (powerUp != null) {
			bs.add(powerUp);
		}
	}
	
	private List<Block> getAdjacentBlocks() {
		List<Block> bs = new ArrayList<>();
		Vector2 vector = new Vector2(30, 28);
		vector.set(getRectangle().x+getRectangle().width, getRectangle().y);
		addAdjacentBlock(bs, vector);
		vector.set(getRectangle().x-getRectangle().width, getRectangle().y);
		addAdjacentBlock(bs, vector);
		vector.set(getRectangle().x, getRectangle().y+getRectangle().height);
		addAdjacentBlock(bs, vector);
		vector.set(getRectangle().x, getRectangle().y-getRectangle().height);
		addAdjacentBlock(bs, vector);
		
		vector.set(getRectangle().x+getRectangle().width, getRectangle().y+getRectangle().height);
		addAdjacentBlock(bs, vector);
		vector.set(getRectangle().x-getRectangle().width, getRectangle().y-getRectangle().height);
		addAdjacentBlock(bs, vector);
		vector.set(getRectangle().x-getRectangle().width, getRectangle().y+getRectangle().height);
		addAdjacentBlock(bs, vector);
		vector.set(getRectangle().x+getRectangle().width, getRectangle().y-getRectangle().height);
		addAdjacentBlock(bs, vector);
		return bs;
	}

	private void addAdjacentBlock(List<Block> bs, Vector2 vector) {
		Block block = blocks.get(vector);
		if (block != null) {
			bs.add(block);
		}
	}
	
	private boolean isAboutToCollide() {
			switch (currentDirection) {
			case LEFT:
				for (Block b : getAdjacentBlocks()) {
					if (getRectangle().intersects(b.getxPos()+1, b.getyPos(), b.getWidth(), b.getHeight())) {
						return true;
					}
				}
				break;
			case RIGHT:
				for (Block b : getAdjacentBlocks()) {
					if (getRectangle().intersects(b.getxPos()-1, b.getyPos(), b.getWidth(), b.getHeight())) {
						return true;
					}
				}
				break;
			case UP:
				for (Block b : getAdjacentBlocks()) {
					if (getRectangle().intersects(b.getxPos(), b.getyPos()+1, b.getWidth(), b.getHeight())) {
						return true;
					}
				}
				break;
			case DOWN:
				for (Block b : getAdjacentBlocks()) {
					if (getRectangle().intersects(b.getxPos(), b.getyPos()-1, b.getWidth(), b.getHeight())) {
						return true;
					}
				}
				break;
			case STOP:
				break;
			default:
				break;
			}
		return false;
	}
	
	private boolean attemptChangeDirection() {
		if (changeDirection()) {
			currentDirection = queuedDirection;
			queuedDirection = null;
			switch (currentDirection) {
				case LEFT:
					playerAnimation.setRotation(Math.toDegrees(.2f));
					break;
				case RIGHT:
					playerAnimation.setRotation(Math.toDegrees(0));
					break;
				case UP:
					playerAnimation.setRotation(Math.toDegrees(.3f));
					break;
				case DOWN:
					playerAnimation.setRotation(Math.toDegrees(.1f));
					break;
			case STOP:
				break;
			default:
				break;
			
			}
			return true;
		}
		return false;
	}
	
	private boolean changeDirection() {
		if (queuedDirection == null || getBlocks().isEmpty()) return false;
		
		switch (queuedDirection) {
			case LEFT:
				for (Block b : getAdjacentBlocks()) {
					if (getRectangle().intersects(b.getxPos()+b.getWidth(), b.getyPos(), b.getWidth(), b.getHeight())) {
						return false;
					}
				}
				break;
			case RIGHT:
				for (Block b : getAdjacentBlocks()) {
					if (getRectangle().intersects(b.getxPos()-b.getWidth(), b.getyPos(), b.getWidth(), b.getHeight())) {
						return false;
					}
				}
				break;
			case UP:
				for (Block b : getAdjacentBlocks()) {
					if (getRectangle().intersects(b.getxPos(), b.getyPos()+b.getHeight(), b.getWidth(), b.getHeight())) {
						return false;
					}
				}
				break;
			case DOWN:
				for (Block b : getAdjacentBlocks()) {
					if (getRectangle().intersects(b.getxPos(), b.getyPos()-b.getHeight(), b.getWidth(), b.getHeight())) {
						return false;
					}
				}
				break;
		case STOP:
			break;
		default:
			break;
		}
		return true;
	}
	
	public boolean isSuperPacMan() {
		return superPacMan;
	}
	
	public void setSuperPacMan(boolean superPacMan) {
		this.superPacMan = superPacMan;
	}

	public int getHealth() {
		return health;
	}
}
