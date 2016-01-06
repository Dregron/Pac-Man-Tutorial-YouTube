package com.dregronprogram.game_state.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
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
import com.dregronprogram.tiled_map.Layer;
import com.dregronprogram.tiled_map.Property;
import com.dregronprogram.tiled_map.Tiled;
import com.dregronprogram.tiled_map.TiledMap;
import com.dregronprogram.tiled_map.Tileset;
import com.dregronprogram.utils.TickTimer;
import com.dregronprogram.utils.Vector2;

public class Level_1 implements Level {

	private TiledMap tiledmap;
	private Map<Integer, BufferedImage> spriteSheet;
	private Map<Vector2, Block> blocks = new HashMap<Vector2, Block>();
	private Map<Vector2, Node> nodes = new HashMap<Vector2, Node>();
	private Map<Vector2, Food> foods = new HashMap<Vector2, Food>();
	private Map<Vector2, PowerUp> powerUps = new HashMap<Vector2, PowerUp>();
	private List<Ghost> ghosts = new ArrayList<Ghost>();
	private Player player;
	private boolean ready;
	private TickTimer spawn;
	
	public Level_1(Map<Integer, BufferedImage> spriteSheet, Player player) {
		this.spriteSheet = spriteSheet;
		this.player = player;
		this.ready = false;
	}
	
	@Override
	public void update(double delta) {
		
		for (Ghost ghost : ghosts) {
			ghost.update(delta);
		}
		
		spawn.tick(delta);
		if (spawn.isEventReady()) {
			for (Ghost ghost : ghosts) {
				if (!ghost.isStart()) {
					ghost.setStart(true);
					break;
				}
			}
		}
		
		for (Block block : blocks.values()) {
			block.update(delta);
		}
		
		for (PowerUp powerUp : powerUps.values()) {
			powerUp.update(delta);
		}
	}

	@Override
	public void draw(Graphics2D g) {
		
		if (GameState.debugMode) {
			for (Node node : nodes.values()) {
				node.draw(g);
			}
		}
		
		for (Food food : foods.values()) {
			food.draw(g);
		}
		
		for (PowerUp powerUp : powerUps.values()) {
			powerUp.draw(g);
		}
		
		for (Ghost ghost : ghosts) {
			ghost.draw(g);
		}
		
		for (Block block : blocks.values()) {
			block.draw(g);
		}
	}

	@Override
	public boolean isComplete() {
		return false;
	}

	@Override
	public boolean isGameOver() {
		return false;
	}

	@Override
	public void beginLevel() {
		
		this.tiledmap = new TiledMap("../levels/Level1_Map.json");
		
		int counter = 0;
		Tileset tileset = getTiles().getTilesets()[0];
		for (int y = 0; y < getTiles().getHeight(); y++) {
			for (int x = 0; x < getTiles().getWidth(); x++) {
				
				for (Layer layer : getTiles().getLayers()) {
					
					if (layer.getName().equals("Map")) {
						int dataNum = layer.getData()[counter];
						Property property = tileset.getTileproperties().get(dataNum-1);
						if (property != null && property.getValue().equalsIgnoreCase("square")) {
							int xPos = x*getTiles().getTileWidth();
							int yPos = y*getTiles().getTileHeight();
							blocks.put(new Vector2(xPos, yPos, 30, 28)
										, new Block(xPos, yPos, getTiles().getTileWidth(), getTiles().getTileHeight(), spriteSheet.get(dataNum-1)));
						}
						if (property != null && property.getValue().equalsIgnoreCase("food")) {
							foods.put(new Vector2((x*getTiles().getTileWidth()) + (getTiles().getTileWidth()/2), (y*getTiles().getTileHeight())+(getTiles().getTileHeight()/2), 5, 5), 
									new Food((x*getTiles().getTileWidth()) + (getTiles().getTileWidth()/2), (y*getTiles().getTileHeight())+(getTiles().getTileHeight()/2), 5, 5, spriteSheet.get(dataNum-1)));
						}
						if (property != null && property.getValue().equalsIgnoreCase("powerUp")) {
							powerUps.put(new Vector2((x*getTiles().getTileWidth()) + (getTiles().getTileWidth()/2), (y*getTiles().getTileHeight())+(getTiles().getTileHeight()/2), 5, 5), 
									new PowerUp((x*getTiles().getTileWidth()) + (getTiles().getTileWidth()/2), (y*getTiles().getTileHeight())+(getTiles().getTileHeight()/2), 5, 5, spriteSheet.get(dataNum-1)));
						}
						if (property != null && property.getValue().equalsIgnoreCase("player")) {
							player.setXPos(x*getTiles().getTileWidth());
							player.setYPos(y*getTiles().getTileHeight());
						}
					} else if (layer.getName().equals("Ghosts")) {
						int dataNum = layer.getData()[counter];
						Property property = tileset.getTileproperties().get(dataNum-1);
						if (property != null && property.getId().equalsIgnoreCase("Ghost")) {
							Ghost ghost = new Ghost(  x*getTiles().getTileWidth()
													, y*getTiles().getTileHeight()
													, getTiles().getTileWidth()
													, getTiles().getTileHeight()
													, spriteSheet.get(Integer.valueOf(property.getValue()))
													, nodes);
							ghosts.add(ghost);
						}
					} else if (layer.getName().equals("Floor")) {
						int dataNum = layer.getData()[counter];
						Property property = tileset.getTileproperties().get(dataNum-1);
						if (property != null && property.getId().equalsIgnoreCase("floor")) {
							Node node = new Node(x*getTiles().getTileWidth()
									, y*getTiles().getTileHeight()
									, getTiles().getTileWidth()
									, getTiles().getTileHeight(), "floot");
							
							nodes.put(new Vector2(x*getTiles().getTileWidth()
												, y*getTiles().getTileHeight()
												, getTiles().getTileWidth()-2
												, getTiles().getTileHeight()-2)
									  , node);
							if (property.getValue().equalsIgnoreCase("adjacentLeft")) {
								node.setAdjacentLeftFloor(true);
							} else if (property.getValue().equalsIgnoreCase("adjacentRight")) {
								node.setAdjacentRightFloor(true);
							} 
						}
					}
				}
				
				counter++;
			}
		}
		
		player.setBlocks(blocks);
		player.setFoods(foods);
		player.setPowerUps(powerUps);
		for (Ghost ghost : ghosts) {
			ghost.setPlayer(player);
		}
		for (Block block : blocks.values()) {
			block.setPlayer(player);
		}
		ready = true;
		spawn = new TickTimer(300);
	}
	
	private Tiled getTiles() {
		return tiledmap.getTiled();
	}

	@Override
	public boolean levelReady() {
		return ready;
	}
}
