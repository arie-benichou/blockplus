var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.BoardManager = function(board, renderer, positionFactory, selectedPositions, viewPort) {
	this.renderer = renderer;
	this.positionFactory = positionFactory;
	this.selectedPositions = selectedPositions;
	this.board = board;
	this.color = "#000";
	this.potentialPositions = {};
	this.viewPort = viewPort;
	this.renderer.context.canvas.width = viewPort.min;
	this.renderer.context.canvas.height = viewPort.min;
};

Blockplus.BoardManager.prototype = {
	constructor : Blockplus.BoardManager,
	renderCell : function(position, color) {
		this.renderer.renderCell(position, color);
	},
	renderEmptyCell : function(position) {
		this.renderer.renderEmptyCell(position);
	},
	render : function(board) {
		board == undefined ? this.renderer.render(this.board) : this.renderer.render(board);
		for ( var potentialPosition in this.potentialPositions) {
			this.renderPotentialCell2(this.positionFactory.positionFromIndex(potentialPosition), this.color);
		}
	},
	renderPotentialCell : function(position, color) {
		this.renderer.renderPotentialCell(position, color);
	},
	renderPotentialCell2 : function(position, color) {
		this.renderer.renderPotentialCell2(position, color);
	},
	renderSelectedCell : function(position, color) {
		this.renderer.renderSelectedCell(position, color);
	},
	renderSelectedCells : function(selectedPotentialPositions, color) {
		for ( var selectedPotentialPosition in selectedPotentialPositions) {
			this.renderSelectedCell(this.positionFactory.positionFromIndex(selectedPotentialPosition), color);
		}
	},
	register : function(name, handler) {
		$(this.renderer.canvas).bind(name, handler);
	},
	unregister : function(name) {
		$(this.renderer.canvas).unbind(name);
	},

	positionFromOffset : function(x, y) {
		var row = Math.floor(y / (this.renderer.cellHeight));
		var column = Math.floor(x / (this.renderer.cellWidth));
		return this.positionFactory.position(row, column);
	},

	zoomInTopLeftPosition : function(position, neighbourhood) {
		var minY = position.row - neighbourhood;
		var minX = position.column - neighbourhood;
		var maxY = position.row + neighbourhood;
		var maxX = position.column + neighbourhood;
		if (maxY > (this.board.rows - 1))
			minY -= (maxY - (this.board.rows - 1));
		else if (minY < 0)
			minY = 0;
		if (maxX > (this.board.columns - 1))
			minX -= (maxX - (this.board.columns - 1));
		else if (minX < 0)
			minX = 0;
		return this.positionFactory.position(minY, minX);
	},

	clearSelection : function() {
		this.selectedPositions.clear();
	},
	selection : function() {
		return this.selectedPositions.get();
	},
	hasSelection : function(position) {
		return this.selectedPositions.contains(this.positionFactory.indexFromPosition(position));
	},
	select : function(position, color) {
		this.renderSelectedCell(position, color);
		this.selectedPositions.add(this.positionFactory.indexFromPosition(position));
	},
	unselect : function(position, color) {
		this.renderer.renderEmptyCell(position);
		this.renderPotentialCell(position, color);
		this.selectedPositions.remove(this.positionFactory.indexFromPosition(position));
	},
	updateColor : function(color) {
		this.color = color;
	},
	updateBoard : function(board) {
		this.board = new Blockplus.Board(board);
	},
	updatePotentialPositions : function(potentialPositions) {
		this.potentialPositions = potentialPositions;
	}
};