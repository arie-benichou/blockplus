/**
 * @constructor
 */
Application = function(parameters) {

	this.viewPort = new ViewPort({
		maxWidth : parameters.maxWidth,
		maxHeight : parameters.maxHeight
	});

	this.container = parameters.container;
	this.container.width(this.viewPort.maxWidth);
	this.container.height(this.viewPort.maxHeight);
	this.container.bind("mousedown", function(event) {
		event.preventDefault();
	});

	this.audioManager = new AudioManager(parameters.audio);

	// TODO ///////////////////////////////////////////////////////
	this.colors = {
		Blue : "#3971c4",
		Yellow : "#eea435",
		Red : "#cc2b2b",
		Green : "#04a44b"
	};
	this.positionFactory = new Blockplus.Positions(20, 20);
	var pieceRenderer = new Blockplus.PieceRenderer(this.viewPort, this.colors, this.positionFactory);
	this.pieceManager = new Blockplus.PieceManager("pieces", pieceRenderer, "/meta/pieces.json", this.positionFactory);
	// ////////////////////////////////////////////////////////////

	this.container.show();

	this.messages = new Blockplus.Messages();
	var url = document.location.origin.toString().replace("http://", "ws://") + "/io";
	this.client = new Transport.Client(url, new Transport.Protocol());
	this.user = "somebody";
	this.client.register("open", $.proxy(function() {
		$(window).bind("beforeunload", $.proxy(function() {
			this.client.say(this.messages.disconnect());
		}, this));
		var games = null; // JSON.parse(data);
		$("#splash").css("cursor", "pointer");
		$("#splash").show();
		$("#splash").one(
				"click",
				$.proxy(function() {
					this.audioManager.play("../audio/some.mp3");
					$("#splash").hide();
					new Blockplus.Patio($("#patio"), this.viewPort, this.audioManager, this.client, this.messages, games, this.colors, this.positionFactory,
							this.pieceManager);
				}, this));
		this.client.say(this.messages.connection(this.user));
	}, this));
	this.client.register("close", $.proxy(function() {
		this.audioManager.play("../audio/none.mp3");
		this.container.css("opacity", 0.25);
	}, this));
	this.client.register("error", $.proxy(function() {
		this.audioManager.play("../audio/none.mp3");
		this.container.css("opacity", 0.25);
	}, this));
	this.client.start();
};
Application.prototype = {
	constructor : Application
// join : function() {
// this.audioManager.play("../audio/in.mp3");
// this.patio.join();
// },
};