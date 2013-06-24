var Blockplus = Blockplus || {};

Blockplus.PieceRenderer = function(viewPort, colors) {

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

		// TODO à revoir
		var selectedPositions = new Blockplus.SelectedPositions();
		var positions = piece.getPositions();
		for (position in positions) {
			selectedPositions.add(positions[position]);
		}

		// TODO à mettre dans Piece
		var topLeft = selectedPositions.getTopLeftPosition();
		var bottomRight = selectedPositions.getBottomRightPosition();

		var that = this;

		// TODO initialiser une fois
		var canvas = document.createElement('canvas');
		var context = canvas.getContext("2d");
		var tmpBoardRendering = new Blockplus.BoardRenderer(canvas, that.cellDimension, that.colors);

		var width = 1 + bottomRight.column - topLeft.column;
		var height = 1 + bottomRight.row - topLeft.row;

		canvas.width = that.cellDimension.width * width;
		canvas.height = that.cellDimension.height * height;

		for ( var position in selectedPositions.get()) {
			var p1 = JSON.parse(position);
			var p2 = {
				row : p1.row - topLeft.row,
				column : p1.column - topLeft.column
			}
			tmpBoardRendering.renderCell(p2, "#FFF");
			tmpBoardRendering.renderCell2(p2, this.colors[piece.getColor()], 0.42);
		}

		// ///////////////////////////////////////////////
		var canvas2 = document.createElement('canvas');
		var context2 = canvas2.getContext("2d");
		canvas2.width = (1 + 5 + 1) * that.cellDimension.width + 0;
		canvas2.height = (1 + 3 + 1) * that.cellDimension.height + 0;

		context2.fillStyle = "#2a2d30";
		context2.fillRect(0, 0, canvas2.width, canvas2.height);

		var x = (canvas2.width - canvas.width) / 2;
		var y = (canvas2.height - canvas.height) / 2;
		context2.drawImage(canvas, x, y);
		return canvas2;
	},

};