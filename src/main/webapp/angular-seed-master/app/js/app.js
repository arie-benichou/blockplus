'use strict';
angular.module('myApp', [
  'ngRoute',
  'myApp.filters',
  'myApp.services',
  'myApp.directives',
  'myApp.controllers'
]).
config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/', {templateUrl: 'partials/new-game.html', controller: 'NewGame'});
  $routeProvider.when('/rendering', {templateUrl: 'partials/rendering.html', controller: 'Rendering'});
  $routeProvider.otherwise({redirectTo: '/'});
}]);