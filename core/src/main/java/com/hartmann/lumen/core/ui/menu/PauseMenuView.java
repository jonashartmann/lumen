package com.hartmann.lumen.core.ui.menu;

import tripleplay.ui.Button;

/**
 * View used when the game is Paused.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public class PauseMenuView extends MainMenuView {

	public PauseMenuView() {
		super();
		singlePlayerButton = new Button("Continue Game");
	}
}
