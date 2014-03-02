(function() {
	'use strict';
	angular.module('lumen.hud')
	.filter('PlayerFilter', [
	function PlayerFilter () {
		return function (val) {
			return val === 1 ? 'Player One' : 'Player Two';
		};
	}])
	.filter('PauseFilter', [
	function PauseFilter () {
		return function (val) {
			return val ? 'Paused' : 'Playing';
		};
	}]);
})();