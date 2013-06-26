Application = function(parameters) {
	this.container = parameters.container;
	this.viewPort = new ViewPort({
		maxWidth : parameters.maxWidth,
		maxHeight : parameters.maxHeight,
	});
	this.audioManager = new Blockplus.AudioManager(parameters.audio);
	this.container.width(this.viewPort.maxWidth);
	this.container.height(this.viewPort.maxHeight);
	var url = document.location.origin.toString().replace('http://', 'ws://') + "/network/io";
	this.client = new Transport.Client(url, new Transport.Protocol());
	this.container.one('click', $.proxy(this.run, this));
	this.container.bind('mousedown', function(event) {
		event.preventDefault();
	});
};

Application.prototype = {
	constructor : Application,
	run : function() {
		this.audioManager.play("../audio/dummy.mp3"); // android audio hack
		this.client.register("open", $.proxy(function() {
			var user = "somebody";
			var messages = new Blockplus.Messages();
			this.client.say(messages.connection("somebody"));
			this.client.register("games", $.proxy(function(data) {
				var max = JSON.parse(data).length;
				var game = 1;
				this.client.register("fullGame", $.proxy(function(data) {
					if (game < max)
						this.client.say(messages.gameConnection(++game));
					else
						this.audioManager.play("../audio/none.mp3");
				}, this));
				this.client.say(messages.gameConnection(game));
				this.game = new Blockplus.Game(this.viewPort, this.audioManager, this.client, messages);
			}, this));
		}, this));
		this.client.register("close", $.proxy(function() {
			this.audioManager.play("../audio/none.mp3"); // TODO
		}, this));
		this.client.register("error", $.proxy(function() {
			this.audioManager.play("../audio/none.mp3"); // TODO
		}, this));
		this.client.start();
		return false;
	},
};