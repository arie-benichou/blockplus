var Blockplus = Blockplus || {};

Blockplus.PieceManager = function(element, pieceRenderer, url, positionFactory) {

	this.element = element;
	this.pieceRenderer = pieceRenderer;
	this.pieces = {};
	this.positionFactory = positionFactory;

	jQuery.ajax(url, {
		async : false,
		success : $.proxy(function(xmlDocument) {
			var pieces = xmlDocument.evaluate("//piece", xmlDocument, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			for ( var color in this.pieceRenderer.colors) {
				this.pieces[color] = {};
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
						data.push(this.positionFactory.position(y, x));
					}
					var canvas = this.pieceRenderer.render(new Blockplus.Piece(data, color));
					this.pieces[color][piece.getAttribute("id")] = canvas.toDataURL("image/png");
				}
			}
		}, this)
	});
};

Blockplus.PieceManager.prototype = {

	constructor : Blockplus.PieceManager,

	container : function(color) {
		return $("#" + color);
	},

	piece : function(color, id) {
		console.debug(this.pieces);
		return this.pieces[color][id];
	},

	init : function(color, pieces) {
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

	update : function(color, pieces) {
		var container = this.container(color);
		for ( var i = 1, n = pieces.length; i <= n; ++i) {
			if (pieces[n - i] == 0) {
				var image = $($("#" + container.attr("id") + " img")[i - 1]);
				if (image.attr("class") != "used") {
					image.attr("class", "used");
				}
			}
		}
	},

	show : function(color) {
		var current = $("#" + this.element).attr("class");
		if (current != color) {
			$("#" + this.element + " div").hide();
			this.container(color).show();
			$("#" + this.element).removeClass();
			$("#" + this.element).addClass(color);
		}
	},

};