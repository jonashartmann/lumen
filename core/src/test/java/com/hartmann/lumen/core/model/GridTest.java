package com.hartmann.lumen.core.model;

import junit.framework.Assert;

import org.junit.Test;

public class GridTest {

	@Test
	public void testBasicGrid() {
		int width = 10;
		int height = 10;
		Grid instance = new BasicGrid(width, height);
		Assert.assertEquals(width, instance.getWidth());
		Assert.assertEquals(height, instance.getHeight());

		GridPosition gridPosition = instance.getGridPosition(0, 0);
		Assert.assertNotNull(gridPosition);
		Assert.assertEquals(0, gridPosition.getX());
		Assert.assertEquals(0, gridPosition.getY());

		GridPosition gridPosition2 = instance.getGridPosition(-1, 0);
		Assert.assertNull(gridPosition2);

		GridPosition gridPosition3 = instance.getGridPosition(0, -1);
		Assert.assertNull(gridPosition3);

		GridPosition gridPosition4 = instance.getGridPosition(width, 0);
		Assert.assertNull(gridPosition4);

		GridPosition gridPosition5 = instance.getGridPosition(0, height);
		Assert.assertNull(gridPosition5);
	}
	
	@Test
	public void testLuxGrid() {
		int width = 10;
		int height = 10;
		Grid instance = new LuxGrid(width, height);
		Assert.assertEquals(width, instance.getWidth());
		Assert.assertEquals(height, instance.getHeight());
		
		GridPosition gridPosition = instance.getGridPosition(0, 0);
		Assert.assertNotNull(gridPosition);
		Assert.assertEquals(0, gridPosition.getX());
		Assert.assertEquals(0, gridPosition.getY());
	}
}
