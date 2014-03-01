(function() {
	'use strict';
	angular.module('kinetic')
	.directive('stage', function stageDirective () {
		return {
			restrict: 'EA',
			link: function linkFn (scope, elem, attrs) {
				var id = attrs["id"];
				if (!id) {
					id = Math.random().toString(36).substring(7);
					elem.attr('id', id);
				}
				var kinetic = {
					stage: new Kinetic.Stage({
						container: id,
						width: 800,
						height: 600
					})
				};

				scope.kinetic = kinetic;
				console.log('New Stage Created', kinetic.stage);

				scope.$broadcast('KINETIC:STAGE', kinetic.stage);
			}
		};
	});
})();