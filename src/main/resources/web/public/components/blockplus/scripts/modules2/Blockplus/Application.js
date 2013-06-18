var Blockplus = Blockplus || {};

// TODO rendre optional le zoom
// TODO le premier tap doit juste zoomer
// TODO fournir un moyen de sortir du zoom (lors d'un zoom sur une region non
// jouable)
// TODO afficher les pieces restantes
Blockplus.Application = function() {

	this.viewPort = new Blockplus.ViewPort({

		maxWidth : $(window).width(),
		maxHeight : $(window).height(),

	// maxWidth : 320,
	// maxHeight : 480-32

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

	/*-----------------------8<-----------------------*/

	var that = this;

	/*-----------------------8<-----------------------*/

	// TODO refactor to observer pattern...
	this.clickEventHandler1 = function(event) {

		var targetOffset = $(event.target).offset();
		var offsetX = event.pageX - targetOffset.left;
		var offsetY = event.pageY - targetOffset.top;
		var position = that.boardManager.position(offsetX, offsetY);

		var isPotentialPosition = JSON.stringify(position) in that.game.options.getPotentialPositions();
		if (!isPotentialPosition)
			return;

		var p1 = position;

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

		that.boardManager.select(p1, 'Blue');
		that.controlPanelManager.handleSelection(that.boardManager.selectedPositions, that.game.options.perfectMatch(that.boardManager.selectedPositions));

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
				else
					that.boardManager.select(position, 'Blue');
				if (that.boardManager.selectedPositions.isEmpty()) {
					that.start();
				} else {
					that.controlPanelManager.handleSelection(that.boardManager.selectedPositions, that.game.options
							.perfectMatch(that.boardManager.selectedPositions));
				}
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

	this.start = function() {
		if (!that.boardManager.selectedPositions.isEmpty()) {
			that.moveSubmitHandler();
		}
		that.controlPanelManager.hide();
		that.controlPanelManager.unregister('click');
		that.controlPanelManager.register('click', that.start);
		that.boardManager.unregister('click.2');
		that.boardManager.renderer.context.restore();
		that.boardManager.render();
		that.boardManager.register('click.1', that.clickEventHandler1);
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
			}
			else {
				alert("server is full !");
			}

		});

		Blockplus.Client.protocol.register("enterGame", function(data) {
			Blockplus.Client.protocol.register("color", function(data) {
				var color = data;
				that.boardManager.updateColor(color);
				Blockplus.Client.protocol.register("update", function(data) {
					var gameState = new Blockplus.GameState(data);
					that.game = new Blockplus.Game(that.client, color, gameState, that.boardManager);
					that.game.update();
				});
			});
		});

		that.client.say(gameConnection(room));
	});

	var url = computeLocation("/network/io");
	this.client = new Blockplus.Client("Android", url);
	this.client.start(this.client.join);

	/*-------------------------------8<-------------------------------*/

};

Blockplus.Application.prototype = {

	constructor : Blockplus.Application,

};