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
		return 'Position{' + 'row=' + this.getRow() + ', ' + 'column=' + this.getColumn() + '}';
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
		return 'OffsetToPositionBuilder{' + 'offsetX=' + this.getOffsetX() + ', ' + 'offsetY=' + this.getOffsetY() + '}';
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
	toString : function() {
		return 'CellRendering' + '{' + 'offsetY=' + this.offsetY + ', ' + 'offsetX=' + this.offsetX + ', ' + 'width=' + this.width + ', ' + 'length='
				+ this.length;
		'}';
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
	toString : function() {
		return 'BoardRendering{' + 'cellRendering=' + this.cellRendering + '}';
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
}, false);
boardRendering.getCanvas().addEventListener("click", function(event) {
	if (!event.ctrlKey)
		boardRendering.updateCell(offsetToPositionBuilder.build(event.offsetX, event.offsetY), "black");
}, false);
/*--------------------------------------------------8<--------------------------------------------------*/
var source = new EventSource('/blockplus/data');
source.addEventListener('open', function(event) {
	console.log("Event listening...");
}, false);

/*
source.addEventListener('message', function(event) {
	boardRendering.update(JSON.parse(event.data));
	// event.target.removeEventListener('message', this);
	source.removeEventListener('message', this);
	event.target.close();

	event.target.addEventListener('message', function(event) {
		// TODO check origin & integrity
		boardRendering.update(JSON.parse(event.data));
		// event.target.close();
	}, false);
}, false);
*/

source.addEventListener('error', function(event) {
	if (event.readyState == EventSource.CLOSED)
		console.log("Event handling error");
}, false);

source.addEventListener('gamenotover', function(event) {
	console.log("Game is not over");
	if ($("audio-game") == undefined) {
		var music = document.createElement('audio');
		music.setAttribute('id', 'audio-game');
		//music.setAttribute('controls', 'controls');
		music.setAttribute('loop', 'loop');
		var musicSource = document.createElement('source');
		musicSource.setAttribute('src', './audio/game.mp3');
		musicSource.setAttribute('type', 'audio/mp3');
		music.appendChild(musicSource);
		music.play();
		console.log(music);
		document.getElementsByTagName("body")[0].appendChild(music);
		console.log(document.getElementById('audio-game'));
	}
	boardRendering.update(JSON.parse(event.data));
}, false);
source.addEventListener('gameover', function(event) {
	console.log("Game is over.");
	boardRendering.update(JSON.parse(event.data));	
	if ($("audio-game") != undefined) $("audio-game").pause();
	if ($("audio-game-over") == undefined) {
		var music = document.createElement('audio');
		music.setAttribute('id', 'audio-game-over');
		var musicSource = document.createElement('source');
		musicSource.setAttribute('src', './audio/game-over.mp3');
		musicSource.setAttribute('type', 'audio/mp3');
		music.appendChild(musicSource);
		music.play();
	}
	event.target.close();
}, false);
/*--------------------------------------------------8<--------------------------------------------------*/