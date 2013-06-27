Application = function(parameters) {
	this.viewPort = new ViewPort({
		maxWidth : parameters.maxWidth,
		maxHeight : parameters.maxHeight,
	});
	this.container = parameters.container;
	this.container.width(this.viewPort.maxWidth);
	this.container.height(this.viewPort.maxHeight);
	this.container.bind('mousedown', function(event) {
		event.preventDefault();
	});
	this.audioManager = new Blockplus.AudioManager(parameters.audio);
	this.messages = new Blockplus.Messages();
	var url = document.location.origin.toString().replace('http://', 'ws://') + "/io";
	this.client = new Transport.Client(url, new Transport.Protocol());
	this.user = "somebody";
	this.patio = {
		join : function() {
			console.error("you are not connected yet");
		}
	}
	this.client.register("open", $.proxy(function() {
		this.client.register("games", $.proxy(function(data) {
			this.patio = new Blockplus.Patio(this.viewPort, this.audioManager, this.client, this.messages, JSON.parse(data));
			this.container.one('click', $.proxy(this.join, this));
		}, this));
		this.client.say(this.messages.connection(this.user));
	}, this));
	this.client.register("close", $.proxy(function() {
		this.audioManager.play("../audio/none.mp3");
		this.container.css("opacity", 0.25);
	}, this));
	this.client.register("error", $.proxy(function() {
		this.audioManager.play("../audio/none.mp3"); // TODO
	}, this));
	this.client.start();
};
Application.prototype = {
	constructor : Application,
	join : function() {
		console.debug(this.patio);
		this.patio.join();
	},
};