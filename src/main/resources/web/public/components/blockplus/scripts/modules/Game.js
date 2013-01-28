// TODO more refactoring
var Game = function(client) {

    this.client = client;

    this.currentColor = null;
    this.option = null;
    this.potentialPositions = null;
    this.board = document.getElementById("board");
    this.boardRendering = new BoardRendering(new CellRendering(board, 34, 34, 33, 33));
    this.selectedPositions = new SelectedPositions();
    this.audioManager = new AudioManager(new Audio());
    this.offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);

    var that = this;

    // TODO send a single game state object    
    Client.protocol.register("color", function(data) {
        that.currentColor = data; // TODO avoir un objet game
    });

    // TODO send a single game state object    
    Client.protocol.register("pieces", function(data) {
        $("#available-pieces").html('');
        for ( var i = 1; i <= 21; ++i) { // TODO
            var key = getLocalStoreKey(that.currentColor, "piece" + i);
            var retrievedObject = localStorage.getItem(key);
            var image = new Image();
            image.setAttribute("id", "piece-" + i);
            image.src = retrievedObject;
            image.setAttribute("class", "not-available");
            $("#available-pieces").append(image);
        }
        var array = data;
        for ( var i = 1; i <= 21; ++i)
            $("#piece-" + i).attr("class", "not-available");
        for ( var i = 0; i < array.length; ++i)
            $("#piece-" + array[i]).attr("class", "available");
        $("#available-pieces").show();
    });

    // TODO send a single game state object    
    Client.protocol.register("board", function(data) {
        that.boardRendering.update(data);
    });

    // TODO send a single game state object    
    Client.protocol.register("options", function(data) {
        that.option = new Options(data); // TODO
    });
    
    // TODO send a single game state object
    Client.protocol.register("potential", function(data) {
        that.selectedPositions.clear();
        that.potentialPositions = new PotentialPositions(data);
        for ( var i = 0; i < data.length; ++i)
            that.showPotentialCells(new Position(data[i][0], data[i][1]));
    });

    // TODO send a single game state object    
    Client.protocol.register("end", function(data) {
        that.audioManager.play("../audio/game-is-over.mp3");
        that.potentialPositions = null; // TODO clear();
        that.selectedPositions.clear();
        $(that.board).attr("style", "opacity:0.33;");
    });

    // TODO send a single game state object
    Client.protocol.register("score", function(data) {
        $("#available-pieces").html('');
        //$("#available-pieces").hide();        
        $("#left").attr("style", "width:0");
        $("#remaining-pieces").html('');
        var k = 0;
        for ( var color in Colors) {
            var array = JSON.parse(data[k]);
            for ( var i = 0; i < array.length; ++i) {
                var retrievedObject = localStorage.getItem(getLocalStoreKey(color, "piece" + array[i]));
                var image = new Image();
                image.src = retrievedObject;
                image.setAttribute("style", "width:55px; height:55px;");
                $("#remaining-pieces").append(image);
            }
            ++k;
        }
        $("#remaining-pieces").show();        
    });

    
    Client.protocol.register("link", function(data) {
        sessionStorage.setItem("blockplus.network.hashcode", JSON.stringify(data));
    });

};

Game.prototype = {

    constructor : Game,

    showPotentialCells : function(position) {
        var context = this.boardRendering.getContext();
        var color = Colors[this.currentColor];
        context.globalAlpha = 0.4;
        context.fillStyle = color;
        context.beginPath();
        context.arc(34 * position.getColumn() + 34 / 2, 34 * position.getRow() + 34 / 2, 7, 0, Math.PI * 2, true);
        context.closePath();
        context.fill();
        context.globalAlpha = 0.8;
        context.lineWidth = 2;
        context.strokeStyle = color;
        context.stroke();
        context.globalAlpha = 1;
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
            if (that.potentialPositions.match(position)) {
                if (that.selectedPositions.contains(position)) {
                    that.boardRendering.updateCell(position, "White");
                    that.selectedPositions.remove(position);
                    that.showPotentialCells(position);
                } else {
                    that.boardRendering.getContext().globalAlpha = 0.5;
                    that.boardRendering.updateCell(position, Colors[that.currentColor]);
                    that.boardRendering.getContext().globalAlpha = 1;
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