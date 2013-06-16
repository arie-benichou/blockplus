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

var BoardRenderer = function(canvas, cellDimension, colors) {
	this.canvas = canvas;
	this.context = canvas.getContext("2d");
	this.cellWidth = cellDimension.width;
	this.cellHeight = cellDimension.height;
	this.colors = colors;
};

BoardRenderer.prototype = {

	constructor : BoardRenderer,

	_setFillStyle : function(style) {
		this.context.fillStyle = style;
	},

	_fillRect : function(x, y, width, height) {
		this.context.fillRect(x, y, width, height);
	},

	_drawCell : function(row, column) {
		this._fillRect(this.cellWidth * column, this.cellHeight * row, this.cellWidth - 1, this.cellHeight - 1);
	},

	renderCell : function(position, color) {
		this._setFillStyle(color);
		this._drawCell(position.row, position.column);
	},

	render : function(board) {
		this._setFillStyle("#a0a6ab");
		this._fillRect(0, 0, this.canvas.width, this.canvas.height);
		this._setFillStyle("#2a2d30");
		for ( var i = 0; i < board.rows; ++i)
			for ( var j = 0; j < board.columns; ++j)
				this._drawCell(i, j);
		for ( var color in board.cells) {
			var cells = board.getCells(color);
			this._setFillStyle(this.colors[color]);
			for ( var i = 0, n = cells.length; i < n; ++i) {
				var index = cells[i];
				this._drawCell(Math.floor(index / board.columns), index % board.rows);
			}
		}
	},

	renderPotentialCell : function(position, color) {
		this.context.save();
		this.context.globalAlpha = 0.4;
		this.context.fillStyle = this.colors[color];
		this.context.beginPath();
		var x = this.cellWidth * position.column + this.cellWidth / 2 - 1;
		var y = this.cellHeight * position.row + this.cellHeight / 2 - 1;
		var r = (((this.cellWidth + this.cellHeight) / 2) / 2) - 2*2;
		this.context.arc(x, y, r-(r/4), 0, Math.PI * 2, true);
		this.context.closePath();
		this.context.fill();
		this.context.globalAlpha = 0.8;
		this.context.lineWidth = 2;
		this.context.strokeStyle = this.colors[color];
		this.context.stroke();
		this.context.restore();
	},
	
	renderSelectedCell : function(position, color) {
		this.context.save();
		this.context.globalAlpha = 0.5;
		this.updateCell(position, this.colors[color]);
		this.context.restore();
	},

};