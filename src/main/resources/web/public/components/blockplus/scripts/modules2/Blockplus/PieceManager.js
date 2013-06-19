var Blockplus = Blockplus || {};

Blockplus.PieceManager = function(element, pieceRenderer, url, positionFactory, callBack) {

	this.element = element;
	this.pieceRenderer = pieceRenderer;
	this.pieces = {};
	this.positionFactory = positionFactory;

	console.debug(positionFactory);

	var that = this;
	jQuery.ajax(url, {
		success : function(xmlDocument) {
			var pieces = xmlDocument.evaluate("//piece", xmlDocument, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			for ( var color in that.pieceRenderer.colors) {
				for ( var i = 0; i < pieces.snapshotLength; ++i) {
					var data = [];
					var piece = pieces.snapshotItem(i);
					var positions = xmlDocument.evaluate(".//position", piece, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
					for ( var j = 0; j < positions.snapshotLength; ++j) {
						var position = positions.snapshotItem(j);
						var node = xmlDocument.evaluate(".//y/text()", position, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
						var y = parseInt(node.snapshotItem(0).textContent);
						var node = xmlDocument.evaluate(".//x/text()", position, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
						var x = parseInt(node.snapshotItem(0).textContent);
						data.push(that.positionFactory.getPosition(y, x));
					}
					var canvas = that.pieceRenderer.render(new Blockplus.Piece(data, color));
					that.pieces[color + "." + piece.getAttribute("name")] = canvas.toDataURL("image/png");
				}
			}
			callBack();
		}
	});
};

Blockplus.PieceManager.prototype = {

	constructor : Blockplus.PieceManager,

	hide : function() {
		$(this.element).hide();
	},

	show : function() {
		$(this.element).show();
	},

	piece : function(color, id) {
		return this.pieces[color + '.' + "piece" + id];
	},

	update : function(color, pieces) {
		$(this.element).html("");
		for (id in pieces) {
			var image = new Image();
			image.setAttribute("id", "piece" + id);
			image.src = this.piece(color, id);
			image.setAttribute("class", pieces[id] ? "available" : "not-available");
			this.element.appendChild(image);
		}
	},

};