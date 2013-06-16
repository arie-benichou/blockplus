var Application = function() {

	this.viewPort = new ViewPort({
		minWidth : 320,
		minHeight : 480
	});

	this.board = new Board({
		dimension : {
			rows : 20,
			columns : 20
		},
		cells : {
			Blue : [ 0, 21, 42, 63, 84, 105, 126, 147, 168, 189 ],
			Yellow : [ 19, 38, 57, 76, 95, 114, 133, 152, 171, 190 ],
			Red : [ 380, 361, 342, 323, 304, 285, 266, 247, 228, 209 ],
			Green : [ 399, 378, 357, 336, 315, 294, 273, 252, 231, 210 ]
		}
	});

	this.cellDimension = {
		width : this.viewPort.min / this.board.rows,
		height : this.viewPort.min / this.board.columns
	};

	this.colors = {
		Blue : "#3971c4",
		Yellow : "#eea435",
		Red : "#cc2b2b",
		Green : "#04a44b"
	};

	/*-----------------------8<-----------------------*/
	
	this.maxPieceSize = 5;
	
	this.neighbourhood = this.maxPieceSize - 1;
	
	this.scale = {
		x : this.board.columns / (2 * this.neighbourhood + 1),
		y : this.board.rows / (2 * this.neighbourhood + 1)
	};
	
	/*-----------------------8<-----------------------*/
	
	this.boardRenderer = new BoardRenderer(document.getElementById('board'), this.cellDimension, this.colors);
	this.positionFactory = new PositionFactory();
	this.boardManager = new BoardManager(this.board, this.boardRenderer, this.positionFactory);
	
};

Application.prototype = {

	constructor : Application,

};