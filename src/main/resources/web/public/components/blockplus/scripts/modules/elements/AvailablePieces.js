// TODO injecter le PieceManager
var AvailablePieces = function(id) {
    this.element = document.getElementById(id);
};

AvailablePieces.prototype = {

    constructor : AvailablePieces,

    getElement : function() {
        return this.element;
    },

    clear : function() {
        $(this.getElement()).html('');
    },

    show : function() {
        $(this.getElement()).show();
    },

    hide : function() {
        $(this.getElement()).hide();
    },

    update : function(color, availablePieces) {
        for ( var i = 1; i <= 21; ++i) { // TODO à revoir
            var key = getLocalStoreKey(color, "piece" + i);
            var retrievedObject = localStorage.getItem(key);
            var image = new Image();
            image.setAttribute("id", "piece-" + i);
            image.src = retrievedObject;
            image.setAttribute("class", "not-available");
            this.getElement().appendChild(image);
        }
        for ( var i = 0, n = availablePieces.length; i < n; ++i) {
            var x = availablePieces[i];
            if (x != 0) { // TODO à revoir
                document.getElementById("piece-" + x).setAttribute("class", "available");
            }
        }
    }

};