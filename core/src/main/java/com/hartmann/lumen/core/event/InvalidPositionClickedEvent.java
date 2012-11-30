package com.hartmann.lumen.core.event;

import com.hartmann.lumen.core.tools.event.MvpEvent;

public class InvalidPositionClickedEvent implements MvpEvent {

	private static final long serialVersionUID = 4187139203904617372L;

	private final float x;
	private final float y;

	public InvalidPositionClickedEvent(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
	
	@Override
	public String getEventName() {
		return "";
	}

}
