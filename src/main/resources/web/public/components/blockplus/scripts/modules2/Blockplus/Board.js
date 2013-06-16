var Blockplus = Blockplus || {};

Blockplus.Board = function(data) {
	this.dimension = data.dimension;
	this.rows = this.dimension.rows;
	this.columns = this.dimension.columns;
	this.size = this.columns * this.rows;
	this.cells = data.cells;
};
Blockplus.Board.prototype = {
	constructor : Blockplus.Board,
	getCells : function(color) {
		return this.cells[color];
	}
};