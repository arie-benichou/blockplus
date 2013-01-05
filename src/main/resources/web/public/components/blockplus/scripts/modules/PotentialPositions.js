var PotentialPositions = Class.create({
    initialize : function(array) {
        this.data = {};
        for ( var i = 0; i < array.length; ++i) {
            var position = new Position(array[i][0], array[i][1]);
            this.data[position] = true;
        }
        
    },
    match : function(position) {
        return position in this.data; 
    }
});