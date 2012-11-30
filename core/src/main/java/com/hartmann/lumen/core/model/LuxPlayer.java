package com.hartmann.lumen.core.model;

/**
 * Represents a player in the Lumen game.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public enum LuxPlayer {
	NEUTRAL(0, HSBColor.GREEN), PLAYER_ONE(1, HSBColor.RED), PLAYER_TWO(2,
			HSBColor.BLUE);

	private final int playerId;
	private final HSBColor color;

	LuxPlayer(int playerId, HSBColor color) {
		this.playerId = playerId;
		this.color = color;
	}

	public int getId() {
		return playerId;
	}

	public int getColor() {
		return color.toRGB();
	}

	public HSBColor getHSBColor() {
		return color;
	}

	/**
	 * Gets an instance of {@link LuxPlayer}. If the id is invalid, it returns
	 * null.
	 * 
	 * @param id
	 *            - Id of the player
	 * @return Returns the player with the given id
	 */
	public static LuxPlayer getById(int id) {
		switch (id) {
		case 0:
			return LuxPlayer.NEUTRAL;
		case 1:
			return LuxPlayer.PLAYER_ONE;
		case 2:
			return LuxPlayer.PLAYER_TWO;
		}

		return null;
	}
}
