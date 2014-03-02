(function(Lumen) {
	'use strict';
	Lumen.controller('LumenCtrl', ['$scope', 'Grid', 'GameManager',
	function LumenCtrl ($scope, Grid, GameManager) {
		Grid.init(5, 5);
		GameManager.init(1);

		// Fill the grid after the stage is ready
		$scope.$on('KINETIC:READY', function kineticReady (event, stage) {
			GameManager.createGrid(stage);
			GameManager.setStartingNode(1, Math.floor(Grid.cols/2), 0); // player, col, row
			GameManager.setStartingNode(2, Math.floor(Grid.cols/2), Grid.rows-1);
		});
	}]);
})(window.Lumen);