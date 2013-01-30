// TODO injecter le PieceManager
var RemainingPieces = function(id) {
    this.element = document.getElementById(id);
};

RemainingPieces.prototype = {

    constructor : RemainingPieces,

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

    update : function(color, remainingPieces) {
        for ( var i = 0, n = remainingPieces.length; i < n; ++i) {
            // TODO à revoir
            if (remainingPieces[i] != 0) {
                // TODO à revoir
                var key = getLocalStoreKey(color, "piece" + remainingPieces[i]);
                var retrievedObject = localStorage.getItem(key);
                var image = new Image();
                image.src = retrievedObject;
                image.setAttribute("style", "width:55px; height:55px;");
                this.getElement().appendChild(image);
            }
        }
    }

};