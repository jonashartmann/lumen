package com.hartmann.lumen.core.logic;

import java.util.logging.Logger;

import playn.core.PlayN;
import tripleplay.game.Screen;
import tripleplay.game.ScreenStack;
import tripleplay.game.ScreenStack.Predicate;

import com.hartmann.lumen.core.event.GameStateChangedEvent;
import com.hartmann.lumen.core.model.Grid;
import com.hartmann.lumen.core.tools.event.EventBus;
import com.hartmann.lumen.core.tools.event.SimpleEventBus;

/**
 * Manages the game loop and all the logic of the game.
 * 
 * @author Jonas
 * 
 */
public abstract class GameManager {

	protected static final Logger logger = Logger.getLogger(GameManager.class
			.getName());
	private static final float OFFSET_X = 50f;
	private static final float OFFSET_Y = 50f;

	private final EventBus eventBus;
	private GameState gameState;
	private final Grid grid;
	private int currentPlayerId;
	private final Mechanics gameMechanics;
	private final int screenWidth;
	private final int screenHeight;
	private int lastUpdateTurn;
	private int turn;
	private int winnerId;

	public GameManager(int screenWidth, int screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		eventBus = new SimpleEventBus();
		turn = 1;
		lastUpdateTurn = 0;

		gameState = getStartState();
		grid = createGrid();
		currentPlayerId = getStartPlayerId();
		gameMechanics = createGameMechanics();
	}

	public void init() {
		gameMechanics.init();
	}

	protected abstract void restartGame();

	protected abstract int getStartPlayerId();

	protected abstract GameState getStartState();

	protected abstract Grid createGrid();

	protected abstract Mechanics createGameMechanics();

	public float getOffsetX() {
		return OFFSET_X;
	}

	public float getOffsetY() {
		return OFFSET_Y;
	}

	public void resetTurns() {
		turn = 1;
	}

	public void pauseGame() {
		if (!GameState.PAUSED.equals(gameState)) {
			setGameState(GameState.PAUSED);
		}
	}

	public void continueGame() {
		if (!GameState.PLAYING.equals(gameState)) {
			setGameState(GameState.PLAYING);
		}
	}

	public void paint(float alpha) {
		screens.paint(alpha);
	}

	public void update(float delta) {
		screens.update(delta);
	}

	public boolean isGamePaused() {
		return GameState.PLAYING != gameState;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public int getCurrentPlayerId() {
		return currentPlayerId;
	}

	public void setCurrentPlayerId(int currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}

	public int getWinnerId() {
		return winnerId;
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public GameState getGameState() {
		return gameState;
	}

	public Grid getGrid() {
		return grid;
	}

	/**
	 * Goes to the next turn.
	 */
	public void nextTurn() {
		turn++;
		currentPlayerId = (turn % 2) == 0 ? 2 : 1;
		logger.info("Turn = " + turn + "  PlayerId = " + currentPlayerId);
	}

	public int getCurrentTurn() {
		return turn;
	}

	public void endGame(int winnerId) {
		logger.info("GAME OVER");
		this.winnerId = winnerId;
		setGameState(GameState.GAME_OVER);
	}

	public void setGameState(GameState newGameState) {
		GameState oldState = gameState;
		gameState = newGameState;
		eventBus.fireEvent(new GameStateChangedEvent(oldState, gameState));
	}

	public void addScreen(Screen screen) {
		screens.push(screen);
	}

	public void removeScreen(Screen screen) {
		screens.remove(screen);
	}

	public void removeAllScreens() {
		screens.remove(new Predicate() {

			@Override
			public boolean apply(Screen screen) {
				return true;
			}
		});
	}

	protected Mechanics getGameMechanics() {
		return gameMechanics;
	}

	public int getLastUpdateTurn() {
		return lastUpdateTurn;
	}

	public void setLastUpdateTurn(int lastUpdateTurn) {
		this.lastUpdateTurn = lastUpdateTurn;
	}

	protected final ScreenStack screens = new ScreenStack() {
		@Override
		protected void handleError(RuntimeException error) {
			PlayN.log().warn("Screen failure", error);
		}

		@Override
		protected Transition defaultPushTransition() {
			return slide();
		}

		@Override
		protected Transition defaultPopTransition() {
			return slide().right();
		}
	};

	public int getGridScreenWidth() {
		return (int) (getScreenWidth() - 2 * OFFSET_X);
	}

	public int getGridScreenHeight() {
		return (int) (getScreenHeight() - 2 * OFFSET_Y);
	}
}
