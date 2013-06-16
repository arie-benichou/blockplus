var ViewPort = function(data) {
	this.minWidth = data.minWidth;
	this.minHeight = data.minHeight;
	this.min = Math.min(this.minWidth, this.minHeight);
};

ViewPort.prototype = {
	constructor : ViewPort,
};