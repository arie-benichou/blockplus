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

var Board = function(state) {
    this.rows = state.dimension.rows;
    this.columns = state.dimension.columns;
    this.cells = state.cells;
};

Board.prototype = {

    constructor : Board,
    
    get : function() {
        return this.cells;
    },    

    getCells : function(color) {
        return this.cells[color];
    },

    indexToPosition : function(index) {
        var row = Math.floor(index / this.columns);
        var column = index % this.rows;
        return new Position(row, column);
    },

    positionToIndex : function(position) {
        return this.columns * position.row + (position.column % this.rows);
    },
    
    getOpponentColorAt : function(position) {
        var opponentColor = null;        
        var index = this.positionToIndex(position);
        for ( var color in this.get()) {
            if (jQuery.inArray(index, this.getCells(color)) != -1) {
                opponentColor = color;
                break;
            }
        }
        return opponentColor;
    },    

};