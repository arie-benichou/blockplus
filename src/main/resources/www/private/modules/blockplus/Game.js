var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.Game = function(viewPort, audioManager, client, messages, colors, positionFactory, pieceManager, gameData) {
	this.gameData = gameData;
	this.viewPort = viewPort;
	this.audioManager = audioManager;
	this.client = client;
	this.messages = messages;
	/*-----------------------8<-----------------------*/
	this.colors = colors;
	this.positionFactory = positionFactory;
	this.pieceManager = pieceManager;
	/*-----------------------8<-----------------------*/
	this.board = new Blockplus.Board({
		dimension : {
			rows : 20,
			columns : 20
		},
		cells : {
			blue : [ 0, 21, 42, 63, 84, 105, 126, 147, 168, 189 ],
			yellow : [ 19, 38, 57, 76, 95, 114, 133, 152, 171, 190 ],
			red : [ 380, 361, 342, 323, 304, 285, 266, 247, 228, 209 ],
			green : [ 399, 378, 357, 336, 315, 294, 273, 252, 231, 210 ]
		}
	});
	/*-----------------------8<-----------------------*/
	this.cellDimension = {
		width : this.viewPort.min / this.board.rows,
		height : this.viewPort.min / this.board.columns
	};
	/*-----------------------8<-----------------------*/
	this.maxPieceSize = 5;
	this.neighbourhood = this.maxPieceSize - 1;
	this.scale = {
		x : this.board.columns / (2 * this.neighbourhood + 1),
		y : this.board.rows / (2 * this.neighbourhood + 1)
	};
	/*-----------------------8<-----------------------*/
	this.boardRenderer = new Blockplus.BoardRenderer(document.getElementById('board'), this.cellDimension, this.colors);
	this.selectedPositions = new Blockplus.SelectedPositions();
	this.boardManager = new Blockplus.BoardManager(this.board, this.boardRenderer, this.positionFactory, this.selectedPositions, this.viewPort);
	this.controlPanelManager = new Blockplus.ControlPanelManager(document.getElementById('control-panel'), this.viewPort, this.audioManager, this.colors,
			this.positionFactory);
	/*-----------------------8<-----------------------*/
	/*
	 * $("#players div").bind('click', $.proxy(function(event) { // TODO var
	 * color = $(event.currentTarget).attr('class');
	 * this.pieceManager.show(color); }, this));
	 */
	/*-----------------------8<-----------------------*/
	// this.client.register("game", $.proxy(function(data) {
	$("#splash").hide();
	$("#control-panel").hide();
	this.boardManager.render(this.board);
	var players = [];
	for (color in this.colors) {
		players.push(color);
	}
	// TODO !!! le serveur doit envoyer les pieces du joueurs
	var pieces = Blockplus.GameState.prototype._decodePieces({
		0 : 4194303
	});
	for ( var i = 0; i < this.gameData.players - 1; ++i) {
		this.pieceManager.init(players[i], pieces[0]);
		$($("#players div")[i]).addClass(players[i]);
		$($("#players div")[i]).removeClass("not-available");
		$($("#players div")[i]).css("cursor", "pointer");
		$($("#players div")[i]).bind('click', $.proxy(function(event) {
			var color = $(event.currentTarget).attr('class');
			this.pieceManager.show(color);
		}, this));
		// $($("#players div")[i]).html("&nbsp;")

	}

	// TODO !!!
	var i = this.gameData.players - 1;
	this.color = players[i];

	this.client.register("player", $.proxy(function(data) {
		this.audioManager.play("../audio/in.mp3");
		this.pieceManager.init(players[i], pieces[0]);
		$($("#players div")[i]).html("0");
		$($("#players div")[i]).addClass(players[i]);
		$($("#players div")[i]).css("cursor", "pointer");
		$($("#players div")[i]).bind('click', $.proxy(function(event) {
			var color = $(event.currentTarget).attr('class');
			this.pieceManager.show(color);
		}, this));
		// $($("#players div")[i]).removeClass("not-available");
		if (i < 3) {
			$($("#players div")[i + 1]).css("cursor", "pointer");
			$($("#players div")[i + 1]).css("color", "white");
			$($("#players div")[i + 1]).html("AI ?")
			$($("#players div")[i + 1]).one('click', $.proxy(function(event) {
				var message = this.messages.virtualPlayer(this.gameData.id);
				this.client.say(message);
				$(event.currentTarget).html("0");
			}, this));
		}
		++i;
	}, this));

	this.pieceManager.show(players[this.gameData.players - 1]);

	// /////////////////////////////////////////////////////////////////////

	this.client.register("notification", $.proxy(function(data) {
		console.debug(this.color);
		console.debug(data.to);
		if (data.to == this.color) { // TODO !!! à revoir coté serveur
			$("#messages").removeClass();
			$("#messages").addClass("from" + data.from);
			$('#messages').html(data.message); // TODO xss
			jQuery('#messages').fadeIn().delay(3000).fadeOut();
		}
	}, this));

	$('#players div').dblclick($.proxy(function() {
		// $(".history").scrollTop($(".history p").height() * $(".history
		// p").length);
		var alterColor = $("#pieces").attr("class");
		$("#input").removeClass();
		$("#input").addClass("to" + alterColor);
		$("#input").addClass("from" + this.color);
		$("#input span").html("&nbsp;");
		$("#input").show();
		$("#input [contenteditable]").focus();

		$("#input span").bind("blur", function() {
			$("#input").hide();
		});

		$(document).keyup(function(e) {
			if (e.keyCode == 27) {
				$("#input [contenteditable]").blur();
				$("#input").hide();
			}
		});

		$("#input [contenteditable]").bind("keypress.key13", $.proxy(function(event) {
			if (event.which == 13) {
				$("#input [contenteditable]").unbind("keypress.key13");
				$("#input [contenteditable]").blur();
				var message = $("#input [contenteditable]").html();
				var from = this.color;
				var to = alterColor;
				this.client.say(this.messages.notification(from, to, message)); // TODO
				// xss
			}
		}, this))
	}, this));
	// /////////////////////////////////////////////////////////////////////

	// TODO à déplacer
	this.controlPanelManager.register('click', this.init);
	this.client.register("update", $.proxy(function(data) {
		$("#board-container").show();
		$("#pieces").show();
		var newGameState = new Blockplus.GameState(data);
		// this.color = newGameState.getColor(); // TODO à faire dans le update
		this.boardManager.updateColor(this.color); // TODO à revoir
		// this.client.register("update", $.proxy(function(data) {
		this.update(newGameState);
		// }, this));
	}, this));
	// }, this));
	/*-----------------------8<-----------------------*/
	this.clickEventHandler1 = $.proxy(function(event) {
		var targetOffset = $(event.target).offset();
		var offsetX = event.pageX - targetOffset.left;
		var offsetY = event.pageY - targetOffset.top;
		var position = this.boardManager.positionFromOffset(offsetX, offsetY);
		this.boardManager.unregister('click.1');
		var referential = this.boardManager.zoomInTopLeftPosition(position, this.neighbourhood);
		var translation = {
			x : -(referential.column * this.cellDimension.width - 0) * this.scale.x + 0,
			y : -(referential.row * this.cellDimension.height - 0) * this.scale.y + 0
		}
		var context = this.boardManager.renderer.context;
		context.save();
		context.translate(translation.x, translation.y);
		context.scale(this.scale.x, this.scale.y);
		this.boardManager.render();
		var clickEventHandler2 = $.proxy(function(event) {
			var targetOffset = $(event.target).offset();
			var offsetX = event.pageX - targetOffset.left;
			var offsetY = event.pageY - targetOffset.top;
			var p = this.boardManager.positionFromOffset(offsetX / this.scale.x, offsetY / this.scale.y);
			var position = this.positionFactory.position(p.row + referential.row, p.column + referential.column);
			var positionID = this.positionFactory.indexFromPosition(position);
			var isPotentialPosition = this.isPotentialPosition(positionID);
			if (isPotentialPosition) {
				if (this.boardManager.hasSelection(position))
					this.boardManager.unselect(position, this.color);
				else if (positionID in this.options.matchPotentialPositions(this.boardManager.selectedPositions))
					this.boardManager.select(position, this.color);
			}
			if (this.boardManager.selectedPositions.isEmpty())
				this.init();
			else
				this.controlPanelManager.handle(this.options, this.boardManager.selectedPositions, this.boardManager, this.color);
		}, this);
		this.boardManager.register('click.2', clickEventHandler2);
	}, this);
	/*-------------------------------8<-------------------------------*/
	this.init = $.proxy(function() {
		this.boardManager.unregister('click.2');
		this.controlPanelManager.unregister('click');
		this.controlPanelManager.hide();
		this.controlPanelManager.register('click', this.init);
		if (!this.boardManager.selectedPositions.isEmpty()) {
			this.boardManager.potentialPositions = {}; // TODO
		}
		this.boardManager.renderer.context.restore();
		this.boardManager.render();
		if (!this.boardManager.selectedPositions.isEmpty()) {
			// ////////////////////////////////////////////////
			// TODO utiliser un objet piece
			var data = [];
			this.boardManager.renderSelectedCells(this.boardManager.selectedPositions.get(), this.color);
			for (position in this.boardManager.selectedPositions.get()) {
				data.push(position);
				this.boardManager.renderPotentialCell(this.positionFactory.positionFromIndex(position), this.color);
			}
			// var pieceId =
			// this.options.perfectMatch(this.boardManager.selectedPositions);
			this.play(data);
			// ////////////////////////////////////////////////
		} else {
			this.boardManager.register('click.1', this.clickEventHandler1);
		}
		this.boardManager.clearSelection();
	}, this);

	this.controlPanelManager.register('click', this.init);
};
Blockplus.Game.prototype = {
	constructor : Blockplus.Game,

	update : function(gameState) {

		if (gameState.getColor() == this.color) {
			this.boardManager.unregister('click.1');
			this.boardManager.register('click.1', this.clickEventHandler1);
			this.pieceManager.show(this.color);
		}

		for (color in this.colors) {
			this.pieceManager.update(color, gameState.getPieces(color));
		}

		this.boardManager.updateBoard(gameState.getBoard());

		this.options = new Blockplus.Options(gameState.getOptions(this.color));
		// console.debug(this.options);

		var potentialPositions = this.options.getPotentialPositions();
		// console.debug(potentialPositions);

		this.boardManager.updatePotentialPositions(potentialPositions);
		this.boardManager.render();

		var leader;
		var bestScore = 0;
		for (color in this.colors) {
			var score = this.boardManager.board.getCells(color).length;
			if (score > bestScore) {
				bestScore = score;
				leader = color;
			}
			$("#players div." + color).html(score);
		}
		if (gameState.isTerminal()) {
			this.audioManager.play("../audio/none.mp3");
			$("#board-container").css("opacity", 0.45);
			$("#players div").css("opacity", 0.33);
			$("#players div." + leader).css("opacity", 1);
			$("#players div." + leader).css("font-weight", "bold");
			this.pieceManager.show(leader);
		} else {
			if (gameState.getColor() == this.color) {
				this.audioManager.play("../audio/some.mp3");
			}
			// TODO on Game Ready
			$("#board-container").removeClass();
		}

		/*
		 * var options = gameState.getOptions(this.color); for ( var i = 21; i >
		 * 0; --i) { if (i in options) { this.play(options[i][0]); break; } }
		 */

	},

	play : function(positions) {
		this.client.say(this.messages.moveSubmit(positions));
	},

	isPotentialPosition : function(position) {
		return position in this.options.getPotentialPositions();
	}

};