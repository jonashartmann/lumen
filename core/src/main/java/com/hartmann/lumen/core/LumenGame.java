package com.hartmann.lumen.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;

import com.hartmann.lumen.core.logic.GameManager;
import com.hartmann.lumen.core.logic.LuxGameManager;

/**
 * This is the starting point of the Lumen game. It is an implementation of the
 * PlayN {@link Game} interface.
 * <p>
 * It defines the update rate, adds a background and initializes the game. The
 * control of the game loop is then delegated to a {@link GameManager}.
 * Specifically, a {@link LuxGameManager}.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public class LumenGame implements Game {

	private static final int SCREEN_WIDTH = 600;
	private static final int SCREEN_HEIGHT = 600;

	private GameManager gameManager;

	@Override
	public void init() {
		graphics().setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

		// create and add background image layer
		Image bgImage = assets().getImage("images/bg.png");
		ImageLayer bgLayer = graphics().createImageLayer(bgImage);
		graphics().rootLayer().add(bgLayer);

		gameManager = new LuxGameManager(SCREEN_WIDTH, SCREEN_HEIGHT);
		gameManager.init();

	}

	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything
		// here!
		gameManager.paint(alpha);
	}

	@Override
	public void update(float delta) {
		gameManager.update(delta);
	}

	@Override
	public int updateRate() {
		return 25;
	}

}
