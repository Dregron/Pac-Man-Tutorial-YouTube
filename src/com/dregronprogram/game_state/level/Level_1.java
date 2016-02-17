package com.dregronprogram.game_state.level;

import com.dregronprogram.game_state.Block;
import com.dregronprogram.game_state.Player;
import com.dregronprogram.game_state.PowerUp;
import com.dregronprogram.game_state.ghost.Ghost;
import com.dregronprogram.tiled_map.Layer;
import com.dregronprogram.tiled_map.TiledMap;
import com.dregronprogram.tiled_map.Tileset;
import com.dregronprogram.utils.TickTimer;

import java.awt.image.BufferedImage;
import java.util.Map;

public class Level_1 extends Level {

	private Ghost resetGhost;
	private boolean ready, pause;
	private TickTimer spawn, pauseTimer;

	public Level_1(Map<Integer, BufferedImage> spriteSheet, Player player) {
		super(spriteSheet, player);
		this.ready = false;
		this.pause = false;
		this.pauseTimer = new TickTimer(60);
	}
	
	@Override
	public void update(double delta) {
		if (pause) {
			pauseTimer.tick(delta);
			if (pauseTimer.isEventReady()) {
				setPause(false);
				resetGhost = null;
			} else {
				resetGhost.update(delta);
			}
			return;
		}
		
		getPlayer().update(delta);
		
		spawn.tick(delta);
		for (Ghost ghost : getGhosts()) {
			ghost.update(delta);
		}
		
		for (Block block : getBlocks().values()) {
			block.update(delta);
		}
		
		for (PowerUp powerUp : getPowerUps().values()) {
			powerUp.update(delta);
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
		
		setTiledmap(new TiledMap("../levels/Level1_Map.json"));

		Layer ghostLayer = getTiledmap().getTiled().getLayer("Ghosts");
		Layer mapLayer = getTiledmap().getTiled().getLayer("Map");
		Layer floorLayer = getTiledmap().getTiled().getLayer("Floor");

		int count = 0;
		Tileset tileset = getTiles().getTilesets()[0];
		for (int y = 0; y < getTiles().getHeight(); y++) {
			for (int x = 0; x < getTiles().getWidth(); x++) {

				int xPos = x*getTiles().getTilewidth();
				int yPos = y*getTiles().getTileheight();
				initMapLayer(mapLayer.getData()[count], tileset, y, x, xPos, yPos);
				initGhostLayer(ghostLayer.getData()[count], tileset, xPos, yPos);
				initFloorLayer(floorLayer.getData()[count], tileset, xPos, yPos);

				count++;
			}
		}

		getPlayer().setBlocks(getBlocks());
		getPlayer().setFoods(getFoods());
		getPlayer().setPowerUps(getPowerUps());
		ready = true;
		spawn = new TickTimer(300);
	}

	@Override
	public boolean levelReady() {
		return ready;
	}
	
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	public boolean isPause() {
		return pause;
	}
}
