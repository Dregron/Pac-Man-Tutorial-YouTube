package com.dregronprogram.game_state.level;

import com.dregronprogram.game_state.*;
import com.dregronprogram.game_state.a_star.Node;
import com.dregronprogram.game_state.ghost.Ghost;
import com.dregronprogram.tiled_map.Tiled;
import com.dregronprogram.tiled_map.TiledMap;
import com.dregronprogram.tiled_map.Tileset;
import com.dregronprogram.utils.SpriteAnimation;
import com.dregronprogram.utils.Vector2;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;

public abstract class Level {

	private TiledMap tiledmap;
	private Player player;

	private List<Ghost> ghosts = new ArrayList<>();
	private Map<Integer, BufferedImage> spriteSheet;
	private Map<Vector2, Node> nodes = new HashMap<>();
	private Map<Vector2, Block> blocks = new HashMap<>();
	private Map<Vector2, Food> foods = new HashMap<>();
	private Map<Vector2, PowerUp> powerUps = new HashMap<>();

	public Level(Map<Integer, BufferedImage> spriteSheet, Player player) {
		this.spriteSheet = spriteSheet;
		this.player = player;
	}

	public abstract void update(double delta);
	public void draw(Graphics2D g) {
		getPlayer().draw(g);
		if (GameState.debugMode) {
			for (Node node : getNodes().values()) {
				node.draw(g);
			}
		}
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
	public abstract boolean levelReady();
	
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
		if (property != null && property.containsKey("Ghost")) {

			Ghost ghost = new Ghost(xPos, yPos
					, getTiles().getTilewidth()
					, getTiles().getTileheight()
					, nodes);
			int speed = 12;
			ghost.setLeftAnimation(new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(30), getSpriteSheet().get(31), getSpriteSheet().get(32))));
			ghost.setRightAnimation(new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(33), getSpriteSheet().get(34), getSpriteSheet().get(35))));
			ghost.setDownAnimation(new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(42), getSpriteSheet().get(43), getSpriteSheet().get(44))));
			ghost.setUpAnimation(new SpriteAnimation(xPos, yPos, speed, Arrays.asList(getSpriteSheet().get(45), getSpriteSheet().get(46), getSpriteSheet().get(47))));
			ghost.getLeftAnimation().setLoop(true);
			ghost.getRightAnimation().setLoop(true);
			ghost.getDownAnimation().setLoop(true);
			ghost.getUpAnimation().setLoop(true);

			ghosts.add(ghost);
		}
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
}
