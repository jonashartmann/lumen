(function(Lumen) {
	'use strict';
	Lumen.controller('LumenCtrl', ['$scope', 'Grid',
	function LumenCtrl ($scope, Grid) {
		$scope.grid = Grid;
	}]);
})(window.Lumen);