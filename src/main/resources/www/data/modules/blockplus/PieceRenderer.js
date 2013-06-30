var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.PieceRenderer = function(viewPort, colors, positionFactory) {

	this.positionFactory = positionFactory;
	this.viewPort = viewPort;
	this.colors = colors;

	// TODO déterminer si Portrait ou Landscape
	this.width = this.viewPort.min
	this.height = this.viewPort.max - this.viewPort.min;

	var min = Math.min(this.width, this.height);

	var playersDivHeight = $("#players div").outerHeight();

	this.cellDimension = {
		width : ((this.width - 7 * (2 * 1 + 2 * 2) - 2 * 2) / ((1 + 5 + 1) * 7)),
		height : (((this.height - (2 + playersDivHeight + 2 * 3) - 3 * (2 * 1 + 2 * 2)) / ((1 + 3 + 1) * 3)))
	};

	$("#pieces div").css("width", 7 * (Math.floor(7 * this.cellDimension.width) + 2 * 1 + 2 * 2));

};

Blockplus.PieceRenderer.prototype = {

	constructor : Blockplus.PieceRenderer,

	render : function(piece) {

		var positions = piece.getPositions();
		var topLeft = this.positionFactory.getTopLeftPosition(positions);
		var bottomRight = this.positionFactory.getBottomRightPosition(positions);

		// TODO initialiser une fois
		var canvas = document.createElement('canvas');
		var context = canvas.getContext("2d");
		var tmpBoardRendering = new Blockplus.BoardRenderer(canvas, this.cellDimension, this.colors);

		var width = 1 + bottomRight.column - topLeft.column;
		var height = 1 + bottomRight.row - topLeft.row;

		canvas.width = this.cellDimension.width * width;
		canvas.height = this.cellDimension.height * height;

		for ( var i in positions) {
			var p1 = positions[i];
			var p2 = this.positionFactory.position(p1.row - topLeft.row, p1.column - topLeft.column);
			tmpBoardRendering.renderCell(p2, "#FFF");
			tmpBoardRendering.renderCell2(p2, this.colors[piece.getColor()], 0.42);
		}

		// ///////////////////////////////////////////////
		var canvas2 = document.createElement('canvas');
		var context2 = canvas2.getContext("2d");
		canvas2.width = (1 + 5 + 1) * this.cellDimension.width + 0;
		canvas2.height = (1 + 3 + 1) * this.cellDimension.height + 0;

		context2.fillStyle = "#2a2d30";
		context2.fillRect(0, 0, canvas2.width, canvas2.height);

		var x = (canvas2.width - canvas.width) / 2;
		var y = (canvas2.height - canvas.height) / 2;
		context2.drawImage(canvas, x, y);
		return canvas2;
	}

};