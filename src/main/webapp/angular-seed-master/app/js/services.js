'use strict';
angular.module('myApp.services', []).
  service('Game', [function () {
	  return new Blokus.Game();
  }]);