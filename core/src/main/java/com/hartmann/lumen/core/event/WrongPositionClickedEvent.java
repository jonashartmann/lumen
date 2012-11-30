package com.hartmann.lumen.core.event;

import com.hartmann.lumen.core.tools.event.MvpEvent;

public class WrongPositionClickedEvent implements MvpEvent {

	private static final long serialVersionUID = 5949387972902157851L;

	private float x;
	private float y;

	public WrongPositionClickedEvent(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	@Override
	public String getEventName() {
		return "";
	}
}
