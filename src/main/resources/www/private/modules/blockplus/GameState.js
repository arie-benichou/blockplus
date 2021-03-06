var Blockplus = Blockplus || {};
/**
 * @constructor
 */
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
			var code = pieces[color];
			var digits = Math.floor(1 + Math.log(code) / Math.log(2));
			var n = digits - 1;
			var array = [];
			for ( var i = 0; i < n; ++i, code = code >> 1) {
				array.push(code & 1);
			}
			data[color] = array.reverse();
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
	}
};