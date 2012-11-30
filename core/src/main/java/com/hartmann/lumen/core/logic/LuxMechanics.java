package com.hartmann.lumen.core.logic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.logging.Logger;

import com.hartmann.lumen.core.event.GridPositionDisseminationEvent;
import com.hartmann.lumen.core.event.InvalidPositionClickedEvent;
import com.hartmann.lumen.core.event.RestartGameRequestEvent;
import com.hartmann.lumen.core.event.WrongPositionClickedEvent;
import com.hartmann.lumen.core.model.LuxGrid;
import com.hartmann.lumen.core.model.LuxGridPosition;
import com.hartmann.lumen.core.model.LuxPlayer;
import com.hartmann.lumen.core.tools.event.EventBus;
import com.hartmann.lumen.core.tools.event.MouseClickedEvent;
import com.hartmann.lumen.core.tools.event.MvpEventHandler;

public class LuxMechanics implements Mechanics {

	private static final Logger logger = Logger.getLogger(LuxMechanics.class
			.getName());
	private final LuxGrid grid;
	private final EventBus eventBus;
	private final PriorityQueue<DisseminationItem> disseminationList;
	private final GameManager gameManager;

	public LuxMechanics(GameManager gameManager, EventBus eventBus, LuxGrid grid) {
		this.gameManager = gameManager;
		this.eventBus = eventBus;
		this.grid = grid;
		this.disseminationList = new PriorityQueue<DisseminationItem>(
				DisseminationItem.START_TURNS + 1,
				new DisseminationItemComparator());

		eventBus.addHandler(MouseClickedEvent.class,
				new MvpEventHandler<MouseClickedEvent>() {
					private static final long serialVersionUID = -8578193658689331151L;

					@Override
					public void onEvent(MouseClickedEvent event) {
						float x = event.x();
						float y = event.y();
						onMouseClicked(x, y, event.getPlayerId());
					}
				});

	}

	@Override
	public void init() {
		setInitialPositions();
	}

