var BoardRendering = Class.create({
	initialize : function(cellRendering) {
		this.cellRendering = cellRendering;
	},
	getCanvas : function() {
		return this.cellRendering.getCanvas();
	},
	getContext : function(board) {
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