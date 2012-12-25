/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * Position
 */
var Position = Class.create({
	initialize : function(y, x) {
		this.row = y;
		this.column = x;
	},
	getRow : function() {
		return this.row;
	},
	getColumn : function() {
		return this.column;
	},
	toString : function() {
		return "Position{" + "row=" + this.getRow() + ", " + "column=" + this.getColumn() + "}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * OffsetToPositionBuilder
 */
var OffsetToPositionBuilder = Class.create({
	initialize : function(offsetX, offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	},
	getOffsetX : function() {
		return this.offsetX;
	},
	getOffsetY : function() {
		return this.offsetY;
	},
	build : function(x, y) {
		var row = Math.floor(y / this.offsetY);
		var column = Math.floor(x / this.offsetX);
		newPosition = new Position(row, column);
		console.log(newPosition.toString());
		return newPosition;

	},
	toString : function() {
		return "OffsetToPositionBuilder{" + "offsetX=" + this.getOffsetX() + ", " + "offsetY=" + this.getOffsetY() + "}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * CellRendering
 */
var CellRendering = Class.create({
	initialize : function(canvas, offsetY, offsetX, width, length) {
		this.canvas = $(canvas);
		this.context = this.canvas.getContext("2d");
		this.offsetY = offsetY;
		this.offsetX = offsetX;
		this.width = width;
		this.length = length;
	},
	getCanvas : function() {
		return this.canvas;
	},
	getContext : function() {
		return this.context;
	},
	update : function(position, state) {
		switch (state) {
		case "Blue":
			this.getContext().fillStyle = "blue";
			break;
		case "Yellow":
			this.getContext().fillStyle = "yellow";
			break;
		case "Green":
			this.getContext().fillStyle = "green";
			break;
		case "Red":
			this.getContext().fillStyle = "red";
			break;
		case "White":
			this.getContext().fillStyle = "gray";
			break;
		default:
			this.getContext().fillStyle = "black";
		}
		this.getContext().fillRect(this.offsetX * position.getColumn(), this.offsetY * position.getRow(), this.width, this.length);
	},
	// TODO ajouter canvas et context
	toString : function() {
		return "CellRendering" + "{" + "offsetY=" + this.offsetY + ", " + "offsetX=" + this.offsetX + ", " + "width=" + this.width + ", " + "length="
				+ this.length;
		"}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * BoardRendering
 */
var BoardRendering = Class.create({
	initialize : function(cellRendering) {
		this.cellRendering = cellRendering;
	},
	getCanvas : function() {
		return this.cellRendering.getCanvas();
	},
	getContext : function(board) {
		return this.cellRendering.getContext();
	},
	updateCell : function(position, state) {
		this.cellRendering.update(position, state);
	},
	update : function(board) {
		for ( var i = 0; i < board.length; i++) {
			var row = board[i];
			for ( var j = 0; j < row.length; j++) {
				var state = board[i][j];
				var position = new Position(i, j);
				this.updateCell(position, state);
			}
		}
	},
	clear : function() {
		this.getContext().clearRect(0, 0, this.getCanvas().width, this.getCanvas().height);
	},	
	toString : function() {
		return "BoardRendering{" + "cellRendering=" + this.cellRendering + "}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
var offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);
console.log(offsetToPositionBuilder.toString());
var boardRendering = new BoardRendering(new CellRendering("board", 34, 34, 33, 33));
console.log(boardRendering.toString());
/*--------------------------------------------------8<--------------------------------------------------*/
boardRendering.getCanvas().addEventListener("click", function(event) {
	if (event.ctrlKey)
		window.location = event.srcElement.toDataURL("image/png");
	else
		boardRendering.updateCell(offsetToPositionBuilder.build(event.offsetX, event.offsetY), "black");
}, false);
/*--------------------------------------------------8<--------------------------------------------------*/
// TODO ! extract EventManager
// TODO ! ajouter une zone de notifications dans la page
// TODO ! check origin & integrity of messages
// TODO ! utiliser le local storage pour enregistrer le currentime de la
// musique, par exemple...
// TODO ? émettre l"événement newgame
var source = new EventSource("/blockplus/data");

source.addEventListener("open", function(event) {
	console.log("Event listening");
}, false);

source.addEventListener("message", function(event) {
	console.log(event.data);
	// TODO extract method
	{
		var newChild = document.createElement("div");
		newChild.setAttribute("id", "last-message");
		newChild.innerHTML = JSON.parse(event.data);
		$("last-message").remove();
		$("messages").appendChild(newChild);
	}
}, false);

source.addEventListener("error", function(event) {
	console.log(event);
	//console.log(event.data);
	console.log(event.type);
	//console.log(event.target);
	console.log(event.readyState);
	console.log(event.srcElement.readyState);
	if (event.readyState == EventSource.CLOSED) {
		console.log("Event handling error");
		alert('error');
	}
		
}, false);

source.addEventListener("gamenotover", function(event) {
	boardRendering.update(JSON.parse(event.data));
	$("board").className = "game-is-not-over";
	if ($("game-is-not-over").currentTime == 0) {
		//$("game-is-not-over").loop = true;
		$("game-is-not-over").play();
	}
}, false);

source.addEventListener("bag", function(event) {
	console.log(event.data);
	var array = JSON.parse(event.data);	
	/*
	Element.update("available-pieces", "");
	console.log(array);
	for ( var i=0 ; i < array.length; ++i ) {
		var retrievedObject = localStorage.getItem("piece" + array[i]);
		var image = new Image();
		image.src = retrievedObject;
		$("available-pieces").appendChild(image);
	}
	*/
	for ( var i=0 ; i < array.length; ++i ) {
		$(("piece-" + array[i])).setAttribute("class", "not-available"); 
	}
}, false);

source.addEventListener("gameover", function(event) {
	$("game-is-over").play();
	event.target.close();
	boardRendering.update(JSON.parse(event.data));
	$("board").className = "game-is-over";
	$("game-is-not-over").pause();
}, false);
/*--------------------------------------------------8<--------------------------------------------------*/
/*
var pieceRendering = new BoardRendering(new CellRendering("piece", 12, 12, 11, 11));
new Ajax.Request("/pieces.xml", {onSuccess: function(response) {
	var data = response.responseXML;
	var pieces = data.evaluate("//piece", data, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null );
	for ( var i=0 ; i < pieces.snapshotLength; ++i ) {
		console.log(i);
		var piece = pieces.snapshotItem(i);
		var positions = document.evaluate("position", piece, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null );
		for ( var j=0 ; j < positions.snapshotLength; ++j ) {
			var position = positions.snapshotItem(j);
			var y = document.evaluate("y", position, null, XPathResult.NUMBER_TYPE, null );
			var x = document.evaluate("x", position, null, XPathResult.NUMBER_TYPE, null );
			pieceRendering.updateCell(new Position(y.numberValue, x.numberValue), "black");
		}
		var imageDataURL = $("piece").toDataURL("image/png");
		localStorage.setItem("piece" + i, imageDataURL);
		var retrievedObject = localStorage.getItem("piece" + i);
		var image = new Image();
		image.src = retrievedObject;
		$("available-pieces").appendChild(image);
		pieceRendering.clear("piece");
	}
	console.log("ok1");
	$("initialization").hide();
	console.log("ok2");
}});
*/
/*--------------------------------------------------8<--------------------------------------------------*/
for ( var i=1 ; i <= 21; ++i ) {
	var retrievedObject = localStorage.getItem("piece" + i);
	var image = new Image();
	image.setAttribute("id", "piece-" + i);
	image.src = retrievedObject;
	image.setAttribute("class", "available"); 
	$("available-pieces").appendChild(image);
}