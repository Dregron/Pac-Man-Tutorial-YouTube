package com.dregronprogram.state;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.dregronprogram.game_state.GameState;
import com.dregronprogram.menu_state.MenuState;
import com.dregronprogram.splash_state.SplashState;

public class StateMachine {

	private final Map<StateId, State> states = new HashMap<StateId, State>();
	private final Canvas canvas;
	private StateId currentState;
	
	public StateMachine(Canvas canvas){
		this.canvas = canvas;
		
		GameState gameState = new GameState(this);
		MenuState menuState = new MenuState(this);
		SplashState splashState = new SplashState(this);
		getStates().put(StateId.GAME, gameState);
		getStates().put(StateId.MENU, menuState);
		getStates().put(StateId.SPLASH, splashState);
		setState(StateId.MENU);
	}
	
	public void draw(Graphics2D g){
		getStates().get(currentState).draw(g);
	}
	
	public void update(double delta){
		getStates().get(currentState).update(delta);
	}
	
	public void setState(StateId i){
		for(int r = 0; r < canvas.getKeyListeners().length; r++) {
			canvas.removeKeyListener(canvas.getKeyListeners()[r]);
		}
		currentState = i;
		getStates().get(currentState).init(canvas);
	}

	private Map<StateId, State> getStates() {
		return states;
	}
}
