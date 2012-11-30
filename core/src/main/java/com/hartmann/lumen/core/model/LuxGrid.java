package com.hartmann.lumen.core.model;

import java.util.ArrayList;
import java.util.List;

public class LuxGrid extends Grid {

	public LuxGrid(int width, int height) {
		super(width, height);
	}

	@Override
	protected void createPositions() {
		for (int w = 0; w < getWidth(); w++) {
			for (int h = 0; h < getHeight(); h++) {
				setGridPosition(new LuxGridPosition(w, h));
			}
		}
	}

	@Override
	public LuxGridPosition getGridPosition(int x, int y) {
		return (LuxGridPosition) super.getGridPosition(x, y);
	}

	public List<LuxGridPosition> getPositionsAsList() {
		List<LuxGridPosition> p = new ArrayList<LuxGridPosition>();
		for (GridPosition[] positions : super.getPositions()) {
			for (GridPosition pos : positions) {
				p.add((LuxGridPosition) pos);
			}
		}
		return p;
	}
}
