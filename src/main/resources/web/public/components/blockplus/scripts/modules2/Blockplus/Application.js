var Blockplus = Blockplus || {};

Blockplus.Application = function() {

	this.viewPort = new Blockplus.ViewPort({
		
		//maxWidth : $(window).width(),
		//maxHeight : $(window).height()
		
		maxWidth : 320,
		maxHeight : 480
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

	/*-----------------------8<-----------------------*/

	var that = this;

	/*-----------------------8<-----------------------*/
	this.clickEventHandler1 = function(event) {

		var targetOffset = $(event.target).offset();
		var offsetX = event.pageX - targetOffset.left;
		var offsetY = event.pageY - targetOffset.top;
		var position = that.boardManager.position(offsetX, offsetY);

		$('#controls').show();
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
			if (that.boardManager.hasSelection(position))
				that.boardManager.unselect(position);
			else
				that.boardManager.select(position, 'Blue');
		};
		that.boardManager.register('click.2', clickEventHandler2);

	};

	this.start = function() {
		$('#controls').hide();
		$('#controls').unbind('click');
		$('#controls').bind('click', that.start);
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
		Blockplus.Client.protocol.register("enterGame", function(data) {
			Blockplus.Client.protocol.register("color", function(data) {
				var color = data;
				that.boardManager.updateColor(color);
				Blockplus.Client.protocol.register("update", function(data) {
					var gameState = new Blockplus.GameState(data);
					var game = new Blockplus.Game(client, color, gameState, that.boardManager);
					game.update();
				});
			});
		});
		client.say(gameConnection(18));
	});

	var url = computeLocation("/network/io");
	var client = new Blockplus.Client("Android", url);
	client.start(client.join);

	/*-------------------------------8<-------------------------------*/

};

Blockplus.Application.prototype = {

	constructor : Blockplus.Application,

};