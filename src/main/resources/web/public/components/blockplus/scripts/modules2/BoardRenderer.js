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

var BoardRenderer = function(boardDimension, cellRenderer) {
	this.boardDimension = boardDimension;
	this.cellRenderer = cellRenderer;
};

BoardRenderer.prototype = {

	constructor : BoardRenderer,

	getCanvas : function() {
		return this.cellRenderer.canvas;
	},

	getContext : function(board) {
		return this.cellRenderer.context;
	},

	_updateCell : function(row, column, state) {
		this.cellRenderer._update(row, column, state);
	},

	updateCell : function(position, state) {
		this.cellRenderer.update(position, state);
	},

	update : function(board) {
		this.getContext().fillStyle = "#a0a6ab";
		this.getContext().fillRect(0, 0, this.getCanvas().width, this.getCanvas().height);
		for ( var i = 0; i < board.rows; ++i)
			for ( var j = 0; j < board.columns; ++j)
				this._updateCell(i, j, "#2a2d30");
		for ( var color in board.get()) {
			var cells = board.getCells(color);
			for ( var i = 0, n = cells.length; i < n; ++i) {
				var index = cells[i];
				var row = Math.floor(index / this.boardDimension.columns);
				var column = index % this.boardDimension.rows;
				this._updateCell(row, column, color);
			}
		}
	},

};