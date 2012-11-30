package com.hartmann.lumen.core.model;

/**
 * Extension of a {@link GridPosition}, so that we can save information specific
 * to the Lumen game.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public class LuxGridPosition extends GridPosition {

	private static final int MAX_FORTIFICATION = 99;

	private int fortification;
	private LuxPlayer player;

	public LuxGridPosition(int x, int y) {
		super(x, y);
		fortification = 1;
		player = LuxPlayer.NEUTRAL;
	}

	public boolean isNeutral() {
		return player.equals(LuxPlayer.NEUTRAL);
	}

	public int getPlayerId() {
		return player.getId();
	}

	public int getPlayerColor() {
		return player.getColor();
	}

	public HSBColor getPlayerHSBColor() {
		return player.getHSBColor();
	}

	public void changeControl(int playerId) {
		this.player = LuxPlayer.getById(playerId);
		fortification = 1;
	}

	/**
	 * Disseminate to this position. If the player owns the position it will
	 * build up fortification, otherwise it will remove fortification and in
	 * some cases give control of the position to the player who started the
	 * dissemination.
	 * 
	 * @param playerId
	 *            - the id of the player who caused the dissemination
	 * @param fortificationUnits
	 *            - the amount of fortification units disseminated
	 */
	public void disseminate(int playerId, int fortificationUnits) {
		if (playerId == this.player.getId()) {
			fortification += fortificationUnits;
			if (fortification > MAX_FORTIFICATION) {
				fortification = MAX_FORTIFICATION;
			}
		} else {
			fortification -= fortificationUnits;
			if (fortification < 1) {
				changeControl(playerId);
			}
		}
	}

	public void fortify(int playerId) {
		disseminate(playerId, 1);
	}

	public boolean isFortified() {
		return fortification != 1;
	}

	public int getFortification() {
		return fortification;
	}

	@Override
	public String toString() {
		return "LuxGridPosition [" + getX() + ", " + getY()
				+ " fortification = " + getFortification() + " playerId = "
				+ getPlayerId() + "]";
	}
}
