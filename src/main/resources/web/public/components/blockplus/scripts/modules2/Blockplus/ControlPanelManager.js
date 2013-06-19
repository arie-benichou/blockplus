var Blockplus = Blockplus || {};

/*
 * TODO renommer en MoveProposalManager
 */
Blockplus.ControlPanelManager = function(canvas, viewPort) {

	this.canvas = canvas;
	this.viewPort = viewPort;

	this.context = canvas.getContext("2d");

	console.log(this.context.canvas.width, this.context.canvas.height);
	console.log(viewPort);

	// TODO déterminer si Portrait ou Landscape
	this.context.canvas.width = this.viewPort.min
	this.context.canvas.height = this.viewPort.max - this.viewPort.min;

	var min = Math.min(this.context.canvas.width, this.context.canvas.height);

	this.cellDimension = {
		width : min / (5 + 1),
		height : min / (5 + 1)
	};

	console.log(this.cellDimension);

	// TODO à revoir
	this.colors = {
		Blue : "#3971c4",
		Yellow : "#eea435",
		Red : "#cc2b2b",
		Green : "#04a44b"
	};

	// in order to avoid annoying behaviour...
	this.register('mousedown', function(event) {
		event.preventDefault();
	});

};

Blockplus.ControlPanelManager.prototype = {

	constructor : Blockplus.ControlPanelManager,

	register : function(name, handler) {
		$(this.canvas).bind(name, handler);
	},

	unregister : function(name) {
		$(this.canvas).unbind(name);
	},

	hide : function() {
		$(this.canvas).hide();
	},

	show : function() {
		$(this.canvas).show();
	},

	handle : function(options, selectedPositions, boardManager, color) {

		var potentialPositions = options.matchPotentialPositions(selectedPositions);
		console.log(potentialPositions);

		for ( var position in options.getPotentialPositions()) {
			if (!(position in selectedPositions.get())) {
				var p = JSON.parse(position);
				boardManager.renderEmptyCell(p);
				if (position in potentialPositions) {
					boardManager.renderPotentialCell(p, color);
				}
				else {
					boardManager.renderPotentialCell2(p, color);
				}
			}
		}

		var isPlayable = options.perfectMatch(selectedPositions);

		if (!isPlayable) {
			this.hide();
		} else {
			this.show();

			var topLeft = selectedPositions.getTopLeftPosition();
			var bottomRight = selectedPositions.getBottomRightPosition();

			var that = this;
			var copy = function(topLeft, bottomRight) {

				// TODO initialiser une fois
				var newCanvas = document.createElement('canvas');
				var ctx = newCanvas.getContext("2d");
				var tmpBoardRendering = new Blockplus.BoardRenderer(newCanvas, that.cellDimension, that.colors);

				var width = 1 + bottomRight.column - topLeft.column;
				var height = 1 + bottomRight.row - topLeft.row;

				newCanvas.width = that.cellDimension.width * width;
				newCanvas.height = that.cellDimension.height * height;

				ctx.fillStyle = "#2a2d30";
				ctx.fillRect(0, 0, that.canvas.width, that.canvas.height);

				// tmpBoardRendering.clear("#2a2d30");
				for ( var position in selectedPositions.get()) {
					var p1 = JSON.parse(position); // TODO à revoir
					var p2 = {
						row : p1.row - topLeft.row,
						column : p1.column - topLeft.column
					}
					tmpBoardRendering.renderCell(p2, that.colors.Blue);
				}

				// console.log(newCanvas.toDataURL());

				that.context.fillStyle = "#2a2d30";
				that.context.fillRect(0, 0, that.canvas.width, that.canvas.height);

				var x = (that.context.canvas.width - newCanvas.width) / 2;
				var y = (that.context.canvas.height - newCanvas.height) / 2;
				// that.context.putImageData(ctx.getImageData(0, 0,
				// newCanvas.width, newCanvas.height), x, y);
				that.context.drawImage(newCanvas, x, y);

			}(topLeft, bottomRight);

		}

	},

};