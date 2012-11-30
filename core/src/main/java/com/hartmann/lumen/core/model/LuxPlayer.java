package com.hartmann.lumen.core.model;

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

	public static LuxPlayer getById(int id) {
		switch (id) {
		case 0:
			return LuxPlayer.NEUTRAL;
		case 1:
			return LuxPlayer.PLAYER_ONE;
		case 2:
			return LuxPlayer.PLAYER_TWO;
		}

		throw new IllegalArgumentException("Id not recognized! id=" + id);
	}
}
