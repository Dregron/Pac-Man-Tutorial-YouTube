package com.dregronprogram.state;

import java.awt.Canvas;

import com.dregronprogram.application.Renderer;

public abstract class State implements Renderer {

	private StateMachine stateMachine;
	
	public State(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	public abstract void init(Canvas canvas);
	
	public StateMachine getStateMachine() {
		return stateMachine;
	}
}
