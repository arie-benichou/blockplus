var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.Messages = function() {
};
Blockplus.Messages.prototype = {
	constructor : Blockplus.ViewPort,
	connection : function(name) {
		return {
			type : 'Client',
			data : {
				name : name
			}
		};
	},
	disconnect : function() {
		return {
			type : 'Disconnect',
			data : {}
		};
	},	
	gameConnection : function(n) {
		return {
			type : 'GameConnection',
			data : {
				ordinal : n
			}
		};
	},
	virtualPlayer : function(n) {
		return {
			type : 'VirtualPlayerConnection',
			data : {
				ordinal : n
			}
		};
	},
	moveSubmit : function(id, positions) {
		return {
			type : 'MoveSubmit',
			data : {
				id : id,
				positions : positions
			}
		};
	}
};