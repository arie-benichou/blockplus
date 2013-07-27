var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.Options = function(data) {
	this.potentialPositions = {};
	this.potentialPositionsByLight = {}
	for ( var light in data) {
		var instancesByPolyomino = data[light];
		this.potentialPositions[light] = [];
		this.potentialPositionsByLight[light] = []
		for ( var polyomino in instancesByPolyomino) {
			var instances = instancesByPolyomino[polyomino]
			var polyominos = {
				id : polyomino,
				size : instances[0].length,
				instances : []
			};
			for ( var i = 0; i < instances.length; ++i) {
				var positions = instances[i];
				var instance = {};
				for ( var j = 0; j < positions.length; ++j) {
					var position = positions[j];
					instance[position] = true;
					this.potentialPositions[light][position] = true;
				}
				polyominos.instances.push(instance);
			}
			this.potentialPositionsByLight[light].push(polyominos);
		}
	}
};

// TODO itérer une seule fois et retourner les matches
Blockplus.Options.prototype = {

	constructor : Blockplus.Options,

	get : function() {
		return this.data;
	},

	/*
	 * isLight : function(position) { return position in this.getLights(); },
	 * 
	 * getLights : function() { return this.lights; },
	 */

	isLight : function(position) {
		return position in this.getPotentialPositionsByLight();
	},

	getPotentialPositionsByLight : function() {
		return this.potentialPositionsByLight;
	},

	matchPotentialPositions : function(light, selectedPositions) {
		if(light == null) return {};
		var potentialPositions = this.potentialPositionsByLight[light];
		var matches = {};
		var min = selectedPositions.getSize();
		var n = potentialPositions.length;
		for ( var i = 0; i < n; ++i) {
			var pieceInstances = potentialPositions[i];
			if (pieceInstances.size >= min) {
				var instances = pieceInstances.instances;
				for ( var j = 0; j < instances.length; ++j) {
					var instance = instances[j];
					var match = true;
					for ( var position in selectedPositions.get()) {
						if (!(position in instance)) {
							match = false;
							break;
						}
					}
					if (match) {
						for ( var position in instance) {
							matches[position] = true;
						}
					}
				}
			}
		}
		return matches;
	},

	perfectMatch : function(selectedPositions) {
		var potentialPositions = this.potentialPositionsByLight[selectedPositions.selectedLight];
		var min = selectedPositions.getSize();
		var n = potentialPositions.length;
		for ( var i = 0; i < n; ++i) {
			var pieceInstances = potentialPositions[i];
			if (pieceInstances.size == min) {
				var instances = pieceInstances.instances;
				for ( var j = 0; j < instances.length; ++j) {
					var match = true;
					for ( var position in selectedPositions.get()) {
						if (!(position in instances[j])) {
							match = false;
							break;
						}
					}
					if (match) {
						return pieceInstances.id;
					}
				}
			}
		}
		return 0;
	}

};