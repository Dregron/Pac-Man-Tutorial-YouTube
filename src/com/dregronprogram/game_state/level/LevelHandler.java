package com.dregronprogram.game_state.level;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import com.dregronprogram.game_state.Player;

public class LevelHandler {

	private Level[] levels = new Level[1];
	private byte currentLevel;
	
	public LevelHandler(Map<Integer, BufferedImage> spriteSheet, Player player) {
		this.currentLevel = 0;
		this.levels[0] = new Level_1(spriteSheet, player);
	}
	
	public void update(double delta) {
		levels[currentLevel].update(delta);
	}
	
	public void draw(Graphics2D g) {
		levels[currentLevel].draw(g);
	}
	
	public Level getCurrentLevel() {
		return levels[currentLevel];
	}
	
	public boolean levelReady() {
		return getCurrentLevel().levelReady();
	}

	public void reset() {
		getCurrentLevel().reset();
	}
}
