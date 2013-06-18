var Blockplus = Blockplus || {};


// TODO ! déclarer le minimum expecté dans Application...
Blockplus.ViewPort = function(data) {
	this.maxWidth = data.maxWidth;
	this.maxHeight = data.maxHeight;
	this.min = Math.min(this.maxWidth, this.maxHeight);
	this.max = Math.max(this.maxWidth, this.maxHeight);	
};

Blockplus.ViewPort.prototype = {
	constructor : Blockplus.ViewPort,
};