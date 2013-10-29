var main = function() {

	Client.protocol.register("games", function(data) {

		var games = JSON.parse(data);

		var showGame = function(ordinal) {
			var object = {
				type : 'ShowGame',
				data : {
					ordinal : ordinal
				}
			};
			return object;
		};

		var drawGameNumber = function(context, ordinal) {
			context.fillStyle = "rgba(255,255,255,0.90)";
			context.textAlign = "center";
			context.font = "bold 425% sans-serif";
			context.fillText(ordinal, 100, 122);
			context.strokeStyle = "rgba(255,255,255,0.45)";
			context.lineWidth = 4;
			context.strokeText("" + ordinal, 100, 122);
		};

		for ( var i = 0; i < games.length; ++i) {
			var canvas = document.createElement("canvas");
			var ordinal = games[i];
			canvas.setAttribute("id", "game-" + ordinal);
			canvas.setAttribute("width", "200px");
			canvas.setAttribute("height", "200px");
			$("#games").append(canvas);
			var context = canvas.getContext("2d");
			context.fillStyle = "rgba(0,0,0,0.350)";
			context.fillRect(0, 0, 200, 200);
			drawGameNumber(context, ordinal);
			client.say(showGame(ordinal));
		}

		Client.protocol.register("game", function(data) {
			var ordinal = data.game;
			var canvas = document.getElementById('game-' + ordinal);
			var boardRendering = new BoardRendering(new CellRendering(canvas, 10, 10, 9.5, 9.5));
			boardRendering.clear("#2a2d30"); // TODO à revoir
			boardRendering.update(new Board(JSON.parse(data.board)));
		});

	});

});
    
};