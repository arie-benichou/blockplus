var Blockplus = Blockplus || {};

Blockplus.PieceManager = function(element, pieceRenderer, url) {

	this.element = element;

	this.pieceRenderer = pieceRenderer;

	this.pieces = {};

	var that = this;
	jQuery.ajax(url, {
		success : function(xmlDocument) {
			var pieces = xmlDocument.evaluate("//piece", xmlDocument, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			// TODO !!injecter Colors
			for ( var color in application.colors) {
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
						data.push(application.positionFactory.getPosition(y, x));
					}
					var canvas = that.pieceRenderer.render(new Blockplus.Piece(data, application.colors[color]));
					that.pieces[color + "." + piece.getAttribute("name")] = canvas.toDataURL("image/png");
				}
			}

			// TODO à mettre dans Application
			var data = [];
			// TODO !!injecter Colors
			for ( var color in application.colors) {
				data.push(that.pieces[color + "." + "piece21"]);
			}
			data.reverse();
			// TODO request animation frame
			var id = window.setInterval(function() {
				if (data.length > 0) {
					var img = document.createElement('img');
					img.id = data.length;
					$("#splash").append(img);
					img.src = data.pop();
				} else {
					window.clearInterval(id);
					var data2 = [];
					for ( var color in application.colors) {
						data2.push(that.pieces[color + "." + "piece1"]);
						data2.push(that.pieces[color + "." + "piece8"]);
						data2.push(that.pieces[color + "." + "piece21"]);
					}
					data2.reverse();
					var n = 0;
					var t = 3;
					// var id2 = window.setInterval(function() {
					// $("#4").attr('src', data2[n % t]);
					// $("#3").attr('src', data2[n % t + t]);
					// $("#2").attr('src', data2[n % t + t * 2]);
					// $("#1").attr('src', data2[n % t + t * 3]);
					// ++n;
					// }, 160);
				}

			}, 120);
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

	update : function(color, pieces) {
		$(this.element).html("");
		for (id in pieces) {
			var image = new Image();
			image.setAttribute("id", "piece" + id);
			image.src = this.pieces["Blue" + "." + "piece" + id];
			image.setAttribute("class", pieces[id] ? "available" : "not-available");
			this.element.appendChild(image);
		}
	},

};