// TODO ? pouvoir assigner plusieurs handlers
var Transport = Transport || {};
/**
 * @constructor
 */
Transport.Protocol = function() {
	this.callbacks = {};
};
Transport.Protocol.prototype = {
	constructor : Transport.Protocol,
	handle : function(message) {
		var object = null;
		try {
			object = JSON.parse(message);
		} catch (e) {
			console.error(message);
		}
		if (object != null) {
			this.on(object);
		}
	},
	on : function(json) {
		if (json.type in this.callbacks) {
			this.callbacks[json.type](json.data);
			console.log(json);
		} else {
			console.warn(json);
		}
	},
	register : function(type, callback) {
		this.callbacks[type] = callback;
	}
};