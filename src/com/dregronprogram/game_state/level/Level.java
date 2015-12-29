package com.dregronprogram.game_state.level;

import java.awt.Graphics2D;

public interface Level {

	public void update(double delta);
	public void draw(Graphics2D g);
	
	public boolean isComplete();
	public boolean isGameOver();
	public boolean levelReady();
	
	public void beginLevel();
}
