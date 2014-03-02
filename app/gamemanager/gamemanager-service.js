(function() {
	'use strict';
	angular.module('lumen.game')
	.service('GameManager', ['$rootScope', 'Grid', 'Messaging',
	function GameManager ($rootScope, Grid, Messaging) {
		var COUNTDOWN = 3;
		return {
			currentPlayer: 1,
			turn: 0,
			paused: false,
			stage: null,
			gridLayer: null,
			fortificationLayer: null,
			disseminationList: {},
			init: function init (startingPlayer) {
				this.currentPlayer = startingPlayer;
				this.paused = false;
				this.gridLayer = new Kinetic.Layer();
				this.fortificationLayer = new Kinetic.Layer();
				this.turn = 1;
			},
			createGrid: function createGrid (stage) {
				this.stage = stage;

				var rectWidth = Math.floor(stage.getWidth() / Grid.cols - Grid.cols/2);
				var rectHeight = Math.floor(stage.getHeight() / Grid.rows - Grid.rows/2);

				var self = this;
				function clickHandler (e) {
					$rootScope.$apply(self.onNodeClicked(e));
				}

				for (var i = 0; i < Grid.cols; i++) {
					for (var j = 0; j < Grid.rows; j++) {
						var options = {
							x: (i * rectWidth) + (i*Math.floor(Grid.cols/2)) + Math.floor(Grid.cols/2),
							y: (j * rectHeight) + (j*Math.floor(Grid.cols/2)) + Math.floor(Grid.cols/2),
							width: rectWidth,
							height: rectHeight,
							fill: '#00D2FF',
							// stroke: 'black',
							draggable: false
						};
						var rect = new Kinetic.Rect(options);

						// add extra custom data
						var node = {
							col: i,
							row: j,
							player: 0,
							fortification: 1,
							countdown: -1,
							parent: rect
						};
						rect.lumen = node;
						rect.on('click', clickHandler);
						this.gridLayer.add(rect);
						Grid.addNode(node.col, node.row, node);
						// console.log('Added Rectangle', rect);

						// Display fortification
						var fortificationText = new Kinetic.Text({
							x: rect.x() + rect.width()/2 - 10,
							y: rect.y() + rect.height()/2 - 15,
							text: node.fortification.toString(),
							fontSize: 30,
							fontFamily: 'Calibri',
							fill: 'white',
							listening: false // don't block clicks
						});
						node.fortificationText = fortificationText;
						this.fortificationLayer.add(fortificationText);
					}
				}

				stage.add(this.gridLayer);
				stage.add(this.fortificationLayer);
			},
			onNodeClicked: function onNodeClicked (event) {
				if (this.paused) return false;

				var shape = event.targetNode;
				var node = shape.lumen;

				if (!this.isValidClick(this.currentPlayer, node)) {
					Messaging.postMessage('You can\'t move there!');
					return false;
				}
				// Click on an owned position
				if (node.player == this.currentPlayer) {
					// Fortify and add to dissemination list
					node.fortification++;
					this.prepareForDissemination(node);
				}
				node.player = this.currentPlayer;
				this.updateProps(node);

				this.nextTurn(node);
			},
			prepareForDissemination: function prepareForDissemination (node) {
				if (!this.disseminationList[COUNTDOWN]) {
					this.disseminationList[COUNTDOWN] = [];
				}
				// Reset dissemination countdown, remove it from the list
				if (node.countdown >= 0) {
					var list = this.disseminationList[node.countdown];
					var ix = list.indexOf(node);
					list.splice(ix, 1);
				}
				// Add at the top of the countdown list
				node.countdown = COUNTDOWN;
				this.disseminationList[COUNTDOWN].push(node);
			},
			nextTurn: function nextTurn () {
				this.updateDissemination();
				this.redrawStage();
				this.turn++;
				this.currentPlayer = this.currentPlayer == 1 ? 2 : 1;
			},
			updateDissemination: function updateDissemination () {
				for (var i = 0; i <= COUNTDOWN; i++) {
					var curList = this.disseminationList[i];
					if (!curList) {
						continue;
					}
					var lowerList = i === 0 ? [] : this.disseminationList[i-1];
					if (!lowerList) {
						lowerList = this.disseminationList[i-1] = [];
					}

					// Either disseminate or push all nodes to the lower list
					// Decreasing the countdown
					var len = curList.length;
					while (len--) {
						var node = curList[len];
						// Only update nodes of the current player
						if (node.player != this.currentPlayer) {
							continue;
						}

						node.countdown--;
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
						if (otherNode && otherNode.player == node.player) {
							console.log('->', otherNode);
							otherNode.fortification++;
							this.updateProps(otherNode);
						}
					}
				}
			},
			redrawStage: function redrawStage () {
				this.gridLayer.draw();
				this.fortificationLayer.draw();
			},
			setStartingNode: function setStartingNode (player, col, row) {
				var node = Grid.getNode(col, row);
				if (!node) {
					alert('WRONG STARTING POSITION MATE! ', col, row);
				}
				node.player = player;
				this.updateProps(node);
				this.redrawStage();
			},
			updateProps: function updateProps (node) {
				var shape = node.parent;
				switch (node.player) {
					case 1:
						shape.setAttr('fill', 'red');
						break;
					case 2:
						shape.setAttr('fill', 'blue');
						break;
					default:
						shape.setAttr('fill', '#00D2FF');
						break;
				}
				node.fortificationText.setAttr('text', node.fortification.toString());
			},
			isValidClick: function isValidClick (player, node) {
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
			}
		};
	}]);
})();