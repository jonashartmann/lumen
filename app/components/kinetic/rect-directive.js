(function() {
    'use strict';
    angular.module('kinetic')
    .directive('rect', function rectDirective () {
        return {
            restrict: 'EA',
            scope: {
                opt: '='
            },
            link: function linkFn (scope, elem, attrs) {
                var options = scope.opt;
                var posX = parseInt(attrs["posX"], 10) || 0;
                var posY = parseInt(attrs["posY"], 10) || 0;

                // Default options
                if (!options) {
                    options = {
                        width: 10,
                        height: 10,
                        fill: '#00D2FF',
                        stroke: 'black',
                        strokeWidth: 4,
                        draggable: true
                    };
                }
                // Position attributes override the options
                options.x = posX;
                options.y = posY;
                var rect = new Kinetic.Rect(options);

                // add cursor styling
                rect.on('mouseover', function () {
                    document.body.style.cursor = 'pointer';
                });
                rect.on('mouseout', function () {
                    document.body.style.cursor = 'default';
                });
                console.log('New Rectangle Created', rect);

                scope.$watch('opt', function optionsChanged (opt) {
                    rect.setAttrs(opt);
                    rect.draw(); // tells kinetic something has changed
                });

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