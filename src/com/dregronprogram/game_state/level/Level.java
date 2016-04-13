package com.dregronprogram.game_state.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dregronprogram.game_state.Block;
import com.dregronprogram.game_state.Food;
import com.dregronprogram.game_state.GameState;
import com.dregronprogram.game_state.Player;
import com.dregronprogram.game_state.PowerUp;
import com.dregronprogram.game_state.a_star.Node;
import com.dregronprogram.game_state.ghost.Ghost;
import com.dregronprogram.tiled_map.Tiled;
import com.dregronprogram.tiled_map.TiledMap;
import com.dregronprogram.tiled_map.Tileset;
import com.dregronprogram.utils.SpriteAnimation;
import com.dregronprogram.utils.Vector2;

public abstract class Level {

	private boolean pause;
	private TiledMap tiledmap;
	private Player player;
	private boolean ready;

	private List<Ghost> ghosts = new ArrayList<>();
	private Map<Integer, BufferedImage> spriteSheet;
	private Map<Vector2, Node> nodes = new HashMap<>();
	private Map<Vector2, Block> blocks = new HashMap<>();
	private Map<Vector2, Food> foods = new HashMap<>();
	private Map<Vector2, PowerUp> powerUps = new HashMap<>();

	public Level(Map<Integer, BufferedImage> spriteSheet, Player player) {
		this.spriteSheet = spriteSheet;
		this.player = player;
		this.ready = false;
	}

	public abstract void update(double delta);
	public void draw(Graphics2D g) {
		
		if (GameState.debugMode) {
			for (Node node : getNodes().values()) {
				node.draw(g);
			}
		}
		
		getPlayer().draw(g);
		
		for (Food food : getFoods().values()) {
			food.draw(g);
		}
		for (PowerUp powerUp : getPowerUps().values()) {
			powerUp.draw(g);
		}
		for (Ghost ghost : getGhosts()) {
			ghost.draw(g);
		}
		for (Block block : getBlocks().values()) {
			block.draw(g);
		}
	}
	
	public abstract boolean isComplete();
	public abstract boolean isGameOver();
	
	public abstract void beginLevel();

	public Map<Vector2, Node> getNodes() {
		return nodes;
	}

	public Player getPlayer() {
		return player;
	}

	public List<Ghost> getGhosts() {
		return ghosts;
	}

	public Map<Vector2, Block> getBlocks() {
		return blocks;
	}

	public Map<Vector2, Food> getFoods() {
		return foods;
	}

	public Map<Vector2, PowerUp> getPowerUps() {
		return powerUps;
	}

	public void setTiledmap(TiledMap tiledmap) {
		this.tiledmap = tiledmap;
	}

	public TiledMap getTiledmap() {
		return tiledmap;
	}

	public Tiled getTiles() {
		return tiledmap.getTiled();
	}

	protected void initMapLayer(int dataNum, Tileset tileset, int y, int x, int xPos, int yPos) {
		Map<String, String> property = tileset.getTileproperties().get(dataNum - 1);

		if (doesContainValue(property, "square")) {
			getBlocks().put(new Vector2(xPos, yPos, getTiles().getTilewidth() - 2, getTiles().getTileheight() - 2)
					, new Block(xPos, yPos, getTiles().getTilewidth(), getTiles().getTileheight(), spriteSheet.get(dataNum - 1)));
		}
		if (doesContainValue(property, "food")) {
			getFoods().put(new Vector2((x * getTiles().getTilewidth()) + (getTiles().getTilewidth() / 2), (y * getTiles().getTileheight()) + (getTiles().getTileheight() / 2), 5, 5),
					new Food((x * getTiles().getTilewidth()) + (getTiles().getTilewidth() / 2), (y * getTiles().getTileheight()) + (getTiles().getTileheight() / 2), 5, 5, spriteSheet.get(dataNum - 1)));
		}
		if (doesContainValue(property, "powerUp")) {
			getPowerUps().put(new Vector2((x * getTiles().getTilewidth()) + (getTiles().getTilewidth() / 2), (y * getTiles().getTileheight()) + (getTiles().getTileheight() / 2), 5, 5),
					new PowerUp((x * getTiles().getTilewidth()) + (getTiles().getTilewidth() / 2), (y * getTiles().getTileheight()) + (getTiles().getTileheight() / 2), 5, 5, spriteSheet.get(dataNum - 1)));
		}
		if (doesContainValue(property, "player")) {
			player.setXPos(xPos);
			player.setYPos(yPos);
		}
	}

