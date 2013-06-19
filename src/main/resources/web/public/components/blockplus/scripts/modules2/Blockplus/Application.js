var Blockplus = Blockplus || {};

// TODO afficher les pieces restantes
// TODO proposer une pièce lorsque le matching retourne une seule piece
// TODO rendre optional le zoom
Blockplus.Application = function() {

	this.viewPort = new Blockplus.ViewPort({

		// maxWidth : $(window).width(),
		// maxHeight : $(window).height(),

		maxWidth :  $("#content").css("width").substr(0,3),
		maxHeight : $("#content").css("height").substr(0,3)

	});

	this.board = new Blockplus.Board({
		dimension : {
			rows : 20,
			columns : 20
		},
		cells : {
			Blue : [ 0, 21, 42, 63, 84, 105, 126, 147, 168, 189 ],
			Yellow : [ 19, 38, 57, 76, 95, 114, 133, 152, 171, 190 ],
			Red : [ 380, 361, 342, 323, 304, 285, 266, 247, 228, 209 ],
			Green : [ 399, 378, 357, 336, 315, 294, 273, 252, 231, 210 ]
		}
	});

	this.cellDimension = {
		width : this.viewPort.min / this.board.rows,
		height : this.viewPort.min / this.board.columns
	};

	this.colors = {
		Blue : "#3971c4",
		Yellow : "#eea435",
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

	this.boardRenderer = new Blockplus.BoardRenderer(document.getElementById('board'), this.cellDimension, this.colors);
	this.positionFactory = new Blockplus.PositionFactory();
	this.selectedPositions = new Blockplus.SelectedPositions();
	this.boardManager = new Blockplus.BoardManager(this.board, this.boardRenderer, this.positionFactory, this.selectedPositions, this.viewPort);
	this.controlPanelManager = new Blockplus.ControlPanelManager(document.getElementById('control-panel'), this.viewPort);
	var pieceRenderer = new Blockplus.PieceRenderer(this.viewPort, this.colors);

	/*-----------------------8<-----------------------*/

	var that = this;

	/*-----------------------8<-----------------------*/

	var callBack = function() {
		var data = [];
		for ( var color in that.colors) {
			data.push(that.pieceManager.piece(color, 21));
		}
		data.reverse();
		console.debug(data);
		// TODO request animation frame
		var id = window.setInterval(function() {
			if (data.length > 0) {
				var img = document.createElement('img');
				img.id = data.length;
				$("#splash").append(img);
				img.src = data.pop();
			} else {
				window.clearInterval(id);
				var data2 = [];
				for ( var color in that.colors) {
					data2.push(that.pieceManager.piece(color, 21));
					data2.push(that.pieceManager.piece(color, 8));
					data2.push(that.pieceManager.piece(color, 1));
				}
				// data2.reverse();
				var n = 2;
				var t = 3;
				var f = function() {
					var id2 = window.setInterval(function() {
						console.debug(n);
						$("#4").attr('src', data2[n % t]);
						$("#3").attr('src', data2[n % t + t]);
						$("#2").attr('src', data2[n % t + t * 2]);
						$("#1").attr('src', data2[n % t + t * 3]);
						++n;
						if ($("#splash").attr("style") == "display: none;") {
							window.clearInterval(id2);
						}
					}, 150);
				}
				window.setTimeout(f, 150);
			}

		}, 150);

	};

	this.pieceManager = new Blockplus.PieceManager(document.getElementById('pieces'), pieceRenderer, "/xml/pieces.xml", this.positionFactory, callBack);

	/*-----------------------8<-----------------------*/

	/*-----------------------8<-----------------------*/

	// TODO refactor to observer pattern...
	this.clickEventHandler1 = function(event) {

		var targetOffset = $(event.target).offset();
		var offsetX = event.pageX - targetOffset.left;
		var offsetY = event.pageY - targetOffset.top;
		var position = that.boardManager.position(offsetX, offsetY);

		that.boardManager.unregister('click.1');

		var referential = that.boardManager.zoomInTopLeftCornerPosition(position, that.neighbourhood);
		var translation = {
			x : -referential.minX * that.cellDimension.width * that.scale.x,
			y : -referential.minY * that.cellDimension.height * that.scale.y
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
					that.boardManager.unselect(position, 'Blue');
				else if (JSON.stringify(position) in that.game.options.matchPotentialPositions(that.boardManager.selectedPositions)) {
					that.boardManager.select(position, 'Blue');
				}
				if (that.boardManager.selectedPositions.isEmpty()) {
					that.init();
				} else {
					that.controlPanelManager.handle(that.game.options, that.boardManager.selectedPositions, that.boardManager, 'Blue');
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
			}

		});
		Blockplus.Client.protocol.register("enterGame", function(data) {

			that.controlPanelManager.register('click', that.init);

			Blockplus.Client.protocol.register("color", function(data) {
				that.color = data;
				that.boardManager.updateColor(that.color);
				Blockplus.Client.protocol.register("update", function(data) {
					$("#splash").hide();
					window.clearInterval();
					that.controlPanelManager.hide();
					var gameState = new Blockplus.GameState(data);
					if (gameState.getColor() == that.color) {
						that.boardManager.unregister('click.1');
						that.boardManager.register('click.1', that.clickEventHandler1);
					}
					that.game = new Blockplus.Game(that.client, that.color, gameState, that.boardManager);
					that.game.update();
					var pieces = gameState.getPieces(that.color);
					console.log(pieces);
					that.pieceManager.update(that.color, pieces);
				});
			});
		});
		that.client.say(gameConnection(room));
	});

	this.start = function() {
		var url = computeLocation("/network/io");
		that.client = new Blockplus.Client("Android", url);
		that.client.start(that.client.join);
	}

	/*-------------------------------8<-------------------------------*/

};

Blockplus.Application.prototype = {

	constructor : Blockplus.Application,

};