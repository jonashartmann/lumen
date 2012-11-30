package com.hartmann.lumen.core.model;

/**
 * Represents a grid.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
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

	/**
	 * Gets the {@link GridPosition} at the specified x and y. If the indexes
	 * are invalid, then it will return null.
	 * 
	 * @param x
	 *            - The index of the first dimension
	 * @param y
	 *            - The index of the second dimension
	 * @return Returns the position found or null if none was found.
	 */
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
