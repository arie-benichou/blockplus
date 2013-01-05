/*--------------------------------------------------8<--------------------------------------------------*/
var getLocalStoreKey = function(color, piece) {
    return color + "." + piece;
};
/*--------------------------------------------------8<--------------------------------------------------*/
var createPiecesImages = function(color, pieces, pieceRendering) {
    var image = new Image();
    image.src = "";
    document.body.appendChild(image);
    for ( var i = 0; i < pieces.snapshotLength; ++i) {
        var piece = pieces.snapshotItem(i);
        var name = piece.getAttribute("name");
        var positions = document.evaluate("position", piece, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
        var n = 7;
        var t = Math.floor(n / 2);
        var ty, tx;
        var referential = document.evaluate("referential", piece, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null);
        if (referential.singleNodeValue == null) {
            var position = positions.snapshotItem(0);
            var y = document.evaluate("y", position, null, XPathResult.NUMBER_TYPE, null);
            var x = document.evaluate("x", position, null, XPathResult.NUMBER_TYPE, null);
            ty = t - y.numberValue;
            tx = t - x.numberValue;
        } else {
            var y = document.evaluate("y", referential.singleNodeValue, null, XPathResult.NUMBER_TYPE, null);
            var x = document.evaluate("x", referential.singleNodeValue, null, XPathResult.NUMBER_TYPE, null);
            ty = t - y.numberValue;
            tx = t - x.numberValue;
        }
        $("piece").width = n * 12; // TODO
        $("piece").height = n * 12; // TODO
        for ( var j = 0; j < positions.snapshotLength; ++j) {
            var position = positions.snapshotItem(j);
            var y = document.evaluate("y", position, null, XPathResult.NUMBER_TYPE, null);
            var x = document.evaluate("x", position, null, XPathResult.NUMBER_TYPE, null);
            var py = y.numberValue + ty;
            var px = x.numberValue + tx;
            pieceRendering.updateCell(new Position(py, px), color);
        }
        var key = getLocalStoreKey(color, name);
        var value = $("piece").toDataURL("image/png");
        localStorage.setItem(key, value);
        pieceRendering.clear("piece");
    }
};
/*--------------------------------------------------8<--------------------------------------------------*/
var createAllPiecesImages = function(url, pieceRendering) {
    new Ajax.Request(url, {
        onSuccess : function(response) {
            var data = response.responseXML;
            var pieces = data.evaluate("//piece", data, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
            for ( var color in Colors) {
                createPiecesImages(Colors[color], pieces, pieceRendering);
            }
            for ( var i = 1; i <= 21; ++i) {
                var retrievedObject = localStorage.getItem(getLocalStoreKey("Green", "piece" + i));
                var image = new Image();
                image.setAttribute("id", "piece-" + i);
                image.src = retrievedObject;
                image.setAttribute("class", "not-available");
                $("available-pieces").appendChild(image);
            }            
        }
    });
};
/*--------------------------------------------------8<--------------------------------------------------*/