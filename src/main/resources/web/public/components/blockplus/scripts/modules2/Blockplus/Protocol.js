var Blockplus = Blockplus || {};

Blockplus.Protocol(debug) = {
	this.debug = debug;
	this.listeners = {};
}
Blockplus.Protocol.prototype = {

	constructor : Blockplus.Protocol,

	handle : function(message) {
		var object = null;
		try {
			object = JSON.parse(message);
		} catch (e) {
			console.error(message);
		}
		if (object != null)
			this.on(object);
	},

	on : function(json) {
		if (json.type in this.listeners)
			this.listeners[json.type](json.data);
		else if (this.debug) {
			console.error(json);
			console.error("Protocol has no listener defined for event of type: " + json.type);
			console.error(json.data);
		}

	},

	register : function(type, listener) {
		this.listeners[type] = listener;
	}

};