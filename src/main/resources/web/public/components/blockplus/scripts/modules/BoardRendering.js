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

    update : function(array) {
        for ( var i = 0; i < array.length; i++) {
            var row = array[i];
            for ( var j = 0; j < row.length; j++) {
                var state = array[i][j];
                var position = new Position(i, j);
                this.updateCell(position, state);
            }
        }
    },

    update2 : function(boardState) {
        var none = "#2a2d30"; // TODO add property
        var dimension = boardState.dimension;
        var rows = dimension.rows;
        var columns = dimension.columns;
        for ( var i = 0; i < rows; ++i)
            for ( var j = 0; j < columns; ++j)
                this.updateCell(new Position(i, j), none);
        var cells = boardState.cells;
        for ( var color in cells) {
            var array = cells[color];
            for ( var i = 0, n = array.length; i < n; ++i) {
                var index = array[i];
                var row = Math.floor(index/columns);
                var column = index % rows;
                this.updateCell(new Position(row, column), color);                
            }
        }
    },

    clear : function(color) {
        this.getContext().fillStyle = color;
        this.getContext().fillRect(0, 0, this.getCanvas().width, this.getCanvas().height);
    }

};