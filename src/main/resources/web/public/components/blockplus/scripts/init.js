var boardRendering = new BoardRendering(new CellRendering("piece", 12, 12, 11, 11));
/*--------------------------------------------------8<--------------------------------------------------*/
new Ajax.Request("/pieces.xml", {onSuccess: function(response) {
	var color = "Blue";
	var data = response.responseXML;
	var pieces = data.evaluate("//piece", data, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null );
	for ( var i=0 ; i < pieces.snapshotLength; ++i ) {
		var piece = pieces.snapshotItem(i);
		var positions = document.evaluate("position", piece, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null );
		for ( var j=0 ; j < positions.snapshotLength; ++j ) {
			var position = positions.snapshotItem(j);
			var y = document.evaluate("y", position, null, XPathResult.NUMBER_TYPE, null );
			var x = document.evaluate("x", position, null, XPathResult.NUMBER_TYPE, null );
			boardRendering.updateCell(new Position(y.numberValue, x.numberValue), color);
		}
		var imageDataURL = $("piece").toDataURL("image/png");
		localStorage.setItem(color + (i + 1), imageDataURL);
		var retrievedObject = localStorage.getItem(color + (i + 1));
		var image = new Image();
		image.src = retrievedObject;
		$("available-pieces").appendChild(image);
		boardRendering.clear("piece");
	}
	//window.location="game.html";
}});
/*--------------------------------------------------8<--------------------------------------------------*/