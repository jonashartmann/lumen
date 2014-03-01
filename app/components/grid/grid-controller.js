(function() {
	'use strict';
	angular.module('hart.grid')
	.controller('GridCtrl', ['$scope', 'Grid', function GridCtrl ($scope, Grid) {
		Grid.init(6, 6);
		$scope.grid = Grid;
		$scope.$on('KINETIC:STAGE', function onStageCreated (event, stage) {
			var rectWidth = Math.floor(stage.getWidth() / Grid.cols);
			var rectHeight = Math.floor(stage.getHeight() / Grid.rows);
			console.log(rectWidth, rectHeight);
			$scope.opt = {
				width: rectWidth,
				height: rectHeight,
				fill: '#00D2FF',
				stroke: 'black',
				strokeWidth: 1,
				draggable:true
			};
		});
	}]);
})();