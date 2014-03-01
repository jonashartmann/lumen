(function() {
	'use strict';
	angular.module('lumen.game')
	.service('GameManager', function GameManager () {
		return {
			onPositionClicked: function onPositionClicked (event) {
				var pos = event.targetNode;
				var tween = new Kinetic.Tween({
					node: pos,
					opacity: 0.5,
					duration: 0.1,
					easing: Kinetic.Easings.EaseInOut
				});
				tween.play();
			}
		};
	});
})();