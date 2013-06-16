var Blockplus = Blockplus || {};

Blockplus.PositionFactory = function() {
};

Blockplus.PositionFactory.prototype = {

	constructor : Blockplus.PositionFactory,

	getPosition : function(row, column) {
		return {
			row : row,
			column : column
		};
	}

};