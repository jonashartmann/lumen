(function(Lumen) {
	'use strict';
	Lumen.controller('LumenCtrl', ['$scope', 'Grid',
	function LumenCtrl ($scope, Grid) {
		Grid.init(6, 6);
		$scope.grid = Grid;
	}]);
})(window.Lumen);