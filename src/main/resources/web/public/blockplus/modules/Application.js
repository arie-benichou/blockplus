var Blockplus = Blockplus || {};

// TODO ? ne pas effectuer le zoom-in si le zoom est un zoom mort (avertir avec
// un none sound)
// TODO !? rendre optional le zoom

Blockplus.Application = function(parameters) {

	this.viewPort = new Blockplus.ViewPort({
		maxWidth : parameters.maxWidth,
		maxHeight : parameters.maxHeight,
	});

	$("#content").width(this.viewPort.maxWidth);
	$("#content").height(this.viewPort.maxHeight);

	console.debug(this.viewPort);

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

	this.cellDimension = {
		width : this.viewPort.min / this.board.rows,
		height : this.viewPort.min / this.board.columns
	};

	this.colors = {
		Blue : "#3971c4",
		// Yellow : "#eea435",
		Yellow : "rgb(247, 177, 42)",
		Red : "#cc2b2b",
		Green : "#04a44b"
	};

	/*-----------------------8<-----------------------*/

	this.maxPieceSize = 5;

	this.neighbourhood = this.maxPieceSize - 1;

	this.scale = {
		x : this.board.columns / (2 * this.neighbourhood + 1),
		y : this.board.rows / (2 * this.neighbourhood + 1)
	};

	/*-----------------------8<-----------------------*/

	this.audioManager = new Blockplus.AudioManager("test");

	this.boardRenderer = new Blockplus.BoardRenderer(document.getElementById('board'), this.cellDimension, this.colors);
	this.positionFactory = new Blockplus.PositionFactory();
	this.selectedPositions = new Blockplus.SelectedPositions();
	this.boardManager = new Blockplus.BoardManager(this.board, this.boardRenderer, this.positionFactory, this.selectedPositions, this.viewPort);
	this.controlPanelManager = new Blockplus.ControlPanelManager(document.getElementById('control-panel'), this.viewPort, this.audioManager, this.colors);
	var pieceRenderer = new Blockplus.PieceRenderer(this.viewPort, this.colors);

	/*-----------------------8<-----------------------*/

	this.pieceManager = new Blockplus.PieceManager("pieces", pieceRenderer, "./pieces/index.xml", this.positionFactory);

	/*-----------------------8<-----------------------*/

	var that = this;

	/*-----------------------8<-----------------------*/

	// TODO refactor to observer pattern...
	this.clickEventHandler1 = function(event) {

		console.debug("click");

		var targetOffset = $(event.target).offset();
		var offsetX = event.pageX - targetOffset.left;
		var offsetY = event.pageY - targetOffset.top;
		var position = that.boardManager.position(offsetX, offsetY);

		that.boardManager.unregister('click.1');

		var referential = that.boardManager.zoomInTopLeftCornerPosition(position, that.neighbourhood);
		var translation = {
			x : -(referential.minX * that.cellDimension.width - 0) * that.scale.x + 0,
			y : -(referential.minY * that.cellDimension.height - 0) * that.scale.y + 0
		}

		var context = that.boardManager.renderer.context;
		context.save();
		context.translate(translation.x, translation.y);
		context.scale(that.scale.x, that.scale.y);
		that.boardManager.render();

		var clickEventHandler2 = function(event) {
			var targetOffset = $(event.target).offset();
			var offsetX = event.pageX - targetOffset.left;
			var offsetY = event.pageY - targetOffset.top;
			var p = that.boardManager.position(offsetX / that.scale.x, offsetY / that.scale.y);
			var position = that.positionFactory.getPosition(p.row + referential.minY, p.column + referential.minX);

			var isPotentialPosition = JSON.stringify(position) in that.game.options.getPotentialPositions();
			if (isPotentialPosition) {
				if (that.boardManager.hasSelection(position))
					that.boardManager.unselect(position, that.color);
				else if (JSON.stringify(position) in that.game.options.matchPotentialPositions(that.boardManager.selectedPositions)) {
					that.boardManager.select(position, that.color);
				}
				if (that.boardManager.selectedPositions.isEmpty()) {
					that.init();
				} else {
					that.controlPanelManager.handle(that.game.options, that.boardManager.selectedPositions, that.boardManager, that.color);
				}
			} else if (that.boardManager.selectedPositions.isEmpty()) {
				that.init();
			}
		};
		that.boardManager.register('click.2', clickEventHandler2);

	};

	/*-------------------------------8<-------------------------------*/

	// TODO ! se contenter des positions
	this.moveSubmitHandler = function(event) {
		var selectedPositions = that.boardManager.selectedPositions;
		var pieceId = that.game.options.perfectMatch(selectedPositions);
		var data = [];
		for ( var position in selectedPositions.get()) {
			var p = JSON.parse([ position ]);
			data.push([ p.row, p.column ]);
		}

		var moveSubmit = function(id, positions) {
			var object = {
				type : 'MoveSubmit',
				data : {
					id : id,
					positions : positions
				}
			};
			return object;
		};

		that.client.say(moveSubmit(pieceId, data));
	};

	/*-------------------------------8<-------------------------------*/

	this.init = function() {

		that.boardManager.unregister('click.2');
		that.controlPanelManager.unregister('click');

		that.controlPanelManager.hide();
		that.controlPanelManager.register('click', that.init);

		if (!that.boardManager.selectedPositions.isEmpty()) {
			that.boardManager.potentialPositions = {}; // TODO
		}

		that.boardManager.renderer.context.restore();
		that.boardManager.render();

		if (!that.boardManager.selectedPositions.isEmpty()) {
			that.boardManager.renderSelectedCells(that.boardManager.selectedPositions.get(), that.color);
			for (position in that.boardManager.selectedPositions.get()) {
				that.boardManager.renderPotentialCell(JSON.parse(position), that.color);
			}
			that.moveSubmitHandler();
		} else {
			that.boardManager.register('click.1', that.clickEventHandler1);
		}

		that.boardManager.clearSelection();
	};

	/*-------------------------------8<-------------------------------*/

	var computeLocation = function(suffix) {
		return document.location.origin.toString().replace('http://', 'ws://').replace('https://', 'wss://') + suffix;
	};

	var connection = function(name) {
		var object = {
			type : 'Client',
			data : {
				name : name
			}
		};
		return object;
	};

	var gameConnection = function(n) {
		var message = {
			type : 'GameConnection',
			data : {
				ordinal : n
			}
		};
		return message;
	};

	Blockplus.Client.message = connection; // TODO à revoir
	Blockplus.Client.protocol.register("games", function(data) {
		var max = JSON.parse(data).length;
		var room = 1;
		Blockplus.Client.protocol.register("fullGame", function(data) {
			if (room < max) {
				++room;
				that.client.say(gameConnection(room));
			} else {
				console.error("server is full !");
				that.audioManager.play("../audio/none.mp3");
			}

		});
		Blockplus.Client.protocol.register("game", function(data) {

			$("#splash").hide();
			$("#control-panel").hide();
			that.boardManager.render(that.board);

			var players = [];
			for (color in that.colors) {
				players.push(color);
			}
			
			var pieces = Blockplus.GameState.prototype._decodePieces({
				0 : 8388607
			});			

			for ( var i = 0; i < data.players - 1; ++i) {
				that.pieceManager.init(players[i], pieces[0]);
				$($("#players div")[i]).addClass(players[i]);
			}

			var i = data.players - 1;			
			Blockplus.Client.protocol.register("player", function(data) {
				that.audioManager.play("../audio/in.mp3");
				that.pieceManager.init(players[i], pieces[0]);
				$($("#players div")[i]).addClass(players[i]);
				++i;
			});

			that.pieceManager.show(players[data.players - 1]);

			// TODO à déplacer
			that.controlPanelManager.register('click', that.init);

			Blockplus.Client.protocol.register("color", function(data) {

				$("#board-container").show();
				$("#pieces").show();

				that.color = data;
				that.boardManager.updateColor(that.color);
				
				Blockplus.Client.protocol.register("update", function(data) {
					
					var gameState = new Blockplus.GameState(data);
					
					if (gameState.getColor() == that.color) {
						if (!gameState.isTerminal()) {
							that.audioManager.play("../audio/some.mp3");
						}
						that.boardManager.unregister('click.1');
						that.boardManager.register('click.1', that.clickEventHandler1);
						that.pieceManager.show(that.color);
					}
					
					that.game = new Blockplus.Game(that.client, that.color, gameState, that.boardManager);
					that.game.update();
					
					for (color in that.colors) {
						that.pieceManager.update(color, gameState.getPieces(color));
					}

					var leader;
					var bestScore = 0;
					for (color in that.colors) {
						var score = that.boardManager.board.getCells(color).length;
						if (score > bestScore) {
							bestScore = score;
							leader = color;
						}
						$("#players div." + color).html(score);
					}

					if (gameState.isTerminal()) {
						that.audioManager.play("../audio/none.mp3");
						$("#board-container").css("opacity", 0.45);
						$("#players div").css("opacity", 0.33);
						$("#players div." + leader).css("opacity", 1);
						$("#players div." + leader).css("font-weight", "bold");
						that.pieceManager.show(leader);
					} else {
						// TODO on Game Ready
						$("#game").removeClass();
					}

				});
			});
		});
		that.client.say(gameConnection(room));
	});

	this.start = function() {
		that.audioManager.play("../audio/dummy.mp3");
		var url = computeLocation("/network/io");
		that.client = new Blockplus.Client("Android", url);
		that.client.start(that.client.join);
	}

	/*-------------------------------8<-------------------------------*/

	$("#content").bind('mousedown', function(event) {
		event.preventDefault();
	});

	$("#players div").bind('click', function(event) {
		var color = $(this).attr('class');
		that.pieceManager.show(color);
	});

};

Blockplus.Application.prototype = {

	constructor : Blockplus.Application,

};