var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.SelectedPositions = function() {
	this.data = {};
	this.size = 0;
};
Blockplus.SelectedPositions.prototype = {
	constructor : Blockplus.SelectedPositions,
	get : function() {
		return this.data;
	},
	getSize : function() {
		return this.size;
	},
	isEmpty : function() {
		return this.size == 0;
	},
	add : function(position) {
		this.data[position] = true;
		++this.size;
	},
	remove : function(position) {
		delete this.data[position];
		--this.size;
	},
	contains : function(position) {
		return position in this.data;
	},
	clear : function(position) {
		this.data = {};
		this.size = 0;
	}
};