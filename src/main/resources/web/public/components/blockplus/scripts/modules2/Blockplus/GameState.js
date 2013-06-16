var Blockplus = Blockplus || {};

Blockplus.GameState = function(data) {
    this._color = data.color;
    this._pieces = data.pieces;    
    this._board = new Board(data.board);
    this._options = data.options;
    this._isTerminal = data.isTerminal;
};

Blockplus.GameState.prototype = {

    constructor : Blockplus.GameState,
    
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