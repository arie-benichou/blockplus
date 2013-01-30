// TODO more refactoring
var Game = function(client) {

    this.client = client;

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
        $("#available-pieces").html('');
        for ( var i = 1; i <= 21; ++i) { // TODO
            var key = getLocalStoreKey(this.currentColor, "piece" + i);
            var retrievedObject = localStorage.getItem(key);
            var image = new Image();
            image.setAttribute("id", "piece-" + i);
            image.src = retrievedObject;
            image.setAttribute("class", "not-available");
            $("#available-pieces").append(image);
        }
        var array = this.gameState.getPieces(this.currentColor);
        for ( var i = 1; i <= 21; ++i)
            $("#piece-" + i).attr("class", "not-available");
        for ( var i = 0; i < array.length; ++i)
            $("#piece-" + array[i]).attr("class", "available");
        $("#available-pieces").show();
    },

    _updateBoard : function() {
        this.board = this.gameState.getBoard();
        this.boardRendering.update(this.board);
    },

    _showPotentialCells : function(position) {
        var context = this.boardRendering.getContext();
        var color = Colors[this.currentColor];
        context.globalAlpha = 0.4;
        context.fillStyle = color;
        context.beginPath();
        context.arc(34 * position.column + 34 / 2, 34 * position.row + 34 / 2, 7, 0, Math.PI * 2, true);
        context.closePath();
        context.fill();
        context.globalAlpha = 0.8;
        context.lineWidth = 2;
        context.strokeStyle = color;
        context.stroke();
        context.globalAlpha = 1;
    },
    
    _hidePotentialCells : function(position) {
        this.boardRendering.updateCell(position, "White");
    },
    
    _showSelectedPotentialCell : function(position) {
            console.log(position);
            this.boardRendering.getContext().globalAlpha = 0.5;
            this.boardRendering.updateCell(position, Colors[this.currentColor]); // TODO pouvoir passer alpha
            this.boardRendering.getContext().globalAlpha = 1;
    },            
    
    _showSelectedPotentialCells : function() {
        var selectedPotentialPositions = this.selectedPositions.get();
        console.log(selectedPotentialPositions);
        for ( var selectedPotentialPosition in selectedPotentialPositions) {
            // TODO à revoir
            var position = JSON.parse(selectedPotentialPosition);
            this._showSelectedPotentialCell(position);
        }
    },

    _decorateOpponentCell : function(opponentColor, position) {
        this.boardRendering.getContext().globalAlpha = 0.5;
        // TODO add alpha parameter
        this.boardRendering.updateCell(position, "#FFF");
        this.boardRendering.getContext().globalAlpha = 1;
    },

    _updateOptions : function() {
        this.option = new Options(this.gameState.getOptions(this.currentColor));
    },
    
    _updatePotentialPositions : function() {
        this.potentialPositions = this.option.getPotentialPositions();
        for ( var potentialPosition in this.potentialPositions)
            this._showPotentialCells(JSON.parse(potentialPosition));
    },    

    _updateUI : function() {

        this._updateColor();
        this._updatePieces();
        
        this._updateBoard();
        this._updateOptions();
        
        this.selectedPositions.clear();
        this._updatePotentialPositions();
        this._showSelectedPotentialCells();
        

        $("#remaining-pieces").hide();

        if (this.gameState.isTerminal()) {
            this.audioManager.play("../audio/game-is-over.mp3");
            this.potentialPositions = null; // TODO clear();
            this.selectedPositions.clear();
            $(this.boardRendering.getCanvas()).attr("style", "opacity:0.33;");
            // ///////////////////////////////////////////
            $("#available-pieces").html('');
            // $("#available-pieces").hide();
            $("#left").attr("style", "width:0");
            $("#remaining-pieces").html('');
            for ( var color in Colors) {
                var array = this.gameState.getPieces(color);
                for ( var i = 0, n = array.length; i < n; ++i) {
                    if (array[i] != 0) { // TODO à revoir
                        var key = getLocalStoreKey(color, "piece" + array[i]);
                        var retrievedObject = localStorage.getItem(key);
                        var image = new Image();
                        image.src = retrievedObject;
                        image.setAttribute("style", "width:55px; height:55px;");
                        $("#remaining-pieces").append(image);
                    }
                }
            }
            $("#remaining-pieces").show();
        }

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
                    that._showPotentialCells(position);
                    if (that.selectedPositions.isEmpty()) {
                        $("#submitPiece").hide();
                        that._updatePieces();
                        return; // TODO à revoir
                    }
                } else {
                    that._showSelectedPotentialCell(position);
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
                var opponentColor = that.board.getOpponentColorAt(position);
                $("#remaining-pieces").html('');

                // TODO this.selectedOpponent
                that.boardRendering.update(that.board);
                that._updatePotentialPositions();
                that._showSelectedPotentialCells();
                
                if (opponentColor != null /*&& opponentColor != that.currentColor*/) {
                    
                    that.boardRendering.showOpponentCells(that.board, that.currentColor, opponentColor);
                    

                    
                    //if (opponentColor != that.currentColor) {
                        // TODO extract method
                        var f = function(color) {
                            var array = that.gameState.getPieces(color);
                            for ( var i = 0, n = array.length; i < n; ++i) {
                                if (array[i] != 0) { // TODO à revoir
                                    var key = getLocalStoreKey(color, "piece" + array[i]);
                                    var retrievedObject = localStorage.getItem(key);
                                    var image = new Image();
                                    image.src = retrievedObject;
                                    image.setAttribute("style", "width:55px; height:55px;");
                                    $("#remaining-pieces").append(image);
                                }
                            }
                        };
                        f(opponentColor);
                        $("#remaining-pieces").show();
                    //}
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
            for ( var potentialPosition in this.potentialPositions)
                that._hidePotentialCells(JSON.parse(potentialPosition));
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