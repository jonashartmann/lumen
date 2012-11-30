package com.hartmann.lumen.core.tools.event;

/**
 * Event representing a mouse click.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public class MouseClickedEvent implements MvpEvent {

	private static final long serialVersionUID = 8946118613759777358L;

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
