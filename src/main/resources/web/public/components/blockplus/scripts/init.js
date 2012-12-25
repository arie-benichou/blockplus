/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * Position
 */
var Position = Class.create({
	initialize : function(y, x) {
		this.row = y;
		this.column = x;
	},
	getRow : function() {
		return this.row;
	},
	getColumn : function() {
		return this.column;
	},
	toString : function() {
		return "Position{" + "row=" + this.getRow() + ", " + "column=" + this.getColumn() + "}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * CellRendering
 */
var CellRendering = Class.create({
	initialize : function(canvas, offsetY, offsetX, width, length) {
		this.canvas = $(canvas);
		this.context = this.canvas.getContext("2d");
		this.offsetY = offsetY;
		this.offsetX = offsetX;
		this.width = width;
		this.length = length;
	},
	getCanvas : function() {
		return this.canvas;
	},
	getContext : function() {
		return this.context;
	},
	update : function(position, state) {
		switch (state) {
		case "Blue":
			this.getContext().fillStyle = "blue";
			break;
		case "Yellow":
			this.getContext().fillStyle = "yellow";
			break;
		case "Green":
			this.getContext().fillStyle = "green";
			break;
		case "Red":
			this.getContext().fillStyle = "red";
			break;
		case "White":
			this.getContext().fillStyle = "gray";
			break;
		default:
			this.getContext().fillStyle = "black";
		}
		this.getContext().fillRect(this.offsetX * position.getColumn(), this.offsetY * position.getRow(), this.width, this.length);
	},
	// TODO ajouter canvas et context
	toString : function() {
		return "CellRendering" + "{" + "offsetY=" + this.offsetY + ", " + "offsetX=" + this.offsetX + ", " + "width=" + this.width + ", " + "length="
				+ this.length;
		"}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * BoardRendering
 */
var BoardRendering = Class.create({
	initialize : function(cellRendering) {
		this.cellRendering = cellRendering;
	},
	getCanvas : function() {
		return this.cellRendering.getCanvas();
	},
	getContext : function() {
		return this.cellRendering.getContext();
	},
	updateCell : function(position, state) {
		this.cellRendering.update(position, state);
	},
	update : function(board) {
		for ( var i = 0; i < board.length; i++) {
			var row = board[i];
			for ( var j = 0; j < row.length; j++) {
				var state = board[i][j];
				var position = new Position(i, j);
				this.updateCell(position, state);
			}
		}
	},
	clear : function() {
		this.getContext().clearRect(0, 0, this.getCanvas().width, this.getCanvas().height);
	},
	toString : function() {
		return "BoardRendering{" + "cellRendering=" + this.cellRendering + "}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
var boardRendering = new BoardRendering(new CellRendering("piece", 12, 12, 11, 11));
/*--------------------------------------------------8<--------------------------------------------------*/
new Ajax.Request("/pieces.xml", {onSuccess: function(response) {
	var data = response.responseXML;
	var pieces = data.evaluate("//piece", data, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null );
	for ( var i=0 ; i < pieces.snapshotLength; ++i ) {
		var piece = pieces.snapshotItem(i);
		var positions = document.evaluate("position", piece, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null );
		for ( var j=0 ; j < positions.snapshotLength; ++j ) {
			var position = positions.snapshotItem(j);
			var y = document.evaluate("y", position, null, XPathResult.NUMBER_TYPE, null );
			var x = document.evaluate("x", position, null, XPathResult.NUMBER_TYPE, null );
			boardRendering.updateCell(new Position(y.numberValue, x.numberValue), "Blue");
		}
		var imageDataURL = $("piece").toDataURL("image/png");
		localStorage.setItem("piece" + (i + 1), imageDataURL);
		var retrievedObject = localStorage.getItem("piece" + (i + 1));
		var image = new Image();
		image.src = retrievedObject;
		$("available-pieces").appendChild(image);
		boardRendering.clear("piece");
	}
	window.location="game.html";
}});
/*--------------------------------------------------8<--------------------------------------------------*/