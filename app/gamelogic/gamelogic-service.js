(function() {
	'use strict';
	angular.module('lumen.logic')
	.service('LumenLogic', ['Grid',
	function LumenLogic (Grid) {
		var COUNTDOWN = 3,
			MAX_FORTIFICATION = 99,
			disseminationList = {};
		return {
			nodeClicked: function nodeClicked (node, currentPlayer) {
				if (!this.isValidClick(node, currentPlayer)) {
					return false;
				}
				// Click on an owned position
				if (node.player == currentPlayer) {
					// Fortify and add to dissemination list
					node.fortification++;
					if (node.fortification > MAX_FORTIFICATION) {
						node.fortification = MAX_FORTIFICATION;
					}
					this.prepareForDissemination(node);
				}
				node.player = currentPlayer;
				return true;
			},
			isValidClick: function isValidClick (node, player) {
				// Do not click on other player's node!
				if ((node.player !== 0) && (node.player !== player)) {
					return false;
				}

				if (node.player === 0) {
					return this.isNeighbourNode(player, node);
				}

				return true;
			},
			/**
			 * Returns true if the node is neighbour of at least one
			 * node that belongs to the player given.
			 * Neighbourhood is checked only directly horizontally
			 * or vertically.
			 */
			isNeighbourNode: function isNeighbourNode (player, node) {
				var otherNode = null;
				var isNeighbour = false;
				otherNode = Grid.getNode(node.col-1, node.row);
				if (otherNode && otherNode.player == player) {
					isNeighbour = true;
				}
				otherNode = Grid.getNode(node.col+1, node.row);
				if (otherNode && otherNode.player == player) {
					isNeighbour = true;
				}
				otherNode = Grid.getNode(node.col, node.row-1);
				if (otherNode && otherNode.player == player) {
					isNeighbour = true;
				}
				otherNode = Grid.getNode(node.col, node.row+1);
				if (otherNode && otherNode.player == player) {
					isNeighbour = true;
				}
				return isNeighbour;
			},
			prepareForDissemination: function prepareForDissemination (node) {
				// lazy initialization of the lists to allow for dynamic amounts
				if (!disseminationList[COUNTDOWN]) {
					disseminationList[COUNTDOWN] = [];
				}
				// Reset dissemination countdown, remove it from the list
				this.stopCountdown(node);

				// Add node at the top of the countdown list
				node.countdown = COUNTDOWN;
				disseminationList[COUNTDOWN].push(node);
			},
			/**
			 * Removes node from dissemination list, stopping the countdown
			 */
			stopCountdown: function stopCountdown (node) {
				if (node.countdown >= 0) {
					var list = disseminationList[node.countdown];
					var ix = list.indexOf(node);
					list.splice(ix, 1);
					node.countdown = -1;
				}
			},
			endTurn: function endTurn (turn, currentPlayer) {
				this.updateDissemination(currentPlayer);
			},
			updateDissemination: function updateDissemination (currentPlayer) {
				for (var i = 0; i <= COUNTDOWN; i++) {
					var curList = disseminationList[i];
					if (!curList) {
						continue;
					}
					var lowerList = i === 0 ? [] : disseminationList[i-1];
					if (!lowerList) {
						lowerList = disseminationList[i-1] = [];
					}

					// Either disseminate or push all nodes to the lower list
					// Decreasing the countdown
					var len = curList.length;
					while (len--) {
						var node = curList[len];
						node.updateProps(); // To update the countdown text
						// Only update nodes of the current player
						if (node.player != currentPlayer) {
							continue;
						}

						node.countdown--;
						console.log('Node: ', node);
						if (i === 0) {
							this.disseminate(node);
						} else {
							lowerList.push(node);
						}
						curList.splice(len, 1);
					}
				}
			},
			disseminate: function disseminate (node) {
				console.log('DISSEMINATION!', node);
				for (var i = -1; i <= 1; i++) {
					for (var j = -1; j <= 1; j++) {
						// Exclude diagonals
						if (i == j || -1*i == j || i == j*-1) continue;
						var otherNode = Grid.getNode(node.col + i, node.row + j);
						if (otherNode) {
							console.log('->', otherNode);
							if (otherNode.player == node.player) {
								// otherNode.fortification++;
								otherNode.fortification += node.fortification;
								if (otherNode.fortification > MAX_FORTIFICATION) {
									otherNode.fortification = MAX_FORTIFICATION;
								}
							} else {
								// otherNode.fortification--;
								otherNode.fortification -= node.fortification-1;
							}
							// Conquered new positions?
							if (otherNode.fortification <= 0) {
								otherNode.player = node.player;
								otherNode.fortification = 1;
								this.stopCountdown(otherNode);
							}
							otherNode.updateProps();
						}
					}
				}
				node.updateProps();
			},
			isGameOver: function isGameOver () {
				// TODO: replace by a more efficient logic
				var playersFound = {1: false, 2: false};
				for (var i = 0; i < Grid.cols; i++) {
					for (var j = 0; j < Grid.rows; j++) {
						var node = Grid.getNode(i, j);
						playersFound[1] = playersFound[1] || node.player === 1;
						playersFound[2] = playersFound[2] || node.player === 2;
						if (playersFound[1] && playersFound[2]) {
							return false;
						}
					}
				}
				if (playersFound[1] && playersFound[2]) {
					return false;
				}
				return true;
			}
		};
	}]);
})();