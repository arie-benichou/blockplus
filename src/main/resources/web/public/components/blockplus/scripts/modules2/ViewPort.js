var ViewPort = function(data) {
	this.maxWidth = data.maxWidth;
	this.maxHeight = data.maxHeight;
	this.min = Math.min(this.maxWidth, this.maxHeight);
};

ViewPort.prototype = {
	constructor : ViewPort,
};