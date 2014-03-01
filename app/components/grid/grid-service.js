(function() {
	'use strict';

	angular.module('hart.grid')
	.service('Grid', function GridService () {
		var positions = {};
		var Grid = {
			init: function init (cols, rows) {
				this.cols = cols;
				this.rows = rows;
			},
			addNode: function addNode (col, row, node) {
				positions[col+''+row] = node;
			},
			getNode: function getNode (col, row) {
				return positions[col+''+row];
			}
		};
		return Grid;
	});

})();