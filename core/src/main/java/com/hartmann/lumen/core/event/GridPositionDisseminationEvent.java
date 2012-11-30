package com.hartmann.lumen.core.event;

import com.hartmann.lumen.core.tools.event.MvpEvent;

public class GridPositionDisseminationEvent implements MvpEvent {

	private static final long serialVersionUID = -2367143833847433754L;

	private final int x;
	private final int y;
	private final int fortification;
	private final int playerId;

	public GridPositionDisseminationEvent(int x, int y, int fortification,
			int playerId) {
		this.x = x;
		this.y = y;
		this.fortification = fortification;
		this.playerId = playerId;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getFortification() {
		return fortification;
	}

	public int getPlayerId() {
		return playerId;
	}

	@Override
	public String getEventName() {
		return "";
	}
}
