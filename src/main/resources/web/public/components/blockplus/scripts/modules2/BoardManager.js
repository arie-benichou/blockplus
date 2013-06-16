/*
 * Copyright 2012-2013 ArteFact
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

var BoardManager = function(board, renderer, positionFactory) {

	this.board = board;
	this.renderer = renderer;
	this.positionFactory = positionFactory;

	// TODO ? à injecter
	$(this.renderer.canvas).mousedown(function(event) {
		event.preventDefault();
	});

	var that = this;

};

BoardManager.prototype = {

	constructor : BoardManager,

	renderCell : function(position, color) {
		this.renderer.renderCell(position, color);
	},

	render : function(board) {
		board == undefined ? this.renderer.render(this.board) : this.renderer.render(board);
	},

	renderPotentialCell : function(position, color) {
		this.renderer.renderPotentialCell(position, color);
	},

	renderSelectedCell : function(position, color) {
		this.renderer.renderSelectedCell(position, color);
	},

	renderSelectedCells : function(selectedPotentialPositions, color) {
		for ( var selectedPotentialPosition in selectedPotentialPositions) {
			var position = JSON.parse(selectedPotentialPosition);
			this.renderSelectedCell(position, color);
		}
	},
	
	register : function(name, handler) {
		$(this.renderer.canvas).bind(name, handler);
	},

	unregister : function(name) {
		$(this.renderer.canvas).unbind(name);
	},

	position : function(x, y) {
		var row = Math.floor(y / (this.renderer.cellHeight));
		var column = Math.floor(x / (this.renderer.cellWidth));
		return this.positionFactory.getPosition(row, column);
	},
	
	zoomInTopLeftCornerPosition : function(position, neighbourhood) {
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
		return {
			minX : minX,
			minY : minY
		};
	},		

};