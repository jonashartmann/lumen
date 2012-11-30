package com.hartmann.lumen.core.tools.event;

public class MouseClickedEvent implements MvpEvent {

	private final float x;
	private final float y;
	private int playerId;

	public MouseClickedEvent(float x, float y, int playerId) {
		this.x = x;
		this.y = y;
		this.playerId = playerId;
	}

	public float y() {
		return y;
	}

	public float x() {
		return x;
	}

	public int getPlayerId() {
		return playerId;
	}

	@Override
	public String getEventName() {
		return "";
	}
}
