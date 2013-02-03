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

// TODO more refactoring
var Game = function(client) {

    this.client = client;
    
    this.remainingPieces = new RemainingPieces("remaining-pieces");
    this.availablePieces = new AvailablePieces("available-pieces");

    // TODO renommer en 'boardElement' (et à injecter)
    this.boardRendering = new BoardRendering(new CellRendering(document.getElementById("board"), 34, 34, 33, 33));

    this.currentColor = null; // TODO renommer en 'color' (et à injecter)
    this.option = null; // TODO renommer en 'options'
    this.gameState = null;

    // TODO
    var mock = {
        dimension : {
            rows : 20,
            columns : 20
        },
        cells : {
            Blue : [],
            Yellow : [],
            Red : [],
            Green : []
        }
    };
    this.board = new Board(mock);
    // this.board = null;

    this.selectedPositions = new SelectedPositions();
    this.potentialPositions = null;

    this.audioManager = new AudioManager(new Audio());
    this.offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);

    var that = this;

    Client.protocol.register("color", function(data) {
        that.currentColor = data; // TODO avoir un objet game
    });

    Client.protocol.register("update", function(data) {
        that.gameState = new GameState(data);
        that._updateUI();
    });

    // TODO
    Client.protocol.register("link", function(data) {
        sessionStorage.setItem("blockplus.network.hashcode", JSON.stringify(data));
    });

};

