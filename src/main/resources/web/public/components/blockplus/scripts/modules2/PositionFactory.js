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

var PositionFactory = function(cellDimension) {
	this.cellDimension = cellDimension;
};

PositionFactory.prototype = {

	constructor : PositionFactory,

	getPositionFromOffset : function(x, y) {
		var row = Math.floor(y / (this.cellDimension.height));
		var column = Math.floor(x / (this.cellDimension.width));
		return this.getPosition(row, column);
	},

	getPosition : function(row, column) {
		return {
			row : row,
			column : column
		};
	}

};