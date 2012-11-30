package com.hartmann.lumen.core.model;

/**
 * Basic implementation of a grid.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
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
