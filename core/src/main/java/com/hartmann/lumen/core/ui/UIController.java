package com.hartmann.lumen.core.ui;

import com.hartmann.lumen.core.event.GameStateChangedEvent;
import com.hartmann.lumen.core.event.GridPositionDisseminationEvent;
import com.hartmann.lumen.core.event.RestartGameRequestEvent;
import com.hartmann.lumen.core.logic.GameState;
import com.hartmann.lumen.core.logic.LuxGameManager;
import com.hartmann.lumen.core.model.LuxPlayer;
import com.hartmann.lumen.core.tools.ClickListener;
import com.hartmann.lumen.core.tools.event.MvpEventHandler;
import com.hartmann.lumen.core.ui.menu.CreditsView;
import com.hartmann.lumen.core.ui.menu.GameOverView;
import com.hartmann.lumen.core.ui.menu.MainMenuView;
import com.hartmann.lumen.core.ui.menu.PauseMenuView;

public class UIController implements
		MvpEventHandler<GridPositionDisseminationEvent> {

	private static final long serialVersionUID = 4200534624901592182L;

	private LuxGameManager gameManager;

	private GridView gridView;
	private MainMenuView mainMenuView;
	private GameInformationView gameInfoView;
	private PauseMenuView pauseMenuView;
	private GameOverView gameOverView;

	private boolean modelModified;

	private CreditsView creditsView;

	public UIController(final LuxGameManager gameManager) {
		this.gameManager = gameManager;
		this.modelModified = true;

		gridView = new GridView(gameManager.getGrid(),
				gameManager.getGridScreenWidth(),
				gameManager.getGridScreenHeight(), gameManager.getOffsetX(),
				gameManager.getOffsetY());
		mainMenuView = new MainMenuView();
		gameInfoView = new GameInformationView();
		pauseMenuView = new PauseMenuView();
		gameOverView = new GameOverView();
		creditsView = new CreditsView();

		addListenersAndHandlers();
	}

	private void addListenersAndHandlers() {
		mainMenuView.addSinglePlayerButtonClickListener(new ClickListener() {

			@Override
			public void onClick() {
				gameManager.startGame();
			}
		});

		pauseMenuView.addSinglePlayerButtonClickListener(new ClickListener() {

			@Override
			public void onClick() {
				gameManager.continueGame();
			}
		});

		gameOverView.addSinglePlayerButtonClickListener(new ClickListener() {

			@Override
			public void onClick() {
				gameManager.restartGame();
			}
		});

		ClickListener creditsClickListener = new ClickListener() {

			@Override
			public void onClick() {
				gameManager.setGameState(GameState.MENU_CREDITS);
			}
		};
		mainMenuView.addCreditsButtonClickListener(creditsClickListener);
		pauseMenuView.addCreditsButtonClickListener(creditsClickListener);
		gameOverView.addCreditsButtonClickListener(creditsClickListener);

		creditsView.addBackButtonClickListener(new ClickListener() {

			@Override
			public void onClick() {
				gameManager.setGameState(GameState.MENU_MAIN);
			}
		});

		gameManager.getEventBus().addHandler(
				GridPositionDisseminationEvent.class, this);
		gameManager.getEventBus().addHandler(RestartGameRequestEvent.class,
				new MvpEventHandler<RestartGameRequestEvent>() {
					private static final long serialVersionUID = 1L;

					@Override
					public void onEvent(RestartGameRequestEvent event) {
						gridView.reset();
					}
				});
		gameManager.getEventBus().addHandler(GameStateChangedEvent.class,
				new MvpEventHandler<GameStateChangedEvent>() {

					private static final long serialVersionUID = -1548391708659659517L;

					@Override
					public void onEvent(GameStateChangedEvent event) {
						onGameStateChangedEvent(event);
					}
				});
	}

	protected void onGameStateChangedEvent(GameStateChangedEvent event) {
		gameManager.removeAllScreens();
		switch (event.getNewState()) {
		case MENU_CREDITS:
			gameManager.addScreen(creditsView);
			break;
		case MENU_MAIN:
			gameManager.addScreen(mainMenuView);
			break;
		case MENU_OPTIONS:
			break;
		case PAUSED:
			gameManager.addScreen(pauseMenuView);
			break;
		case PLAYING:
			gameManager.removeAllScreens();
			break;
		case GAME_OVER:
			gameOverView.setWinnerPlayer(gameManager.getWinnerId());
			gameManager.addScreen(gameOverView);
			break;
		}
	}

	public void init() {
		gridView.init();
		gameManager.addScreen(mainMenuView);
		gameInfoView.init(gameManager.getScreenWidth(),
				gameManager.getScreenHeight());
	}

	public void paint(float alpha) {
		if (modelModified) {
			gridView.paint(alpha);
			modelModified = false;
		}

		if (gameManager.isPlaying()) {
			gameInfoView.paint(alpha,
					LuxPlayer.getById(gameManager.getCurrentPlayerId()));
		}
	}

	@Override
	public void onEvent(GridPositionDisseminationEvent event) {
		modelModified = true;
	}

	public void update(float delta) {
	}
}
