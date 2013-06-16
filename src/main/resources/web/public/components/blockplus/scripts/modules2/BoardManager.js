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

	register : function(name, handler) {
		$(this.renderer.canvas).bind(name, handler);
	},

	unregister : function(name) {
		$(this.renderer.canvas).unbind(name);
	},
	
	renderCell: function(position, color) {
		this.renderer.renderCell(position, color);
	},	

	render: function(board) {
		board == undefined ? this.renderer.render(this.board): this.renderer.render(board);
	}	

//	getPositionFromOffset : function(x, y) {
//		var row = Math.floor(y / (this.cellDimension.height));
//		var column = Math.floor(x / (this.cellDimension.width));
//		return this.getPosition(row, column);
//	},
//	
//	showPotentialCell : function(position, color) {
//		var size = 16;
//		var context = this.renderer.context;
//		context.globalAlpha = 0.4;
//		context.fillStyle = color;
//		context.beginPath();
//		context.arc(size * position.column + size / 2, size * position.row + size / 2, 5, 0, Math.PI * 2, true);
//		context.closePath();
//		context.fill();
//		context.globalAlpha = 0.8;
//		context.lineWidth = 2;
//		context.strokeStyle = color;
//		context.stroke();
//		context.globalAlpha = 1;
//	},
//
//	showSelectedPotentialCell : function(position, currentColor) {
//		var context = this.renderer.context;
//		context.globalAlpha = 0.5;
//		this.updateCell(position, currentColor); // TODO pouvoir passer alpha
//		context.globalAlpha = 1;
//	},
//
//	showSelectedPotentialCells : function(selectedPotentialPositions, currentColor) {
//		for ( var selectedPotentialPosition in selectedPotentialPositions) {
//			// TODO à revoir
//			var position = JSON.parse(selectedPotentialPosition);
//			this.showSelectedPotentialCell(position, currentColor);
//		}
//	},

};