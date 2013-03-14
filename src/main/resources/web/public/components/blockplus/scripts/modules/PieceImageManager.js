/*
 * Copyright 2012-2013 ArteFact
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

// TODO extract class
/*--------------------------------------------------8<--------------------------------------------------*/
var getLocalStoreKey = function(color, piece) {
	return color + "." + piece;
};
/*--------------------------------------------------8<--------------------------------------------------*/
var createPiecesImages = function(color, xmlDocument, pieceRendering) {
	var size = 13;
	var pieces = xmlDocument.evaluate("//piece", xmlDocument, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
	for ( var i = 0; i < pieces.snapshotLength; ++i) {
		var piece = pieces.snapshotItem(i);
		var positions = xmlDocument.evaluate(".//position", piece, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
		var n = 7;
		var t = Math.floor(n / 2);
		var ty, tx;
		var p0 = positions.snapshotItem(0);
		var node = xmlDocument.evaluate(".//y/text()", p0, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
		var y = parseInt(node.snapshotItem(0).textContent);
		ty = t - y;
		var node = xmlDocument.evaluate(".//x/text()", p0, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
		var x = parseInt(node.snapshotItem(0).textContent);
		tx = t - x;
		$("#piece").width = n * size; // TODO
		$("#piece").height = n * size; // TODO
		pieceRendering.clear(Colors[color]);
		for ( var j = 0; j < positions.snapshotLength; ++j) {
			var position = positions.snapshotItem(j);
			var node = xmlDocument.evaluate(".//y/text()", position, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			var y = parseInt(node.snapshotItem(0).textContent);
			var node = xmlDocument.evaluate(".//x/text()", position, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
			var x = parseInt(node.snapshotItem(0).textContent);
			var py = y + ty;
			var px = x + tx;
			pieceRendering.updateCell(new Position(py, px), "white");
		}
		var key = getLocalStoreKey(color, piece.getAttribute("name"));
		var value = document.getElementById("piece").toDataURL("image/png");
		localStorage.setItem(key, value);
	}
};
/*--------------------------------------------------8<--------------------------------------------------*/
var createAllPiecesImages = function(url, pieceRendering) {
	jQuery.ajax(url, {
		success : function(xmlDocument) {
			for ( var color in Colors) {
				createPiecesImages(color, xmlDocument, pieceRendering);
			}
			$("#initialization").hide();
		}
	});
};
/*--------------------------------------------------8<--------------------------------------------------*/