var PotentialPositions = function(array) {

    this.data = {};
    for ( var i = 0; i < array.length; ++i) {
        var position = new Position(array[i][0], array[i][1]);
        this.data[position] = true;
    }

};

PotentialPositions.prototype = {

    constructor : PotentialPositions,

    match : function(position) {
        return position in this.data;
    }

};