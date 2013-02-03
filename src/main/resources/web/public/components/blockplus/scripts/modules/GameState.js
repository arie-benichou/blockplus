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

var GameState = function(data) {
    this._color = data.color;
    this._pieces = data.pieces;    
    this._board = new Board(data.board);
    this._options = data.options;
    this._isTerminal = data.isTerminal;
};

GameState.prototype = {

    constructor : GameState,
    
    getColor: function() {
        return this._color;
    },    
    
    getBoard: function() {
        return this._board;
    },
    
    getOptions: function(color) {
        return color == this._color ? this._options : {};
    },
    
    getPieces: function(color) {
        return this._pieces[color];
    },
    
    isTerminal: function() {
        return this._isTerminal;
    }, 
    
};