	protected void initGhostLayer(int dataNum, Tileset tileset, int xPos, int yPos) {
		Map<String, String> property = tileset.getTileproperties().get(dataNum-1);

		Ghost ghost = new Ghost(this, xPos, yPos
				, getTiles().getTilewidth()
				, getTiles().getTileheight()
				, nodes, getPlayer(), getGhosts().size() * 120);
		int speed = 12;

		if (property != null && property.containsKey("redGhost")) {

			setSpriteSheet(ghost, new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(30), getSpriteSheet().get(31), getSpriteSheet().get(32))), new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(33), getSpriteSheet().get(34), getSpriteSheet().get(35))), new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(42), getSpriteSheet().get(43), getSpriteSheet().get(44))), new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(45), getSpriteSheet().get(46), getSpriteSheet().get(47))));
			ghost.setScaredGhost(new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(18), getSpriteSheet().get(19), getSpriteSheet().get(20))));
			ghost.getScaredGhost().setLoop(true);
			setDeadSprite(ghost);
			ghosts.add(ghost);
		}

		if (property != null && property.containsKey("blueGhost")) {

			setSpriteSheet(ghost,
					new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(54), getSpriteSheet().get(55), getSpriteSheet().get(56))),
					new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(57), getSpriteSheet().get(58), getSpriteSheet().get(59))),
					new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(66), getSpriteSheet().get(67), getSpriteSheet().get(68))),
					new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(69), getSpriteSheet().get(70), getSpriteSheet().get(71))));
			ghost.setScaredGhost(new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(18), getSpriteSheet().get(19), getSpriteSheet().get(20))));
			ghost.getScaredGhost().setLoop(true);
			setDeadSprite(ghost);
			ghosts.add(ghost);
		}

		if (property != null && property.containsKey("yellowGhost")) {

			setSpriteSheet(ghost,
					  new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(78), getSpriteSheet().get(79), getSpriteSheet().get(80)))
					, new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(81), getSpriteSheet().get(82), getSpriteSheet().get(83)))
					, new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(90), getSpriteSheet().get(91), getSpriteSheet().get(92)))
					, new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(93), getSpriteSheet().get(94), getSpriteSheet().get(95))));
			ghost.setScaredGhost(new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(18), getSpriteSheet().get(19), getSpriteSheet().get(20))));
			ghost.getScaredGhost().setLoop(true);
			setDeadSprite(ghost);
			ghosts.add(ghost);
		}

		if (property != null && property.containsKey("pinkGhost")) {

			setSpriteSheet(ghost,
					new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(102), getSpriteSheet().get(103), getSpriteSheet().get(104)))
					, new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(105), getSpriteSheet().get(106), getSpriteSheet().get(107)))
					, new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(114), getSpriteSheet().get(115), getSpriteSheet().get(116)))
					, new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(117), getSpriteSheet().get(118), getSpriteSheet().get(119))));
			ghost.setScaredGhost(new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(18), getSpriteSheet().get(19), getSpriteSheet().get(20))));
			ghost.getScaredGhost().setLoop(true);
			setDeadSprite(ghost);
			ghosts.add(ghost);
		}
	}

	public void setDeadSprite(Ghost ghost) {
		ghost.setLeftDeadGhost(getSpriteSheet().get(40));
		ghost.setRightDeadGhost(getSpriteSheet().get(41));
		ghost.setDownDeadGhost(getSpriteSheet().get(52));
		ghost.setUpDeadGhost(getSpriteSheet().get(53));
	}

	private void setSpriteSheet(Ghost ghost, SpriteAnimation leftAnimation, SpriteAnimation rightAnimation, SpriteAnimation downAnimation, SpriteAnimation upAnimation) {
		ghost.setLeftAnimation(leftAnimation);
		ghost.setRightAnimation(rightAnimation);
		ghost.setDownAnimation(downAnimation);
		ghost.setUpAnimation(upAnimation);

		ghost.getLeftAnimation().setLoop(true);
		ghost.getRightAnimation().setLoop(true);
		ghost.getDownAnimation().setLoop(true);
		ghost.getUpAnimation().setLoop(true);
	}

	protected void initFloorLayer(int dataNum, Tileset tileset, int xPos, int yPos) {
		Map<String, String> property = tileset.getTileproperties().get(dataNum-1);
		if (property != null && property.containsKey("Floor")) {
			Node node = new Node(xPos, yPos, getTiles().getTilewidth(), getTiles().getTileheight(), "floor");
			nodes.put(new Vector2(xPos, yPos, 0, 0), node);
			if (property.get("Floor").equalsIgnoreCase("adjacentLeft")) {
				node.setAdjacentLeftFloor(true);
			} else if (property.get("Floor").equalsIgnoreCase("adjacentRight")) {
				node.setAdjacentRightFloor(true);
			}
		}
	}

	private boolean doesContainValue(Map<String, String> property, String value) {
		return property != null && property.containsValue(value);
	}

	public Map<Integer, BufferedImage> getSpriteSheet() {
		return spriteSheet;
	}

	public void enemyHit() {
		setPause(true);
	}

	protected void setPause(boolean pause) {
		this.pause = pause;
	}

	protected boolean isPause() {
		return pause;
	}
	
	public boolean levelReady() {
		return ready;
	}
	
	public void startLevel() {
		ready = true;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public void reset() {
		getGhosts().clear();
		getFoods().clear();
		getBlocks().clear();
		getPowerUps().clear();
		setTiledmap(null);
		setReady(false);
		setPause(false);

		getPlayer().reset();
	}
}
