var Blockplus = Blockplus || {};

Blockplus.Game = function(client, color, gameState, boardManager) {
	this.client = client;
	this.color = color;
	this.gameState = gameState;
	this.boardManager = boardManager;
	this.options = new Blockplus.Options(this.gameState.getOptions(this.color));
};

Blockplus.Game.prototype = {

	constructor : Blockplus.Game,

	update : function() {
		this.boardManager.updateBoard(this.gameState.getBoard());
		this.boardManager.updatePotentialPositions(this.options.getPotentialPositions());
		this.boardManager.render();
	},

};