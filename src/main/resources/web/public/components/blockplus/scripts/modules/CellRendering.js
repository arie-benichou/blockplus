var CellRendering = function(canvas, offsetY, offsetX, width, length) {
    this.canvas = canvas;
    this.context = canvas.getContext("2d");
    this.offsetY = offsetY;
    this.offsetX = offsetX;
    this.width = width;
    this.length = length;
};

CellRendering.prototype = {

    constructor : CellRendering,

    getCanvas : function() {
        return this.canvas;
    },

    getContext : function() {
        return this.context;
    },

    update : function(position, color) {
        if (color in Colors) {
            this.getContext().fillStyle = Colors[color];
        } else if (color == "White") {
            this.getContext().fillStyle = "#2a2d30";
        } else {
            this.getContext().fillStyle = color;
        }
        this.getContext().fillRect(this.offsetX * position.column, this.offsetY * position.row, this.width, this.length);
    }

};