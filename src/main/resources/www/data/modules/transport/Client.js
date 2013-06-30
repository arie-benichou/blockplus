var Transport = Transport || {};
/**
 * @constructor
 */
Transport.Client = function(location, protocol) {
	this.location = location;
	this.protocol = protocol;
	this.ws = null;
	this.onopen = function(message) {
		this.protocol.on(this.wrap(message));
	};
	this.onerror = function(message) {
		this.protocol.on(this.wrap(message));
	};
	this.onclose = function(message) {
		this.protocol.on(this.wrap(message));
		this.ws = null;
	};
	this.onmessage = function(message) {
		this.protocol.handle(message.data);
	};
};
Transport.Client.prototype = {
	constructor : Transport.Client,
	start : function() {
		this.ws = new WebSocket(this.location);
		this.ws.onopen = $.proxy(this.onopen, this);
		this.ws.onmessage = $.proxy(this.onmessage, this);
		this.ws.onerror = $.proxy(this.onerror, this);
		this.ws.onclose = $.proxy(this.onclose, this);
	},
	wrap : function(message) {
		return {
			type : message.type,
			data : {
				timeStamp : message.timeStamp
			}
		};
	},
	say : function(message) {
		this.ws.send(JSON.stringify(message));
	},
	register : function(type, callback) {
		this.protocol.register(type, callback);
	}
};