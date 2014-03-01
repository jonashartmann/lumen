(function() {
	'use strict';
	angular.module('hart.grid')
	.controller('GridCtrl', ['$scope', 'Grid', function GridCtrl ($scope, Grid) {
		Grid.init(6, 6);
		$scope.grid = Grid;

		// Create grid UI after the stage is ready
		$scope.$on('KINETIC:READY', function onStageCreated (event, stage) {
			var rectWidth = Math.floor(stage.getWidth() / Grid.cols) - Grid.cols/2;
			var rectHeight = Math.floor(stage.getHeight() / Grid.rows) - Grid.rows/2;
			console.log(rectWidth, rectHeight);
			var layer = new Kinetic.Layer();

			for (var i = 0; i < Grid.cols; i++) {
				for (var j = 0; j < Grid.rows; j++) {
					var options = {
						x: (i * rectWidth) + (i*4),
						y: (j * rectHeight) + (j*4),
						width: rectWidth,
						height: rectHeight,
						fill: '#00D2FF',
						// stroke: 'black',
						draggable: false
					};
					var rect = new Kinetic.Rect(options);
					layer.add(rect);
					// console.log('Added Rectangle', rect);
				}
			}

			stage.add(layer);
		});
	}]);
})();