package com.dregronprogram.state;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.dregronprogram.game_state.GameState;

public class StateMachine {

	private final ArrayList<State> states = new ArrayList<State>();
	private final Canvas canvas;
	private byte selectState = 0;
	
	public StateMachine(Canvas canvas){
		GameState gameState = new GameState(this);
		states.add(gameState);
		
		this.canvas = canvas;
	}
	
	public void draw(Graphics2D g){
		states.get(selectState).draw(g);
	}
	
	public void update(double delta){
		states.get(selectState).update(delta);
	}
	
	public void setState(byte i){
		for(int r = 0; r < canvas.getKeyListeners().length; r++) {
			canvas.removeKeyListener(canvas.getKeyListeners()[r]);
		}
		selectState = i;
		states.get(selectState).init(canvas);
	}

	public byte getStates() {
		return selectState;
	}
}
