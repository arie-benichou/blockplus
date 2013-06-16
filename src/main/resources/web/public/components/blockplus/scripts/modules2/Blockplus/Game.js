var Blockplus = Blockplus || {};

Blockplus.Game = function(client, boardManager) {

	this.client = client;
	this.boardManager = boardManager;

	this.gameState = null;
	this.currentColor = null; // TODO renommer en 'color' (et à injecter)
	this.option = null; // TODO renommer en 'options'
	this.selectedPositions = new SelectedPositions();
	this.potentialPositions = null;
	
	var that = this;
	Client.protocol.register("color", function(data) {
		that.currentColor = data; // TODO avoir un objet game
	});

	Client.protocol.register("update", function(data) {
		that.gameState = new GameState(data);
		that._updateUI();
	});

};

Blockplus.Game.prototype = {

	constructor : Blockplus.Game,

	_updateBoard : function() {
		this.boardManager.render(this.gameState.getBoard());
	},

	_updateOptions : function() {
		this.option = new Options(this.gameState.getOptions(this.currentColor));
	},

	_updatePotentialPositions : function() {
		this.potentialPositions = this.option.getPotentialPositions();
		for ( var potentialPosition in this.potentialPositions) {
			var position = JSON.parse(potentialPosition); // TODO à revoir
			this.boardManager.renderPotentialCell(position, this.currentColor);
		}

	},

	// TODO
	_updateOthers : function() {
		if (this.gameState.isTerminal()) {
			this.potentialPositions = null; // TODO clear();
			this.selectedPositions.clear();
		}
	},

	_updateUI : function() {
		this._updateBoard();
		this._updateOptions();
		this.selectedPositions.clear();
		this._updatePotentialPositions();
		//this.boardManager.showSelectedPotentialCells(this.selectedPositions.get(), this.currentColor);
		this._updateOthers();
	},

	bigMess : function() {
		/*--------------------------------------------------8<--------------------------------------------------*/
		// TODO extract class
		var moveSubmit = function(id, positions) {
			var object = {
				type : 'MoveSubmit',
				data : {
					id : id,
					positions : positions
				}
			};
			return object;
		};
		/*--------------------------------------------------8<--------------------------------------------------*/
		var that = this;
		/*--------------------------------------------------8<--------------------------------------------------*/
		
		var getPositionFromOffset = function(x, y) {
			var row = Math.floor(y / (that.cellDimension.height));
			var column = Math.floor(x / (that.cellDimension.width));
			return that.boardManager.positionFactory.getPosition(row, column);
		};
		
		// TODO remove duplicate code
		var offsetToPosition1 = function(event, targetOffset) {
			event.offsetX = event.pageX - targetOffset.left;
			event.offsetY = event.pageY - targetOffset.top;
			return getPositionFromOffset(event.offsetX, event.offsetY);
		};
		/*--------------------------------------------------8<--------------------------------------------------*/		
		// TODO ? appartient au futur BoardManager 
		var potentialCellClickEventHandler = function(event) {
			var position = offsetToPosition1(event, $(event.target).offset());
			// TODO à revoir
			var isPotentialPosition = JSON.stringify(position) in that.option.getPotentialPositions();
			if (isPotentialPosition) {
				if (that.selectedPositions.contains(position)) {
					that.boardManager.updateCell(position, "#FFF");
					that.selectedPositions.remove(position);
					that.boardManager.showPotentialCell(position, Colors[that.currentColor]);
					if (that.selectedPositions.isEmpty()) {
						return; // TODO à revoir
					}
				} else {
					that.boardManager.showSelectedPotentialCell(position, Colors[that.currentColor]);
					that.selectedPositions.add(position);
				}
			} else {
				that.boardManager.update(that.board);
				that._updatePotentialPositions();
				that.boardManager.showSelectedPotentialCells(that.selectedPositions.get());
			}
		};
		/*--------------------------------------------------8<--------------------------------------------------*/
//		var moveSubmitHandler = function(event) {
//			var pieceId = that.option.perfectMatch(that.selectedPositions);
//			var data = [];
//			for ( var position in that.selectedPositions.get()) {
//				var p = JSON.parse([ position ]);
//				data.push([ p.row, p.column ]);
//			}
//			that.client.say(moveSubmit(pieceId, data));
//		};
		/*--------------------------------------------------8<--------------------------------------------------*/
		this.boardManager.register('click', potentialCellClickEventHandler);
		/*--------------------------------------------------8<--------------------------------------------------*/
	}

};