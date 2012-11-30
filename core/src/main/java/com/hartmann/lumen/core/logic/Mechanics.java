package com.hartmann.lumen.core.logic;

public interface Mechanics {

	/**
	 * This function will be called once per turn by the game manager.
	 */
	public void update();

	/**
	 * Use this method to initialize anything.
	 */
	public void init();

	/**
	 * Called when a game restart request.
	 */
	public void restartRequest();

}