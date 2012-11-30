package com.hartmann.lumen.core.model;


/**
 * Represents a grid.
 * 
 * @author Jonas
 * 
 */
public abstract class Grid {

	private final int width;
	private final int height;
	private final GridPosition[][] positions;

	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		positions = new GridPosition[width][height];
		createPositions();
	}

	abstract protected void createPositions();

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public GridPosition getGridPosition(int x, int y) {
		if (!isValidPosition(x, y)) {
			return null;
		}

		return positions[x][y];
	}

	protected boolean setGridPosition(GridPosition position) {
		if (isValidPosition(position.getX(), position.getY())) {
			positions[position.getX()][position.getY()] = position;
			return true;
		}
		return false;
	}

	private boolean isValidPosition(int x, int y) {
		if ((x < width && x >= 0) && (y >= 0 && y < height)) {
			return true;
		}
		return false;
	}

	public GridPosition[][] getPositions() {
		return positions;
	}
}
