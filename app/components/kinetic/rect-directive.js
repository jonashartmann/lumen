(function() {
    'use strict';
    angular.module('kinetic')
    .directive('rect', function rectDirective () {
        return {
            restrict: 'EA',
            link: function linkFn (scope, elem, attrs) {
                var posX = parseInt(attrs["posX"], 10) || 0;
                var posY = parseInt(attrs["posY"], 10) || 0;
                var options = {
                    x: posX,
                    y: posY,
                    width: 100,
                    height: 50,
                    fill: '#00D2FF',
                    stroke: 'black',
                    strokeWidth: 4,
                    draggable: true
                };
                var rect = new Kinetic.Rect(options);

                // add cursor styling
                rect.on('mouseover', function () {
                    document.body.style.cursor = 'pointer';
                });
                rect.on('mouseout', function () {
                    document.body.style.cursor = 'default';
                });
                console.log('New Rectangle Created', rect);

                scope.$on('KINETIC:LAYER', function onLayerCreated (event, layer) {
                    if (!rect.getLayer()) {
                        layer.add(rect);
                        console.log('Rectangle Added', rect);
                    }
                });
            }
        };
    });
})();