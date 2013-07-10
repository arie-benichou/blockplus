var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.PieceManager = function(element, pieceRenderer, url, positionFactory) {

	this.element = element;
	this.pieceRenderer = pieceRenderer;
	this.pieces = {};
	this.positionFactory = positionFactory;

	jQuery.ajax(url, {
		async : false,
		success : $.proxy(function(data) {
			var pieces = JSON.parse(data);
			var n = pieces.length;
			for ( var color in this.pieceRenderer.colors) {
				this.pieces[color] = {};
				for ( var i = 0; i < n; ++i) {
					var positions = [];
					var data = pieces[i];
					for ( var j = 0; j < data.length; ++j) {
						positions.push(this.positionFactory.position(data[j].y, data[j].x));
					}
					var canvas = this.pieceRenderer.render(new Blockplus.Piece(positions, color));
					this.pieces[color][i] = canvas.toDataURL("image/png");
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
		return this.pieces[color][id];
	},

	init : function(color, pieces) {
		console.debug(pieces.length);
		var container = this.container(color);
		container.html("");
		for ( var i = 0, n = pieces.length; i < n; ++i) {
			var image = new Image();
			image.src = this.piece(color, i);
			if (pieces[i] == 0) {
				image.setAttribute("class", "used");
			}
			container.append(image);
		}
	},

	update : function(color, pieces) {
		console.debug(pieces);
		var container = this.container(color);
		for ( var i = 0, n = pieces.length; i < n; ++i) {
			if (pieces[i] == 0) {
				var image = $($("#" + container.attr("id") + " img")[i]);
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
	}

};