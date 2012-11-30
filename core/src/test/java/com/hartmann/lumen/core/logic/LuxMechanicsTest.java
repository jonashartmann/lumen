package com.hartmann.lumen.core.logic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.PriorityQueue;

import junit.framework.Assert;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.hartmann.lumen.core.logic.LuxMechanics.DisseminationItem;
import com.hartmann.lumen.core.model.LuxGrid;
import com.hartmann.lumen.core.model.LuxGridPosition;
import com.hartmann.lumen.core.tools.event.EventBus;
import com.hartmann.lumen.core.tools.event.MvpEvent;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LuxMechanics.class)
@PowerMockIgnore("java.util.logging.Logger")
@Ignore
public class LuxMechanicsTest {

	private LuxMechanics instance;
	private GameManager gameManagerMock;
	private EventBus eventBusMock;
	private LuxGrid gridMock;

	@Before
	public void before() {
		gameManagerMock = EasyMock.createMock(GameManager.class);
		eventBusMock = EasyMock.createNiceMock(EventBus.class);
		gridMock = EasyMock.createMock(LuxGrid.class);
		instance = new LuxMechanics(gameManagerMock, eventBusMock, gridMock);
	}

	@Test
	public void testOnMouseClicked() throws Exception {
		String[] methodsToMock = new String[] { "getPosition",
				"isValidClickedPosition", "checkForDissemination" };
		LuxMechanics mechanicsMock = PowerMock.createPartialMock(
				LuxMechanics.class, methodsToMock);

		LuxGridPosition gridPositionMock = EasyMock
				.createMock(LuxGridPosition.class);
		float x = 200;
		float y = 300;
		PowerMock.expectPrivate(mechanicsMock, methodsToMock[0], x, y)
				.andReturn(gridPositionMock);

		int playerId = 2;
		PowerMock.expectPrivate(mechanicsMock, methodsToMock[1],
				gridPositionMock, playerId).andReturn(true);

		gridPositionMock.fortify(playerId);
		EasyMock.expectLastCall();
		EasyMock.expect(gridPositionMock.getX()).andReturn(0);
		EasyMock.expect(gridPositionMock.getY()).andReturn(0);
		EasyMock.expect(gridPositionMock.getFortification()).andReturn(1);

		PowerMock.expectPrivate(mechanicsMock, methodsToMock[2],
				gridPositionMock);

		PowerMock.suppress(Whitebox.getMethod(EventBus.class, "fireEvent",
				MvpEvent.class));

		PowerMock.replayAll(gridPositionMock);
		mechanicsMock.onMouseClicked(x, y, playerId);
		PowerMock.verifyAll();
	}

	@Test
	public void testIsValidClickedPosition() throws Exception {
		String methodToMock = "isPositionConnectedWithPlayer";
		LuxGridPosition positionMock = EasyMock
				.createMock(LuxGridPosition.class);
		int playerId = 1;
		LuxMechanics mechanicsMock = PowerMock.createPartialMock(
				LuxMechanics.class, methodToMock);

		EasyMock.expect(positionMock.getPlayerId()).andReturn(playerId);

		PowerMock.replayAll(positionMock);

		Method privateMethodToTest = Whitebox.getMethod(LuxMechanics.class,
				"isValidClickedPosition", LuxGridPosition.class, int.class);
		Object result = privateMethodToTest.invoke(mechanicsMock, positionMock,
				playerId);
		Assert.assertEquals(true, result);

		PowerMock.verifyAll();
		PowerMock.resetAll();

		// Test another branch
		EasyMock.expect(positionMock.getPlayerId()).andReturn(0);
		EasyMock.expect(positionMock.isNeutral()).andReturn(true);
		PowerMock.expectPrivate(mechanicsMock, methodToMock, positionMock,
				playerId).andReturn(true);

		PowerMock.replayAll();

		result = privateMethodToTest.invoke(mechanicsMock, positionMock,
				playerId);
		Assert.assertEquals(true, result);

		PowerMock.verifyAll();
		PowerMock.resetAll();

		// Test different players
		EasyMock.expect(positionMock.getPlayerId()).andReturn(2);
		EasyMock.expect(positionMock.isNeutral()).andReturn(false);

		PowerMock.replayAll();

		result = privateMethodToTest.invoke(mechanicsMock, positionMock,
				playerId);
		Assert.assertEquals(false, result);

		PowerMock.verifyAll();
		PowerMock.resetAll();
	}

