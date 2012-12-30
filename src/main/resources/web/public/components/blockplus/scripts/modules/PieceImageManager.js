/*--------------------------------------------------8<--------------------------------------------------*/
var getLocalStoreKey = function(color, piece) {
    return color + "." + piece;
};
/*--------------------------------------------------8<--------------------------------------------------*/
var createPiecesImages = function(color, pieces) {
    var image = new Image();
    image.src = "";
    document.body.appendChild(image);
    (function myLoop(i) {
        setTimeout(function() {

            var piece = pieces.snapshotItem(i);
            var name = piece.getAttribute("name");
            var radius = parseInt(piece.getAttribute("radius"), 10);
            var positions = document.evaluate("position", piece, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);

            var n = 7;
            // var n = 1 + 2 * (radius + 1);
            var t = Math.floor(n / 2);

            var referential = document.evaluate("referential", piece, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null);
            if (referential.singleNodeValue == null) {
                for ( var j = 0; j < 1; ++j) {
                    var position = positions.snapshotItem(j);
                    var y = document.evaluate("y", position, null, XPathResult.NUMBER_TYPE, null);
                    var x = document.evaluate("x", position, null, XPathResult.NUMBER_TYPE, null);
                }
                var ty = t - y.numberValue;
                var tx = t - x.numberValue;
            }
            else {
                console.log(name);
                console.log(referential.singleNodeValue);
                var y = document.evaluate("y", referential.singleNodeValue, null, XPathResult.NUMBER_TYPE, null);
                var x = document.evaluate("x", referential.singleNodeValue, null, XPathResult.NUMBER_TYPE, null);
                console.log(y.numberValue);
                console.log(x.numberValue);
                var ty = t - y.numberValue;
                var tx = t - x.numberValue;
            }

            $("piece").width = n * 12; // TODO
            $("piece").height = n * 12; // TODO

            for ( var j = 0; j < positions.snapshotLength; ++j) {
                var position = positions.snapshotItem(j);
                var y = document.evaluate("y", position, null, XPathResult.NUMBER_TYPE, null);
                var x = document.evaluate("x", position, null, XPathResult.NUMBER_TYPE, null);
                // var ty = y.numberValue + Math.floor((n - (radius + 1)) / 2);
                // var tx = x.numberValue + Math.floor((n - (radius + 1)) / 2);
                var py = y.numberValue + ty;
                var px = x.numberValue + tx;
                boardRendering.updateCell(new Position(py, px), color);
            }

            var key = getLocalStoreKey(color, name);
            var value = $("piece").toDataURL("image/png");
            localStorage.setItem(key, value);
            var retrievedObject = localStorage.getItem(key);
            image.src = retrievedObject;
            boardRendering.clear("piece");

            if (i + 1 < pieces.snapshotLength)
                myLoop(i + 1);

        }, 75);
    })(0);
};
/*--------------------------------------------------8<--------------------------------------------------*/
var createAllPiecesImages = function(url) {
    new Ajax.Request(url, {
        onSuccess : function(response) {
            var data = response.responseXML;
            var pieces = data.evaluate("//piece", data, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
            for ( var color in Colors) {
                createPiecesImages(Colors[color], pieces);
            }
        }
    });
};
/*--------------------------------------------------8<--------------------------------------------------*/
