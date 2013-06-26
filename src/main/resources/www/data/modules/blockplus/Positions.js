var Blockplus = Blockplus || {};
Blockplus.Positions = function(rows, columns) {
	this.rows = rows;
	this.columns = columns;
};
Blockplus.Positions.prototype = {

	constructor : Blockplus.Positions,

	position : function(row, column) {
		return {
			row : row,
			column : column
		};
	},

	positionFromIndex : function(index) {
		return this.position(Math.floor(index / this.rows), index % this.columns);
	},

	indexFromPosition : function(position) {
		return this.columns * position.row + position.column % this.rows;
	},

	getTopLeftPosition : function(positions) {
		var top = Infinity;
		var left = Infinity;
		for ( var i in positions) {
			var position = positions[i];
			var y = position.row;
			var x = position.column;
			if (y < top)
				top = y;
			if (x < left)
				left = x;
		}
		return this.position(top, left);
	},

	getBottomRightPosition : function(positions) {
		var bottom = -Infinity;
		var right = -Infinity;
		for ( var i in positions) {
			var position = positions[i];
			var y = position.row;
			var x = position.column;
			if (y > bottom)
				bottom = y;
			if (x > right)
				right = x;
		}
		return this.position(bottom, right);
	}

};