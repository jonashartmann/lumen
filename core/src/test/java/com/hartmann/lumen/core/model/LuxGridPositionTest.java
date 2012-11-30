package com.hartmann.lumen.core.model;

import junit.framework.Assert;

import org.junit.Test;

public class LuxGridPositionTest {

	@Test
	public void testAll() {
		LuxGridPosition position = new LuxGridPosition(15, 25);
		Assert.assertEquals(15, position.getX());
		Assert.assertEquals(25, position.getY());
		Assert.assertEquals(1, position.getFortification());
		Assert.assertEquals(true, position.isNeutral());
		Assert.assertEquals(false, position.isFortified());

		int playerId = 1;
		position.disseminate(playerId, 2);
		Assert.assertEquals(false, position.isNeutral());
		Assert.assertEquals(playerId, position.getPlayerId());
		Assert.assertEquals(false, position.isFortified());
		int fort = 1;
		Assert.assertEquals(fort, position.getFortification());

		position.fortify(playerId);
		fort += 1;
		position.fortify(playerId);
		fort += 1;
		position.fortify(playerId);
		fort += 1;
		Assert.assertEquals(fort, position.getFortification());
		Assert.assertEquals(true, position.isFortified());
	}

}
