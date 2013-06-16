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

var CellRenderer = function(canvas, positionFactory, colors) {
	this.canvas = canvas;
	this.context = canvas.getContext("2d");
	this.positionFactory = positionFactory;
	this.colors = colors;
	this.width = this.positionFactory.cellDimension.width;
	this.height = this.positionFactory.cellDimension.height;
};

CellRenderer.prototype = {

	constructor : CellRenderer,

	_update : function(row, column, color) {
		this.context.fillStyle = color;
		this.context.fillRect(this.width * column, this.height * row, this.width - 1, this.height - 1);
	},

	update : function(position, color) {
		this._update(position.row, position.column, (color in this.colors) ? this.colors[color] : color);
	}

};