	private void setInitialPositions() {
		LuxGridPosition player1Start = grid.getGridPosition(
				grid.getWidth() / 2, 0);
		LuxGridPosition player2Start = grid.getGridPosition(
				grid.getWidth() / 2, grid.getHeight() - 1);
		player1Start.fortify(1);
		player2Start.fortify(2);

		eventBus.fireEvent(new GridPositionDisseminationEvent(
				grid.getWidth() / 2, 0, player1Start.getFortification(),
				player1Start.getPlayerId()));
		eventBus.fireEvent(new GridPositionDisseminationEvent(
				grid.getWidth() / 2, grid.getHeight() - 1, player2Start
						.getFortification(), 2));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hartmann.lumen.core.logic.Mechanics#update()
	 */
	@Override
	public void update() {
		/*
		 * Start dissemination for the positions which are ready for it.
		 */
		log("Updating mechanics on turn " + gameManager.getCurrentTurn());
		log("Dissemination list size: " + disseminationList.size());
		DisseminationItem item = disseminationList.peek();
		while (item != null && item.isReadyForDissemination()) {
			disseminationList.remove();
			LuxGridPosition itemPosition = item.getPosition();
			disseminateToNeighbors(itemPosition, getNeighbors(itemPosition));
			item = disseminationList.peek();
		}

		/*
		 * Count down the remaining items
		 */
		Iterator<DisseminationItem> iterator = disseminationList.iterator();
		while (iterator.hasNext()) {
			DisseminationItem nextItem = iterator.next();
			nextItem.countdown();
		}

		if (checkForGameOver()) {
			gameManager.endGame(findWinnerId());
		}
	}

	/**
	 * Finds the id of the first player in the board. It will return the id of
	 * the player who won, only if called just after {@link #checkForGameOver()}
	 * returns true.
	 * 
	 * @return Returns the id of the winner.
	 */
	private int findWinnerId() {
		List<LuxGridPosition> positions = grid.getPositionsAsList();

		for (LuxGridPosition pos : positions) {
			LuxPlayer player = LuxPlayer.getById(pos.getPlayerId());
			if (player.equals(LuxPlayer.NEUTRAL)) {
				continue;
			}

			// If the player is not neutral, we found our winner!
			return player.getId();
		}
		return 0;
	}

	@Override
	public void restartRequest() {
		eventBus.fireEvent(new RestartGameRequestEvent());
		setInitialPositions();
		disseminationList.clear();
	}

	/**
	 * The game is over if only one player is left in the board. Neutral is not
	 * taken in consideration.
	 */
	private boolean checkForGameOver() {
		List<LuxGridPosition> positions = grid.getPositionsAsList();

		Map<LuxPlayer, Boolean> playerIsAlive = new LinkedHashMap<LuxPlayer, Boolean>();
		for (LuxPlayer player : LuxPlayer.values()) {
			// Mark every player as NOT alive, ignore neutral player
			if (!player.equals(LuxPlayer.NEUTRAL)) {
				playerIsAlive.put(player, false);
			}
		}

		for (LuxGridPosition pos : positions) {
			LuxPlayer player = LuxPlayer.getById(pos.getPlayerId());
			if (player.equals(LuxPlayer.NEUTRAL)) {
				continue;
			}

			// If the player is not neutral, mark it as alive
			playerIsAlive.put(player, true);
		}

		// Count the amount of players that are alive
		int alivePlayers = 0;
		for (LuxPlayer player : playerIsAlive.keySet()) {
			if (playerIsAlive.get(player)) {
				alivePlayers++;
			}
		}

		if (alivePlayers > 1) {
			// More than one player is alive, so rock it on!
			return false;
		}
		return true;
	}

	private void disseminateToNeighbors(LuxGridPosition disseminatorPosition,
			List<LuxGridPosition> neighbors) {
		log("Starting dissemination from position: " + disseminatorPosition);
		for (LuxGridPosition neighbor : neighbors) {
			int oldPlayerId = neighbor.getPlayerId();
			neighbor.disseminate(disseminatorPosition.getPlayerId(),
					disseminatorPosition.getFortification());
			log("Disseminated to " + neighbor);
			eventBus.fireEvent(new GridPositionDisseminationEvent(neighbor
					.getX(), neighbor.getY(), neighbor.getFortification(),
					neighbor.getPlayerId()));
			int newPlayerId = neighbor.getPlayerId();
			if (newPlayerId != oldPlayerId) {
				// log("Changed player control to " + newPlayerId);
				// Start chain reaction
				// checkForDissemination(neighbor);
			}
		}
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param playerId
	 *            - ID of the player who made the click
	 */
	protected void onMouseClicked(float x, float y, int playerId) {
		LuxGridPosition positionClicked = getPosition(x, y);
		if (null == positionClicked) {
			eventBus.fireEvent(new WrongPositionClickedEvent(x, y));
			return;
		}

		if (isValidClickedPosition(positionClicked, playerId)) {
			log("Valid click position: " + positionClicked);
			positionClicked.fortify(playerId);
			eventBus.fireEvent(new GridPositionDisseminationEvent(
					positionClicked.getX(), positionClicked.getY(),
					positionClicked.getFortification(), positionClicked
							.getPlayerId()));
			checkForDissemination(positionClicked);
			gameManager.nextTurn();
		} else {
			eventBus.fireEvent(new InvalidPositionClickedEvent(x, y));
		}
	}

	private void log(String message) {
		logger.info(message);
	}

	private void checkForDissemination(LuxGridPosition positionClicked) {
		if (!disseminationList.contains(new DisseminationItem(positionClicked))) {
			// log("Adding position to dissemination list");
			disseminationList.offer(new DisseminationItem(positionClicked));
		}
	}

	/**
	 * Verify rules for the current player click.
	 * <p>
	 * The click is valid when:<br>
	 * <ol>
	 * <li>The position belongs to the player</li>
	 * <li>The position is neutral and is connected to the player</li>
	 * </ol>
	 * 
	 * @param positionClicked
	 *            - The grid position clicked by the player
	 * @return Returns true if the click is valid, false otherwise.
	 */
	private boolean isValidClickedPosition(LuxGridPosition positionClicked,
			int playerId) {
		if (positionClicked.getPlayerId() == playerId) {
			return true;
		}
		if (positionClicked.isNeutral()
				&& isPositionConnectedWithPlayer(positionClicked, playerId)) {
			return true;
		}
		return false;
	}

	/**
	 * A position is connected to the given player if at least one neighboring
	 * position is already owned by him.
	 * 
	 * @param position
	 *            - the position to test
	 * @param playerId
	 *            - the player id
	 * @return Return true if the position is connected with the player. False
	 *         if disconnected.
	 */
	private boolean isPositionConnectedWithPlayer(LuxGridPosition position,
			int playerId) {
		for (int xOffset = -1; xOffset <= 1; xOffset++) {
			for (int yOffset = -1; yOffset <= 1; yOffset++) {
				int x = position.getX();
				int y = position.getY();
				LuxGridPosition gridPosition = grid.getGridPosition(
						x + xOffset, y + yOffset);
				if (gridPosition != null
						&& playerId == gridPosition.getPlayerId()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Get all neighbors of the given position.
	 * 
	 * @param position
	 *            - the position to search for neighbors.
	 * @return Returns a list with all neighbors found for the given position.
	 */
	private List<LuxGridPosition> getNeighbors(LuxGridPosition position) {
		List<LuxGridPosition> neighbors = new ArrayList<LuxGridPosition>();
		for (int xOffset = -1; xOffset <= 1; xOffset++) {
			for (int yOffset = -1; yOffset <= 1; yOffset++) {
				if (xOffset == 0 && yOffset == 0) {
					continue;
				}
				int x = position.getX();
				int y = position.getY();
				LuxGridPosition gridPosition = grid.getGridPosition(
						x + xOffset, y + yOffset);
				if (null != gridPosition) {
					neighbors.add(gridPosition);
				}
			}
		}
		return neighbors;
	}

	/**
	 * Gets the grid position given the screen position.
	 * 
	 * @param x
	 *            - screen x
	 * @param y
	 *            - screen y
	 * @return Returns the grid position object.
	 */
	private LuxGridPosition getPosition(float x, float y) {
		int screenWidth = gameManager.getGridScreenWidth();
		int screenHeight = gameManager.getGridScreenHeight();
		int height = grid.getHeight();
		int width = grid.getWidth();
		int unitWidth = screenWidth / width;
		int unitHeight = screenHeight / height;

		// -1 because the index starts at 0
		float displacedX = x - gameManager.getOffsetX();
		float displacedY = y - gameManager.getOffsetY();
		int gridX = (int) Math.ceil(displacedX / unitWidth) - 1;
		int gridY = (int) Math.ceil(displacedY / unitHeight) - 1;
		// log("Clicked at grid position: " + gridX + " " + gridY);
		return grid.getGridPosition(gridX, gridY);
	}

	private class DisseminationItemComparator implements
			Comparator<DisseminationItem> {

		@Override
		public int compare(DisseminationItem one, DisseminationItem other) {
			// log("called compare " + one + " with " + other);
			return one.getTurnsUntilDissemination()
					- other.getTurnsUntilDissemination();
		}

	}

	class DisseminationItem {
		private static final int START_TURNS = 4;
		private LuxGridPosition position;
		private int turnsUntilDissemination;

		public DisseminationItem(LuxGridPosition position) {
			this.position = position;
			turnsUntilDissemination = START_TURNS;
		}

		public void countdown() {
			turnsUntilDissemination--;
		}

		public boolean isReadyForDissemination() {
			return turnsUntilDissemination <= 0;
		}

		// getters
		public LuxGridPosition getPosition() {
			return position;
		}

		public int getTurnsUntilDissemination() {
			return turnsUntilDissemination;
		}

		@Override
		public boolean equals(Object other) {
			// log("called equals " + this + " with " + other);
			if (other instanceof DisseminationItem) {
				if (((DisseminationItem) other).getPosition().equals(position)) {
					return true;
				}
			}
			return false;
		}

	}
}
