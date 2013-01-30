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