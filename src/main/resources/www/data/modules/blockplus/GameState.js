var Blockplus = Blockplus || {};
Blockplus.GameState = function(data) {
	this._color = data.color;
	this._pieces = this._decodePieces(data.pieces);
	this._board = data.board;
	this._options = data.options;
	this._isTerminal = data.isTerminal;
};
Blockplus.GameState.prototype = {
	constructor : Blockplus.GameState,
	_decodePieces : function(pieces) {
		var data = {};
		for ( var color in pieces) {
			var int = pieces[color];
			var digits = Math.floor(1 + Math.log(int) / Math.log(2));
			var n = digits - 2;
			var array = [];
			for ( var i = 0; i < n; ++i, int = int >> 1)
				array.push(int & 1);
			data[color] = array;
		}
		return data;
	},
	getColor : function() {
		return this._color;
	},
	getBoard : function() {
		return this._board;
	},
	getOptions : function(color) {
		return color == this._color ? this._options : {};
	},
	getPieces : function(color) {
		return this._pieces[color];
	},
	isTerminal : function() {
		return this._isTerminal;
	},
};