var Blockplus = Blockplus || {};

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
        this.data[JSON.stringify(position)] = true;
        ++this.size;
    },

    remove : function(position) {
        delete this.data[JSON.stringify(position)];
        --this.size;
    },

    contains : function(position) {
        return (JSON.stringify(position) in this.data);
    },

    clear : function(position) {
        this.data = {};
        this.size = 0;
    },

    getTopLeftPosition : function() {
        var top = Infinity;
        var left = Infinity;
        for ( var entry in this.get()) {
            var position = JSON.parse(entry);
            var y = position.row;
            var x = position.column;
            if (y < top)
                top = y;
            if (x < left)
                left = x;
        }
        return new Position(top, left);
    },

    getBottomRightPosition : function() {
        var bottom = -Infinity;
        var right = -Infinity;
        for ( var entry in this.get()) {
            var position = JSON.parse(entry);
            var y = position.row;
            var x = position.column;
            if (y > bottom)
                bottom = y;
            if (x > right)
                right = x;
        }
        return new Position(bottom, right);
    }

};