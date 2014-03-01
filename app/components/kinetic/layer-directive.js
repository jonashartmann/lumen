(function() {
    'use strict';
    angular.module('kinetic')
    .directive('layer', function layerDirective () {
        return {
            restrict: 'EA',
            link: function linkFn (scope, elem, attrs) {
                var layer = new Kinetic.Layer();
                console.log('New Layer Created', layer);
                scope.$broadcast('KINETIC:LAYER', layer);

                scope.$on('KINETIC:STAGE', function onStageCreated (event, stage) {
                    stage.add(layer);
                    console.log('Layer Added', layer);
                });
            }
        };
    });
})();