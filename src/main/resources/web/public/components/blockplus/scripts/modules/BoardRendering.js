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

var BoardRendering = function(cellRendering) {
    this.cellRendering = cellRendering;
};

BoardRendering.prototype = {

    constructor : BoardRendering,

    getCanvas : function() {
        return this.cellRendering.getCanvas();
    },

    getContext : function(board) {
        return this.cellRendering.getContext();
    },

    updateCell : function(position, state) {
        this.cellRendering.update(position, state);
    },

    init : function(board) {
        var none = "#2a2d30";
        this.clear("#a0a6ab");
        // TODO save initial canvas context
        for ( var i = 0; i < board.rows; ++i)
            for ( var j = 0; j < board.columns; ++j)
                this.updateCell(new Position(i, j), none);
    },

    update : function(board) {
        this.init(board);
        // TODO precompute positions in order to avoid flickering
        for ( var color in board.get()) {
            var cells = board.getCells(color);
            for ( var i = 0, n = cells.length; i < n; ++i)
                this.updateCell(board.indexToPosition(cells[i]), color);
        }
    },

    clear : function(color) {
        this.getContext().fillStyle = color;
        this.getContext().fillRect(0, 0, this.getCanvas().width, this.getCanvas().height);
    },
    
    
    showPotentialCell : function(position, color) {//Colors[this.currentColor]
    	var size = 16;
        var context = this.getContext();
        context.globalAlpha = 0.4;
        context.fillStyle = color;
        context.beginPath();
        context.arc(size * position.column + size / 2, size * position.row + size / 2, 5, 0, Math.PI * 2, true);
        context.closePath();
        context.fill();
        context.globalAlpha = 0.8;
        context.lineWidth = 2;
        context.strokeStyle = color;
        context.stroke();
        context.globalAlpha = 1;
    },
        
    showSelectedPotentialCell : function(position, currentColor) { //Colors[this.currentColor]
            this.getContext().globalAlpha = 0.5;
            this.updateCell(position, currentColor); // TODO pouvoir passer alpha
            this.getContext().globalAlpha = 1;
    },            
    
    showSelectedPotentialCells : function(selectedPotentialPositions, currentColor) {
        for ( var selectedPotentialPosition in selectedPotentialPositions) {
            // TODO à revoir
            var position = JSON.parse(selectedPotentialPosition);
            this.showSelectedPotentialCell(position, currentColor);
        }
    },    

    // TODO à revoir
    showOpponentCells : function(board, currentColor, opponentColor) {
        var cells = board.getCells(opponentColor);
        for ( var i = 0, n = cells.length; i < n; ++i) {
            var position = board.indexToPosition(cells[i]);
            this.getContext().fillStyle = Colors[opponentColor];
            this.getContext().fillRect(this.cellRendering.offsetX * position.column, this.cellRendering.offsetY * position.row, this.cellRendering.offsetX,
                    this.cellRendering.offsetY);
        }
    },
    
    
};