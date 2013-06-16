var Board = function(data) {
	this.dimension = data.dimension;
	this.rows = this.dimension.rows;
	this.columns = this.dimension.columns;
	this.size = this.columns * this.rows;
	this.cells = data.cells;
};

Board.prototype = {

	constructor : Board,

	getCells : function(color) {
		return this.cells[color];
	}

};