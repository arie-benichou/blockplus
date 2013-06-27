var Blockplus = Blockplus || {};
Blockplus.Patio = function(viewPort, audioManager, client, messages, games) {
	this.viewPort = viewPort;
	this.audioManager = audioManager;
	this.client = client;
	this.messages = messages;
	this.games = games;
	this.game = null; // TODO
};
Blockplus.Patio.prototype = {
	constructor : Blockplus.Patio,
	join : function() {
		var max = this.games.length;
		var game = 1;
		this.client.register("fullGame", $.proxy(function(data) {
			if (game < max)
				this.client.say(this.messages.gameConnection(++game));
			else
				this.audioManager.play("../audio/none.mp3");
		}, this));
		this.client.say(this.messages.gameConnection(game));
		this.game = new Blockplus.Game(this.viewPort, this.audioManager, this.client, this.messages);
	},
};