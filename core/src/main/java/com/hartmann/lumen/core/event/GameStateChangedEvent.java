package com.hartmann.lumen.core.event;

import com.hartmann.lumen.core.logic.GameState;
import com.hartmann.lumen.core.tools.event.MvpEvent;

public class GameStateChangedEvent implements MvpEvent {

	private static final long serialVersionUID = -5408739859751677768L;

	private GameState newState;
	private GameState oldState;

	public GameStateChangedEvent(GameState oldState, GameState newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	public GameState getNewState() {
		return newState;
	}

	public GameState getOldState() {
		return oldState;
	}

	@Override
	public String getEventName() {
		return "Game State Changed";
	}
}
