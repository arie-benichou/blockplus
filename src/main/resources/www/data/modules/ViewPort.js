//var Blockplus = Blockplus || {};
ViewPort = function(data) {
	this.maxWidth = data.maxWidth;
	this.maxHeight = data.maxHeight;
	this.min = Math.min(this.maxWidth, this.maxHeight);
	this.max = Math.max(this.maxWidth, this.maxHeight);	
};
ViewPort.prototype = {
	constructor : ViewPort,
};
//TODO ! déclarer le minimum expecté dans Application...