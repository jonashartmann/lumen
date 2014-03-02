(function() {
	'use strict';
	angular.module('hart.messaging')
	.service('Messaging', ['$timeout',
	function Messaging ($timeout) {
		return {
			msgs: [],
			postMessage: function postMessage (msg) {
				var self = this;
				self.msgs.push(msg);
				$timeout(function() {
					var ix = self.msgs.indexOf(msg);
					self.msgs.splice(ix, 1);
				}, 3000);
			},
			getMessages: function getMessages () {
				return this.msgs;
			}
		};
	}]);
})();