Game.prototype = {

    constructor : Game,

    getBoard : function() {
        return this.board;
    },

    _updateColor : function() {
        // alert(this.gameState.getColor() + " must play..."); // TODO
    },

    // TODO à revoir
    _updatePieces : function() {
        this.availablePieces.clear();
        this.availablePieces.update(this.currentColor, this.gameState.getPieces(this.currentColor));
        this.availablePieces.show();
    },

    _updateBoard : function() {
        this.board = this.gameState.getBoard();
        this.boardRendering.update(this.board);
    },

    _updateOptions : function() {
        this.option = new Options(this.gameState.getOptions(this.currentColor));
    },
    
    _updatePotentialPositions : function() {
        this.potentialPositions = this.option.getPotentialPositions();
        for ( var potentialPosition in this.potentialPositions) {
            var position = JSON.parse(potentialPosition); // TODO à revoir
            this.boardRendering.showPotentialCell(position, Colors[this.currentColor]);
        }
            
    },
    
    _updateOthers : function() {
        this.remainingPieces.hide();
        if (this.gameState.isTerminal()) {
            
            this.audioManager.play("../audio/game-is-over.mp3");
            this.potentialPositions = null; // TODO clear();
            this.selectedPositions.clear();
            $(this.boardRendering.getCanvas()).attr("style", "opacity:0.33;");
            // ///////////////////////////////////////////
            $("#left").attr("style", "width:0");
            
            this.remainingPieces.clear();
            for ( var color in Colors) {
                var remainingPieces = this.gameState.getPieces(color);
                this.remainingPieces.update(color, remainingPieces);
            }
            this.remainingPieces.show();
        }
    },

    _updateUI : function() {

        this._updateColor();
        this._updatePieces();
        
        this._updateBoard();
        this._updateOptions();
        
        this.selectedPositions.clear();

        this._updatePotentialPositions();
        
        this.boardRendering.showSelectedPotentialCells(this.selectedPositions.get(), this.currentColor);
        
        this._updateOthers();
    },

    bigMess : function() {

        this.boardRendering.init(this.board); // TODO à revoir

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
        /*--------------------------------------------------8<--------------------------------------------------*
        // TODO extract class
        var roomReconnection = function(data) {
            var message = {
                type : 'RoomReconnection',
                data : {
                    link : data
                }
            };
            say(message);
        };
        /*--------------------------------------------------8<--------------------------------------------------*/
        var that = this;
        /*--------------------------------------------------8<--------------------------------------------------*/
        var potentialCellClickEventHandler = function(event) {
            event.preventDefault();
            var position = that.offsetToPositionBuilder.build(event.offsetX, event.offsetY);

            // TODO à revoir
            var isPotentialPosition = JSON.stringify(position) in that.option.getPotentialPositions();

            if (isPotentialPosition) {
                if (that.selectedPositions.contains(position)) {
                    that.boardRendering.updateCell(position, "White");
                    that.selectedPositions.remove(position);
                    that.boardRendering.showPotentialCell(position, Colors[that.currentColor]);
                    if (that.selectedPositions.isEmpty()) {
                        $("#submitPiece").hide();
                        that._updatePieces();
                        return; // TODO à revoir
                    }
                } else {
                    that.boardRendering.showSelectedPotentialCell(position, Colors[that.currentColor]);
                    that.selectedPositions.add(position);
                }
                var matches = that.option.matches(that.selectedPositions);
                for ( var i = 1; i <= 21; ++i) {
                    $(("#piece-" + i)).attr("class", "not-available");
                }
                var hasPotential = false;
                for ( var id in matches) {
                    hasPotential = true;
                    $(("#piece-" + id)).attr("class", "available");
                }
                if (!hasPotential) {
                    that.audioManager.play("../audio/none.mp3");
                }
                var id = that.option.perfectMatch(that.selectedPositions);
                if (id) {
                    $("#submitPiece").show();
                    that.audioManager.play("../audio/subtle.mp3");
                    $("#piece-" + id).attr("class", "perfect-match");
                    var topLeft = that.selectedPositions.getTopLeftPosition();
                    var bottomRight = that.selectedPositions.getBottomRightPosition();
                    var copy = function(topLeft, bottomRight) {
                        var width = 2 + 1 + bottomRight.column - topLeft.column;
                        var height = 2 + 1 + bottomRight.row - topLeft.row;
                        var newCanvas = document.getElementById("pieceToPlay");
                        newCanvas.width = 34 * width;
                        newCanvas.height = 34 * height;
                        var tmpBoardRendering = new BoardRendering(new CellRendering(newCanvas, 34, 34, 33, 33));
                        var positions = that.selectedPositions.get();
                        tmpBoardRendering.clear("#2a2d30");
                        for ( var position in positions) {
                            var p = JSON.parse(position); // TODO
                            tmpBoardRendering.updateCell(new Position(1 + p.row - topLeft.row, 1 + p.column - topLeft.column), that.currentColor);
                        }
                    };
                    copy(topLeft, bottomRight);
                    $("#pieceToPlay").attr("class", "opaque out");
                } else {
                    $("#pieceToPlay").attr("class", "transparent out");
                }
            } else {
                that.boardRendering.update(that.board);
                that._updatePotentialPositions();
                that.boardRendering.showSelectedPotentialCells(that.selectedPositions.get());
                that.remainingPieces.hide();
                var opponentColor = that.board.getOpponentColorAt(position);
                if (opponentColor != null) {
                    that.boardRendering.showOpponentCells(that.board, that.currentColor, opponentColor);
                    var remainingPieces = that.gameState.getPieces(opponentColor);
                    that.remainingPieces.clear();
                    that.remainingPieces.update(opponentColor, remainingPieces);
                    that.remainingPieces.show();
                }
                
            }
        };
        /*--------------------------------------------------8<--------------------------------------------------*/
        var moveSubmitHandler = function(event) {
            $("#submitPiece").hide();
            var pieceId = that.option.perfectMatch(that.selectedPositions);
            var data = [];
            for ( var position in that.selectedPositions.get()) {
                var p = JSON.parse([ position ]);
                data.push([ p.row, p.column ]);
            }
            // TODO ? se contenter des positions            
            that.client.say(moveSubmit(pieceId, data));            
        };
        /*--------------------------------------------------8<--------------------------------------------------*/
        $(this.boardRendering.getCanvas()).click(potentialCellClickEventHandler);
        $("#pieceToPlay").click(moveSubmitHandler);
        // localStorage.clear();
        var piece = document.getElementById("piece");
        createAllPiecesImages("/xml/pieces.xml", new BoardRendering(new CellRendering(piece, 13, 13, 12, 12)));
        $("#rooms").hide();
        $("#game").show();
        /*--------------------------------------------------8<--------------------------------------------------*/
    }

};