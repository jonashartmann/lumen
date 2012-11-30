package com.hartmann.lumen.core.ui.menu;

import tripleplay.ui.Button;
import tripleplay.ui.Label;

public class GameOverView extends MainMenuView {

	public GameOverView() {
		super();
		singlePlayerButton = new Button("Retry");
		label = new Label("Game Over!");
	}

	public void setWinnerPlayer(int playerId) {
		label = new Label("Game Over: Player " + playerId + " won!");
	}
	
	public void wasShown() {
		super.wasShown();
	}
}