	@Test
	public void testIsPositionConnected() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		// expectations
		int playerId = 1;
		LuxGridPosition positionInputMock = EasyMock
				.createMock(LuxGridPosition.class);
		LuxGridPosition positionTestMock = EasyMock
				.createMock(LuxGridPosition.class);
		EasyMock.expect(
				gridMock.getGridPosition(EasyMock.anyInt(), EasyMock.anyInt()))
				.andReturn(positionTestMock).anyTimes();
		EasyMock.expect(positionTestMock.getPlayerId()).andReturn(playerId)
				.anyTimes();
		EasyMock.expect(positionInputMock.getX()).andReturn(5).anyTimes();
		EasyMock.expect(positionInputMock.getY()).andReturn(5).anyTimes();

		// call test method
		PowerMock.replayAll(gridMock, positionTestMock, positionInputMock);

		String methodName = "isPositionConnectedWithPlayer";
		Method privateMethodToTest = Whitebox.getMethod(LuxMechanics.class,
				methodName, LuxGridPosition.class, int.class);
		Object result = privateMethodToTest.invoke(instance, positionInputMock,
				playerId);
		Assert.assertEquals(true, result);

		PowerMock.verifyAll();
		PowerMock.resetAll(positionTestMock, positionInputMock);

		/*
		 * Test false result
		 */
		// expectations
		EasyMock.expect(
				gridMock.getGridPosition(EasyMock.anyInt(), EasyMock.anyInt()))
				.andReturn(positionTestMock).anyTimes();
		EasyMock.expect(positionTestMock.getPlayerId()).andReturn(playerId + 1)
				.anyTimes();
		EasyMock.expect(positionInputMock.getX()).andReturn(5).anyTimes();
		EasyMock.expect(positionInputMock.getY()).andReturn(5).anyTimes();

		// call test method
		PowerMock.replayAll(positionTestMock);

		result = privateMethodToTest.invoke(instance, positionInputMock,
				playerId);
		Assert.assertEquals(false, result);

		PowerMock.verifyAll();
		PowerMock.resetAll();
	}

	@Test
	public void testGetPosition() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		// expectations
		float screenY = 10f;
		float screenX = 20f;
		int screenWidth = 400;
		int screenHeight = 400;
		int gridWidth = 10;
		int gridHeight = 10;

		EasyMock.expect(gameManagerMock.getScreenHeight()).andReturn(
				screenHeight);
		EasyMock.expect(gameManagerMock.getScreenWidth())
				.andReturn(screenWidth);
		EasyMock.expect(gridMock.getHeight()).andReturn(gridHeight);
		EasyMock.expect(gridMock.getWidth()).andReturn(gridWidth);

		int calculatedX = 0;
		int calculatedY = 0;
		LuxGridPosition posRes = new LuxGridPosition(calculatedX, calculatedY);
		EasyMock.expect(gridMock.getGridPosition(calculatedX, calculatedY))
				.andReturn(posRes);

		// call test method
		PowerMock.replayAll(gameManagerMock, gridMock);

		String methodName = "getPosition";
		Method privateMethodToTest = Whitebox.getMethod(LuxMechanics.class,
				methodName, float.class, float.class);
		LuxGridPosition result = (LuxGridPosition) privateMethodToTest.invoke(
				instance, screenX, screenY);
		Assert.assertNotNull(result);
		Assert.assertEquals(posRes, result);

		PowerMock.verifyAll();
		PowerMock.resetAll();
	}

	@Test
	public void testUpdate() {
		// expectations
		String[] methodsToIgnore = new String[] { "getNeighbors",
				"disseminateToNeighbors" };
		PowerMock.suppress(Whitebox.getMethods(LuxMechanics.class,
				methodsToIgnore));

		PriorityQueue queueMock = EasyMock.createMock(PriorityQueue.class);
		Whitebox.setInternalState(instance, PriorityQueue.class, queueMock);

		DisseminationItem itemMock = EasyMock
				.createMock(DisseminationItem.class);
		EasyMock.expect(queueMock.peek()).andReturn(itemMock).times(3);
		EasyMock.expect(itemMock.isReadyForDissemination()).andReturn(true)
				.times(2);
		EasyMock.expect(queueMock.remove()).andReturn(itemMock).times(2);
		EasyMock.expect(itemMock.getPosition()).andReturn(null).times(2);

		EasyMock.expect(itemMock.isReadyForDissemination()).andReturn(false)
				.once();

		Iterator iterator = EasyMock.createMock(Iterator.class);
		EasyMock.expect(queueMock.iterator()).andReturn(iterator);
		EasyMock.expect(iterator.hasNext()).andReturn(false).anyTimes();

		// call test method
		PowerMock.replayAll(queueMock, itemMock, iterator);
		instance.update();
		PowerMock.verifyAll();
	}
}
