package com.hartmann.lumen.core.logic;

/**
 * An interface for classes that handle specific game logic.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
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