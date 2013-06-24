var Blockplus = Blockplus || {};

Blockplus.PieceManager = function(element, pieceRenderer, url, positionFactory) {

	this.element = element;
	this.pieceRenderer = pieceRenderer;
	this.pieces = {};
	this.positionFactory = positionFactory;

	var that = this;
	jQuery.ajax(url, {
		success : function(xmlDocument) {
			var pieces = xmlDocument.evaluate("//piece", xmlDocument, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			for ( var color in that.pieceRenderer.colors) {
				that.pieces[color] = {};
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
					that.pieces[color][piece.getAttribute("id")] = canvas.toDataURL("image/png");
				}
			}
		}
	});
};

Blockplus.PieceManager.prototype = {

	constructor : Blockplus.PieceManager,

	container : function(color) {
		return $("#" + color);
	},

	piece : function(color, id) {
		return this.pieces[color][id];
	},

	update : function(color, pieces) {
		var container = this.container(color);
		container.html("");
		for ( var i = 1, n = pieces.length; i <= n; ++i) {
			var image = new Image();
			image.src = this.piece(color, i);
			if (pieces[n - i] == 0) {
				image.setAttribute("class", "used");
			}
			container.append(image);
		}
	},

	show : function(color) {
		var container = this.container(color);
		$("#" + this.element + " div").hide();
		container.show();
		$("#" + this.element).removeClass();
		$("#" + this.element).addClass(color);

		$("#players").removeClass();
		$("#players" + this.element).addClass(color);
	},

};