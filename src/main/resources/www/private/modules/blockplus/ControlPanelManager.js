var Blockplus = Blockplus || {};
/**
 * @constructor
 */
Blockplus.ControlPanelManager = function(canvas, viewPort, audioManager, colors, positionFactory) {

	this.positionFactory = positionFactory;
	this.colors = colors;
	this.audioManager = audioManager;

	this.canvas = canvas;
	this.viewPort = viewPort;

	this.context = canvas.getContext("2d");

	// TODO déterminer si Portrait ou Landscape

	var playersDivHeight = $("#players div").height() + 6;

	this.context.canvas.width = this.viewPort.min
	this.context.canvas.height = this.viewPort.max - this.viewPort.min - playersDivHeight;

	var min = Math.min(this.context.canvas.width, this.context.canvas.height);

	this.cellDimension = {
		width : min / (5 + 1),
		height : min / (5 + 1)
	};

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
		for ( var position in options.getPotentialPositions()) {
			if (!(position in selectedPositions.get())) {
				var p = this.positionFactory.positionFromIndex(position);
				boardManager.renderEmptyCell(p);
				if (position in potentialPositions) {
					boardManager.renderPotentialCell(p, color);
				} else {
					boardManager.renderPotentialCell2(p, color);
				}
			}
		}
		var isPlayable = options.perfectMatch(selectedPositions);
		if (!isPlayable) {
			this.hide();
		} else {
			var positions = {};
			for (index in selectedPositions.get()) {
				positions[index] = this.positionFactory.positionFromIndex(index);
			}
			var topLeft = this.positionFactory.getTopLeftPosition(positions);
			var bottomRight = this.positionFactory.getBottomRightPosition(positions);
			var newCanvas = document.createElement('canvas');
			var ctx = newCanvas.getContext("2d");
			var tmpBoardRendering = new Blockplus.BoardRenderer(newCanvas, this.cellDimension, this.colors);
			var width = 1 + bottomRight.column - topLeft.column;
			var height = 1 + bottomRight.row - topLeft.row;
			newCanvas.width = this.cellDimension.width * width;
			newCanvas.height = this.cellDimension.height * height;
			for ( var id in positions) {
				var p1 = positions[id];
				var p2 = {
					row : p1.row - topLeft.row,
					column : p1.column - topLeft.column
				}
				tmpBoardRendering.renderCell(p2, "#FFF");
				tmpBoardRendering.renderCell2(p2, this.colors[color], 0.42);
			}
			this.context.fillStyle = "#373B3F";
			this.context.fillRect(0, 0, this.canvas.width, this.canvas.height);
			this.context.globalAlpha = 0.55;
			this.context.fillStyle = this.colors[color];
			this.context.fillRect(0, 0, this.canvas.width, this.canvas.height);
			this.context.globalAlpha = 1;
			var x = (this.context.canvas.width - newCanvas.width) / 2;
			var y = (this.context.canvas.height - newCanvas.height) / 2;
			this.context.drawImage(newCanvas, x, y);
			$("#color-dialogs").hide();
			this.show();
		}
	}
};