package com.hartmann.lumen.core.logic;

import static playn.core.PlayN.keyboard;
import static playn.core.PlayN.pointer;
import playn.core.Keyboard;
import playn.core.Keyboard.TypedEvent;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;

import com.hartmann.lumen.core.model.GridPosition;
import com.hartmann.lumen.core.model.LuxGrid;
import com.hartmann.lumen.core.model.LuxGridPosition;
import com.hartmann.lumen.core.model.LuxPlayer;
import com.hartmann.lumen.core.tools.event.MouseClickedEvent;
import com.hartmann.lumen.core.ui.UIController;

/**
 * Implementation of a {@link GameManager} for the Lumen game.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public class LuxGameManager extends GameManager implements Listener {

	private static final int GRID_HEIGHT = 7;
	private static final int GRID_WIDTH = 7;

	private UIController uiController;

	public LuxGameManager(int screenWidth, int screenHeight) {
		super(screenWidth, screenHeight);

		uiController = new UIController(this);

		pointer().setListener(this);

		keyboard().setListener(new Keyboard.Listener() {

			@Override
			public void onKeyUp(playn.core.Keyboard.Event event) {
			}

			@Override
			public void onKeyTyped(TypedEvent event) {

			}

			@Override
			public void onKeyDown(playn.core.Keyboard.Event event) {
				LuxGameManager.this.onKeyDown(event);
			}
		});
	}

	@Override
	public void init() {
		super.init();
		uiController.init();
	}

	@Override
	public void update(float delta) {
		if (isPlaying()) {
			/*
			 * The game is alive!
			 */
			if (getCurrentTurn() != getLastUpdateTurn()) {
				getGameMechanics().update();
				setLastUpdateTurn(getCurrentTurn());
			}
		}

		uiController.update(delta);
	}

	@Override
	public void paint(float alpha) {
		super.paint(alpha);
		uiController.paint(alpha);
	}

	public boolean isPlaying() {
		return getGameState() == GameState.PLAYING;
	}

	@Override
	protected int getStartPlayerId() {
		return LuxPlayer.PLAYER_ONE.getId();
	}

	@Override
	protected GameState getStartState() {
		return GameState.MENU_MAIN;
	}

	@Override
	protected LuxGrid createGrid() {
		return new LuxGrid(GRID_WIDTH, GRID_HEIGHT);
	}

	@Override
	protected Mechanics createGameMechanics() {
		return new LuxMechanics(this, getEventBus(), getGrid());
	}

	@Override
	public LuxGrid getGrid() {
		return (LuxGrid) super.getGrid();
	}

	@Override
	public void restartGame() {
		GridPosition[][] positions = getGrid().getPositions();
		for (GridPosition[] positionW : positions) {
			for (GridPosition position : positionW) {
				((LuxGridPosition) position).changeControl(LuxPlayer.NEUTRAL
						.getId());
			}
		}

		setCurrentPlayerId(getStartPlayerId());
		resetTurns();

		getGameMechanics().restartRequest();
		super.continueGame();
	}

	@Override
	public void onPointerStart(Event event) {
	}

	@Override
	public void onPointerEnd(Event event) {
		float x = event.x();
		float y = event.y();
		// float localX = event.localX();
		// float localY = event.localY();

		// logger.info("Mouse clicked on (screen): " + x + " " + y);
		// logger.info("Mouse clicked on (layer): " + localX + " " + localY);

		if (!isGamePaused()) {
			getEventBus().fireEvent(
					new MouseClickedEvent(x, y, getCurrentPlayerId()));
		}

		// gridPresenter.init();
	}

	public void startGame() {
		super.continueGame();
	}

	@Override
	public void onPointerDrag(Event event) {
	}

	private void onKeyDown(playn.core.Keyboard.Event event) {
		if (isPlaying()) {
			switch (event.key()) {
			case N:
				nextTurn();
				break;
			case R:
				restartGame();
				break;
			case ESCAPE:
				pauseGame();
				break;
			default:
				break;
			}
		}
	}
}
