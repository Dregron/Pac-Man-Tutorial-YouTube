package com.dregronprogram.game_state.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dregronprogram.application.Vector2;
import com.dregronprogram.game_state.Block;
import com.dregronprogram.game_state.Food;
import com.dregronprogram.game_state.Player;
import com.dregronprogram.tiled_map.Layer;
import com.dregronprogram.tiled_map.Property;
import com.dregronprogram.tiled_map.Tiled;
import com.dregronprogram.tiled_map.TiledMap;
import com.dregronprogram.tiled_map.Tileset;

public class Level_1 implements Level {

	private TiledMap tiledmap;
	private Map<Integer, BufferedImage> spriteSheet;
	private Map<Vector2, Block> blocks = new HashMap<Vector2, Block>();
	private List<Food> foods = new ArrayList<Food>();
	private Player player;
	
	public Level_1(Map<Integer, BufferedImage> spriteSheet, Player player) {
		this.spriteSheet = spriteSheet;
		this.player = player;
	}
	
	@Override
	public void update(double delta) {
		
	}

	@Override
	public void draw(Graphics2D g) {
		
		for (Block block : blocks.values()) {
			block.draw(g);
		}
		
		for (Food food : foods) {
			food.draw(g);
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
		long startTime = System.nanoTime();
		this.tiledmap = new TiledMap("../levels/Level1_Map.json");
		
		int counter = 0;
		Layer layer = getTiles().getLayers()[0];
		Tileset tileset = getTiles().getTilesets()[0];
		for (int y = 0; y < getTiles().getHeight(); y++) {
			for (int x = 0; x < getTiles().getWidth(); x++) {
				
				int dataNum = layer.getData()[counter];
				Property property = tileset.getTileproperties().get(dataNum-1);
				if (property != null && property.getValue().equalsIgnoreCase("square")) {
					blocks.put(new Vector2(x*getTiles().getTileWidth(), y*getTiles().getTileHeight())
								, new Block(x*getTiles().getTileWidth(), y*getTiles().getTileHeight(), getTiles().getTileWidth(), getTiles().getTileHeight(), spriteSheet.get(dataNum-1)));
				}
				if (property != null && property.getValue().equalsIgnoreCase("food")) {
					foods.add(new Food(x*getTiles().getTileWidth(), y*getTiles().getTileHeight(), getTiles().getTileWidth(), getTiles().getTileHeight(), spriteSheet.get(dataNum-1)));
				}
				if (property != null && property.getValue().equalsIgnoreCase("player")) {
					player.setXPos(x*getTiles().getTileWidth());
					player.setYPos(y*getTiles().getTileHeight());
				}
				
				counter++;
			}
		}
		player.setBlocks(blocks);
		System.err.println((System.nanoTime() - startTime));
	}
	
	private Tiled getTiles() {
		return tiledmap.getTiled();
	}
}
