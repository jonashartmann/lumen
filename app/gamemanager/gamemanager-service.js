(function() {
	'use strict';
	angular.module('lumen.game')
	.service('GameManager', ['$rootScope', 'Grid', 'Messaging',
	function GameManager ($rootScope, Grid, Messaging) {
		return {
			init: function init (startingPlayer) {
				this.currentPlayer = startingPlayer;
				this.paused = false;
			},
			createGrid: function createGrid (stage) {
				var rectWidth = Math.floor(stage.getWidth() / Grid.cols - Grid.cols/2);
				var rectHeight = Math.floor(stage.getHeight() / Grid.rows - Grid.rows/2);
				console.log(rectWidth, rectHeight);
				var layer = new Kinetic.Layer();

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
							parent: rect
						};
						rect.lumen = node;
						rect.on('click', clickHandler);
						layer.add(rect);
						Grid.addNode(node.col, node.row, node);
						// console.log('Added Rectangle', rect);
					}
				}

				stage.add(layer);
			},
			onNodeClicked: function onNodeClicked (event) {
				if (this.paused) return false;

				var shape = event.targetNode;
				var node = shape.lumen;

				if (!this.isValidClick(this.currentPlayer, node)) {
					Messaging.postMessage('You can\'t move there!');
					return false;
				}
				node.player = this.currentPlayer;
				this.currentPlayer = this.currentPlayer == 1 ? 2 : 1;
				this.adjustNodeFill(node);
			},
			setStartingNode: function setStartingNode (player, col, row) {
				var node = Grid.getNode(col, row);
				if (!node) {
					alert('WRONG STARTING POSITION MATE! ', col, row);
				}
				node.player = player;
				this.adjustNodeFill(node);
			},
			adjustNodeFill: function adjustNodeFill (node) {
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
				shape.draw();
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
				if (otherNode) {
					isNeighbour = isNeighbour || otherNode.player == player;
				}
				otherNode = Grid.getNode(node.col+1, node.row);
				if (otherNode) {
					isNeighbour = isNeighbour || otherNode.player == player;
				}
				otherNode = Grid.getNode(node.col, node.row-1);
				if (otherNode) {
					isNeighbour = isNeighbour || otherNode.player == player;
				}
				otherNode = Grid.getNode(node.col, node.row+1);
				if (otherNode) {
					isNeighbour = isNeighbour || otherNode.player == player;
				}
				return isNeighbour;
			}
		};
	}]);
})();