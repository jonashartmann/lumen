(function() {
	'use strict';
	angular.module('lumen.hud')
	.controller('HudCtrl', ['$scope', 'GameManager', 'Messaging',
	function HudCtrl ($scope, GameManager, Messaging) {
		$scope.game = GameManager;
		$scope.messaging = Messaging;
		$scope.toggleGameState = function toggleGameState () {
			GameManager.paused = !GameManager.paused;
		};
		$scope.getToggleButtonLabel = function getToggleButtonLabel () {
			return GameManager.paused ? 'Play' : 'Pause';
		};
	}]);
})();