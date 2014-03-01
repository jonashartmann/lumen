(function() {
	'use strict';

	angular.module('hart.grid')
	.service('Grid', function GridService () {
		var Grid = {
			init: function init (cols, rows) {
				this.cols = cols;
				this.rows = rows;
			}
		};
		return Grid;
	});

})();