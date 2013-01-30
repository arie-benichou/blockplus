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

    showOpponentCells1 : function(board, currentColor, opponentColor) {
        for ( var color in board.get()) {
            if (color != currentColor && color != opponentColor) {
                var cells = board.getCells(color);
                for ( var i = 0, n = cells.length; i < n; ++i) {
                    this.getContext().fillStyle = Colors[color];
                    var position = board.indexToPosition(cells[i]);
                    this.getContext().fillRect(this.cellRendering.offsetX * position.column, this.cellRendering.offsetY * position.row,
                            this.cellRendering.offsetX, this.cellRendering.offsetY);
                    this.getContext().fillStyle = "#2a2d30";
                    this.getContext().globalAlpha = 0.33;
                    this.getContext().fillRect(this.cellRendering.offsetX * position.column, this.cellRendering.offsetY * position.row,
                            this.cellRendering.offsetX, this.cellRendering.offsetY);
                    this.getContext().globalAlpha = 1;
                }
            }
        }
    },

    // TODO à revoir
    showOpponentCells : function(board, currentColor, opponentColor) {
        var cells = board.getCells(opponentColor);
        // console.log(cells);
        for ( var i = 0, n = cells.length; i < n; ++i) {
            var position = board.indexToPosition(cells[i]);
            // console.log(position);
            this.getContext().fillStyle = Colors[opponentColor];
            this.getContext().fillRect(this.cellRendering.offsetX * position.column, this.cellRendering.offsetY * position.row, this.cellRendering.offsetX,
                    this.cellRendering.offsetY);
        }
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
    }

};