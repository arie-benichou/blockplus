var OffsetToPositionBuilder = function(offsetX, offsetY) {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
};

OffsetToPositionBuilder.prototype = {

    constructor : OffsetToPositionBuilder,

    getOffsetX : function() {
        return this.offsetX;
    },

    getOffsetY : function() {
        return this.offsetY;
    },

    build : function(x, y) {
        var row = Math.floor(y / this.offsetY);
        var column = Math.floor(x / this.offsetX);
        return new Position(row, column);
    }

};