(function(Lumen) {
	'use strict';
	Lumen.controller('LumenCtrl', ['$scope', 'Grid', 'GameManager',
	function LumenCtrl ($scope, Grid, GameManager) {
		Grid.init(5, 5);
		GameManager.init(1);

		// Fill the grid after the stage is ready
		$scope.$on('KINETIC:READY', function kineticReady (event, stage) {
			GameManager.createGrid(stage);
			GameManager.setStartingNode(1, 2, 0);
			GameManager.setStartingNode(2, 2, 4);
		});
	}]);
})(window.Lumen);