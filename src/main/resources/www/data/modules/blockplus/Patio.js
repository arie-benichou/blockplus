var Blockplus = Blockplus || {};
Blockplus.Patio = function(container, viewPort, audioManager, client, messages, games, colors, positionFactory, pieceManager) {

	this.viewPort = viewPort;

	this.container = container;
	this.container.height(this.viewPort.maxHeight);
	this.container.show();

	this.audioManager = audioManager;
	this.client = client;
	this.messages = messages;
	this.games = games;
	this.colors = colors;
	this.positionFactory = positionFactory;
	this.pieceManager = pieceManager;
	this.game = null; // TODO

	var players = [];
	for (color in this.colors) {
		players.push(color.toLowerCase());
	}
	console.debug(players);

	this.client.register("tables", $.proxy(function(tables) {
		$("#tables").html("");
		var sortedTables = {
			3 : [],
			2 : [],
			1 : [],
			0 : []
		}
		for ( var id in tables) {
			sortedTables[tables[id]].push(id);
		}
		for ( var numberOfPlayers = 3; numberOfPlayers >= 0; --numberOfPlayers) {
			var array = sortedTables[numberOfPlayers];
			for ( var k = 0, n = array.length; k < n; ++k) {
				var id = array[k];
				var table = $(document.createElement("div"));
				table.addClass("table");
				for ( var i = 0; i < numberOfPlayers; ++i) {
					table.append('<div class="color ' + players[i] + '">&nbsp;</div>');
				}
				var next = $(document.createElement("div"));
				next.attr("id", id);
				next.addClass("color");
				next.addClass(players[i]);
				next.html("?");
				next.css("cursor", "pointer");
				next.one("click", $.proxy(function(event) {
					var id = $(event.currentTarget).attr('id');
					var message = this.messages.gameConnection(id);
					this.client.register("game", $.proxy(function(data) {
						$("#patio").hide();
						$("#game").show();
						$("#players").show();
						this.game = new Blockplus.Game(this.viewPort, this.audioManager, this.client, this.messages, this.colors, this.positionFactory,
								this.pieceManager, data);
					}, this));
					this.client.say(message);
				}, this));
				table.append(next);
				table.append('<div class="table-id-container">' + '<div class="table-id">' + ('000' + id).slice(-4)
						+ '</div></div><div style="clear: both;"></div>');
				$("#tables").append(table);
			}
		}
	}, this));
	this.client.say({
		type : 'InPatio',
		data : {}
	});

};
Blockplus.Patio.prototype = {
	constructor : Blockplus.Patio,
// join : function() {
// var max = this.games.length;
// var game = 1;
// this.client.register("fullGame", $.proxy(function(data) {
// if (game < max)
// this.client.say(this.messages.gameConnection(++game));
// else
// this.audioManager.play("../audio/none.mp3");
// }, this));
// this.client.say(this.messages.gameConnection(game));
// this.game = new Blockplus.Game(this.viewPort, this.audioManager, this.client,
// this.messages, this.colors, this.positionFactory, this.pieceManager);
// },
};