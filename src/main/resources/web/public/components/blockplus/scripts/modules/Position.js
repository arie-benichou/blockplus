var Position = Class.create({
	initialize : function(y, x) {
		this.row = y;
		this.column = x;
	},
	getRow : function() {
		return this.row;
	},
	getColumn : function() {
		return this.column;
	},
	toString : function() {
		return "Position{" + "row=" + this.getRow() + ", " + "column=" + this.getColumn() + "}";
	},
});