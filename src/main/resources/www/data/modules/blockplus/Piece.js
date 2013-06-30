var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.Piece = function(positions, color) {
	this.positions = positions;
	this.color = color;
};
Blockplus.Piece.prototype = {
	constructor : Blockplus.Piece,
	getPositions : function() {
		return this.positions;
	},
	getColor : function() {
		return this.color;
	}
};