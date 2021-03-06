package com.hartmann.lumen.core.model;

/**
 * Represents a position in a grid.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public class GridPosition {
	private final int x;
	private final int y;

	public GridPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
