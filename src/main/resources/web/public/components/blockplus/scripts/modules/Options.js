var Options = function(data) {
    this.data = [];
    this.potentialPositions = {};
    for ( var p in data) {
        var pieceInstances = data[p];
        var pieceInstancesObject = {
            id : parseInt(p.substring(5, 7), 10),
            size : pieceInstances[0].length,
            instances : []
        };
        var n = pieceInstances.length;
        for ( var i = 0; i < n; ++i) {
            var pieceInstance = pieceInstances[i];
            var size = pieceInstance.length;
            var pieceInstanceObject = {};
            for ( var j = 0; j < size; ++j) {
                var p = new Position(pieceInstance[j][0], pieceInstance[j][1]);
                var json = JSON.stringify(p); // TODO encoder les positions en index
                this.potentialPositions[json] = true;
                pieceInstanceObject[json] = true;
            }
            pieceInstancesObject["instances"].push(pieceInstanceObject);
        }
        this.data.push(pieceInstancesObject);
    }
};

Options.prototype = {

    constructor : Options,

    get : function() {
        return this.data;
    },
    
    getPotentialPositions : function() {
        return this.potentialPositions;
    },

    matches : function(selectedPositions) {
        var matches = {};
        var min = selectedPositions.getSize();
        var n = this.data.length;
        for ( var i = 0; i < n; ++i) {
            var pieceInstances = this.data[i];
            if (pieceInstances.size >= min) {
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
                        matches[pieceInstances.id] = true;
                    }
                }
            }
        }
        return matches;
    },

    perfectMatch : function(selectedPositions) {
        var min = selectedPositions.getSize();
        var n = this.data.length;
        for ( var i = 0; i < n; ++i) {
            var pieceInstances = this.data[i];
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