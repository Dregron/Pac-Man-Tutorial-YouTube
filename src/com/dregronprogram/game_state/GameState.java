package com.dregronprogram.game_state;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;

import com.dregronprogram.application.ApplicationResources;
import com.dregronprogram.state.State;
import com.dregronprogram.state.StateMachine;
import com.dregronprogram.tiled_map.TiledMap;

public class GameState extends State {

	private final TiledMap tiledmap;
	
	public GameState(StateMachine stateMachine) {
		super(stateMachine);
		
		this.tiledmap = new TiledMap("../levels/Level1_Map.json");
	}
	
	@Override
	public void update(double delta, ApplicationResources applicationResources) {
	}

	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.WHITE);
		getTiledmap().draw(g);
	}

	@Override
	public void init(Canvas canvas) {

	}
	
	public TiledMap getTiledmap() {
		return tiledmap;
	}
}
