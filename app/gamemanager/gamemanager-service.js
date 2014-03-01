(function() {
	'use strict';
	angular.module('lumen.game')
	.service('GameManager', ['Grid',
	function GameManager (Grid) {
		return {
			init: function init (startingPlayer) {
				this.currentPlayer = startingPlayer;
			},
			createGrid: function createGrid (stage) {
				var rectWidth = Math.floor(stage.getWidth() / Grid.cols - Grid.cols/2);
				var rectHeight = Math.floor(stage.getHeight() / Grid.rows - Grid.rows/2);
				console.log(rectWidth, rectHeight);
				var layer = new Kinetic.Layer();

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
						rect.on('click', this.onNodeClicked.bind(this));
						layer.add(rect);
						Grid.addNode(node.col, node.row, node);
						// console.log('Added Rectangle', rect);
					}
				}

				stage.add(layer);
			},
			onNodeClicked: function onNodeClicked (event) {
				var shape = event.targetNode;
				var node = shape.lumen;
				node.player = this.currentPlayer;
				this.adjustNodeFill(node);
				this.currentPlayer = this.currentPlayer === 1 ? 2 : 1;
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
			}
		};
	}]);
})();