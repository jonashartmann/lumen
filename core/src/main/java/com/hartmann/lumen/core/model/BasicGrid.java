package com.hartmann.lumen.core.model;

/**
 * Basic implementation of a grid.
 * 
 * @author Jonas
 * 
 */
public class BasicGrid extends Grid {

	public BasicGrid(int width, int height) {
		super(width, height);
	}

	@Override
	protected void createPositions() {
		for (int w = 0; w < getWidth(); w++) {
			for (int h = 0; h < getHeight(); h++) {
				setGridPosition(new GridPosition(w, h));
			}
		}
	}

}
