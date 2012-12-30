var OffsetToPositionBuilder = Class.create({
	initialize : function(offsetX, offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	},
	getOffsetX : function() {
		return this.offsetX;
	},
	getOffsetY : function() {
		return this.offsetY;
	},
	build : function(x, y) {
		var row = Math.floor(y / this.offsetY);
		var column = Math.floor(x / this.offsetX);
		newPosition = new Position(row, column);
		console.log(newPosition.toString());
		return newPosition;

	},
	toString : function() {
		return "OffsetToPositionBuilder{" + "offsetX=" + this.getOffsetX() + ", " + "offsetY=" + this.getOffsetY() + "}";
	},
});