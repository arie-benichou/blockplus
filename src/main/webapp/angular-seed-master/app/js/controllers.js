'use strict';
angular.module('myApp.controllers', [])
	.controller('NewGame', ['Game', 
        function(game) {
			game.populate();
			game.registerEvents();
			game.refresh();
		}
    ])
    .controller('Rendering', ['$location', 'Game',
        function($location, game) {
    		var data = $location.search()['data']
    		var positions = game.parse(data.split(","));
        	game.populate();
        	game.resetCells();
        	game.render(positions);
		}
    ]);