var Application = function() {

	this.viewPort = new ViewPort({
		maxWidth : 320,
		maxHeight : 480
	});

	this.board = new Board({
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

	this.boardRenderer = new BoardRenderer(document.getElementById('board'), this.cellDimension, this.colors);
	this.positionFactory = new PositionFactory();
	this.boardManager = new BoardManager(this.board, this.boardRenderer, this.positionFactory);

	/*-----------------------8<-----------------------*/

	var that = this;

	/*-----------------------8<-----------------------*/
	this.clickEventHandler1 = function(event) {

		var targetOffset = $(event.target).offset();
		var offsetX = event.pageX - targetOffset.left;
		var offsetY = event.pageY - targetOffset.top;
		var position = that.boardManager.position(offsetX, offsetY);

		$('#zoom-out').show();
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
			that.boardManager.renderCell(position, "#FFF");
		};
		that.boardManager.register('click.2', clickEventHandler2);

	};

	this.start = function() {
		$('#zoom-out').hide();
		$('#zoom-out').unbind('click');
		$('#zoom-out').bind('click', that.start);
		that.boardManager.unregister('click.2');
		that.boardManager.renderer.context.restore();
		that.boardManager.render();
		that.boardManager.register('click.1', that.clickEventHandler1);
	};

};

Application.prototype = {

	constructor : Application,

};