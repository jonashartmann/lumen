(function() {
	'use strict';

	var Node = function Node (opts) {
		return {
			col: opts.col,
			row: opts.row,
			player: opts.player || 0,
			fortification: opts.fortification || 1,
			countdown: opts.countdown || -1,
			parent: opts.parent || null,
			fortificationText: new Kinetic.Text({
				x: opts.parent.x() + opts.parent.width()/2 - 10,
				y: opts.parent.y() + opts.parent.height()/2 - 15,
				text: opts.fortification.toString(),
				fontSize: 30,
				fontFamily: 'Calibri',
				fill: 'white',
				listening: false // don't block clicks
			}),
			/**
			 * Updates the shape properties linked to the node
			 */
			updateProps: function updateProps () {
				var shape = this.parent;
				switch (this.player) {
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
				this.fortificationText.setAttr('text', this.fortification.toString());
				this.countdownText.setAttr('text', this.countdown.toString());
				this.countdownText.setAttr('visible', this.countdown >= 0);
			}
		};
	};

	angular.module('lumen.game')
	.service('GameManager', ['$rootScope', 'Grid', 'Messaging', 'LumenLogic',
	function GameManager ($rootScope, Grid, Messaging, LumenLogic) {
		var COUNTDOWN = 3,
			MAX_FORTIFICATION = 99;

		return {
			currentPlayer: 1,
			turn: 0,
			paused: false,
			stage: null,
			gridLayer: null,
			textLayer: null,
			init: function init (startingPlayer) {
				this.currentPlayer = startingPlayer;
				this.paused = false;
				this.gridLayer = new Kinetic.Layer();
				this.textLayer = new Kinetic.Layer();
				this.turn = 1;
			},
			createGrid: function createGrid (stage) {
				this.stage = stage;

				var rectWidth = Math.floor(stage.getWidth() / Grid.cols - Grid.cols/2);
				var rectHeight = Math.floor(stage.getHeight() / Grid.rows - Grid.rows/2);

				var self = this;
				function clickHandler (e) {
					// Use $apply to put the callback inside angular digest cycle
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
						var node = new Node({
							col: i,
							row: j,
							player: 0,
							fortification: 1,
							countdown: -1,
							parent: rect
						});
						rect.lumen = node;
						rect.on('click', clickHandler);
						this.gridLayer.add(rect);
						Grid.addNode(node.col, node.row, node);
						// console.log('Added Rectangle', rect);

						this.textLayer.add(node.fortificationText);

						// Display countdown
						var countdownText = new Kinetic.Text({
							x: rect.x(),
							y: rect.y(),
							text: node.countdown.toString(),
							fontSize: 10,
							fontFamily: 'Calibri',
							fill: 'white',
							visible: false,
							listening: false // don't block clicks
						});
						node.countdownText = countdownText;
						this.textLayer.add(countdownText);
					}
				}

				stage.add(this.gridLayer);
				stage.add(this.textLayer);
			},
			/**
			 * Called when any node is clicked in the board
			 */
			onNodeClicked: function onNodeClicked (event) {
				if (this.paused) return false;

				var shape = event.targetNode;
				var node = shape.lumen;

				if (!LumenLogic.nodeClicked(node, this.currentPlayer)) {
					Messaging.postMessage('You can\'t move there!');
					return false;
				}

				// Update visual properties
				node.updateProps();

				// After a valid click, we move to the next turn
				this.nextTurn();
			},
			/**
			 * Moves to the next turn, updating any logic necessary
			 */
			nextTurn: function nextTurn () {
				LumenLogic.endTurn(this.turn, this.currentPlayer);
				this.redrawStage();
				this.turn++;
				this.changePlayer();

				if (LumenLogic.isGameOver()) {
					alert('Game Over!');
				}
			},
			changePlayer: function changePlayer () {
				this.currentPlayer = this.currentPlayer == 1 ? 2 : 1;
			},
			redrawStage: function redrawStage () {
				this.gridLayer.draw();
				this.textLayer.draw();
			},
			setStartingNode: function setStartingNode (player, col, row) {
				var node = Grid.getNode(col, row);
				if (!node) {
					alert('WRONG STARTING POSITION MATE! ', col, row);
				}
				node.player = player;
				node.updateProps();
				this.redrawStage();
			}
		};
	}]);
})();