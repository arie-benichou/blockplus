var Blockplus = Blockplus || {};

Blockplus.PieceManager = function(element, pieceRenderer, url) {

	this.element = element;

	this.pieceRenderer = pieceRenderer;

	this.pieces = {};

	var that = this;
	jQuery.ajax(url, {
		success : function(xmlDocument) {
			var pieces = xmlDocument.evaluate("//piece", xmlDocument, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			for ( var color in application.colors) { // TODO !!injecter
				// Colors
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
					// console.log(canvas.toDataURL());

					// var img = document.createElement('img');
					// img.width = canvas.width;
					// img.height = canvas.height;
					// img.src = canvas.toDataURL();
					// //$("#pieces").append(img); // TODO
					// $("#pieces").html(img); // TODO

					that.pieces[color + "." + piece.getAttribute("name")] = canvas.toDataURL("image/png");
				}
				// break;
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
					var id2 = window.setInterval(function() {
						$("#4").attr('src', data2[n % t]);
						$("#3").attr('src', data2[n % t + t]);
						$("#2").attr('src', data2[n % t + t * 2]);
						$("#1").attr('src', data2[n % t + t * 3]);
						++n;
					}, 160);
				}

			}, 120);

			// TODO à mettre dans appli
			that.update();

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

	update : function(pieces) {
		$("#pieces").html("");
		var data = [];
		for ( var i = 1; i <= 21; ++i) {
			data.push(this.pieces["Blue" + "." + "piece" + i]);
		}
		data.reverse();
		for ( var i = 1; i <= 21; ++i) {
			var img = document.createElement('img');
			img.id = i;
			$("#pieces").append(img);
			img.src = data.pop();
		}
	},

};