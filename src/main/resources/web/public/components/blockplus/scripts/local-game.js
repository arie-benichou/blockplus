Event.observe(window, 'load', function() {
    /*--------------------------------------------------8<--------------------------------------------------*/
    var audioManager = new AudioManager(new Audio());
    audioManager.play("./audio/none.mp3");
    audioManager.play("./audio/subtle.mp3");
    audioManager.play("./audio/vector.mp3");
    /*--------------------------------------------------8<--------------------------------------------------*/
    var openEventHandler = function(event) {
    };
    var errorEventHandler = function(event) {
        if (event.readyState == EventSource.CLOSED)
            console.log("Event handling error");
    };
    var messsageEventHandler = function(event) {
        var newChild = document.createElement("div");
        newChild.setAttribute("id", "last-message");
        newChild.innerHTML = JSON.parse(event.data);
        $("last-message").remove();
        $("messages").appendChild(newChild);
        
        // TODO define json structure for a game representation
        currentColor = JSON.parse(event.data)[0][0];
    };
    var gamenotoverEventHandler = function(event) {
        boardRendering.update(JSON.parse(event.data));
        $("board").className = "game-is-not-over";
    };
    var gameoverEventHandler = function(event) {
        audioManager.play("./audio/game-is-over.mp3");
        // $("game-is-over").play();
        event.target.close();
        boardRendering.update(JSON.parse(event.data));
        getAvailablePieces();
        $("board").setAttribute("style", "opacity:0.33;");
        $("play-again").show();
    };
    var optionsEventHandler = function(event) {
        
        getAvailablePieces(); // TODO
        
        var array = JSON.parse(event.data);
        if (array == null || array.length == 0) {
            new Ajax.Request("/blockplus/game-null-move", {
                onSuccess : function(response) {
                    console.log("you have no more legal move");
                },
                onFailure : function(response) {
                    alert("failed!");
                },
                method : 'get',
            });
        } else {
            source.disconnect();

            potentialPositions = new PotentialPositions(array); // TODO

            for ( var i = 0; i < array.length; ++i) {
                var position = new Position(array[i][0], array[i][1]);
                showPotentialCells(position);
            }

            new Ajax.Request("/blockplus/game-options", {
                onSuccess : function(response) {
                    option = new Options(JSON.parse(response.responseText)); // TODO
                    audioManager.play("./audio/vector.mp3");
                    console.log("!");
                },
                onFailure : function(response) {
                    alert("failed!");
                },
                method : 'get',
            });
        }
    };
    /*--------------------------------------------------8<--------------------------------------------------*/
    var potentialCellClickEventHandler = function(event) {
        var position = offsetToPositionBuilder.build(event.offsetX, event.offsetY);
        if (potentialPositions.match(position)) {
            if (selectedPositions.contains(position)) {
                boardRendering.updateCell(position, "White");
                selectedPositions.remove(position);
                showPotentialCells(position);
            } else {
                boardRendering.getContext().globalAlpha = 0.55;
                boardRendering.updateCell(position, "black");
                boardRendering.getContext().globalAlpha = 1;                
                selectedPositions.add(position);
            }
            var matches = option.matches(selectedPositions);
            for ( var i = 1; i <= 21; ++i) {
                $(("piece-" + i)).setAttribute("class", "not-available");
            }
            var hasPotential = false;
            for ( var id in matches) {
                hasPotential = true;
                $(("piece-" + id)).setAttribute("class", "available");
            }
            if (!hasPotential) {
                audioManager.play("./audio/none.mp3");
            }
            var id = option.perfectMatch(selectedPositions);
            if (id) {
                $("pieceToPlay").show();
                audioManager.play("./audio/subtle.mp3");
                $("piece-" + id).setAttribute("class", "perfect-match");
                var topLeft = selectedPositions.getTopLeftPosition();
                var bottomRight = selectedPositions.getBottomRightPosition();
                var copy = function(topLeft, bottomRight) {
                    var width = 2 + 1 + bottomRight.column - topLeft.column;
                    var height = 2 + 1 + bottomRight.row - topLeft.row;
                    var newCanvas = $("pieceToPlay");
                    newCanvas.width = 34 * width;
                    newCanvas.height = 34 * height;
                    var tmpBoardRendering = new BoardRendering(new CellRendering(newCanvas, 34, 34, 33, 33));
                    var positions = selectedPositions.get();
                    for ( var position in positions) {
                        var p = JSON.parse(position); // TODO !!
                        tmpBoardRendering.updateCell(new Position(1 + p.row - topLeft.row, 1 + p.column - topLeft.column), currentColor);
                    }
                };
                copy(topLeft, bottomRight);
                $("pieceToPlay").setAttribute("class", "opaque out");
            } else {
                $("pieceToPlay").setAttribute("class", "transparent out");
            }
        }
    };
    /*--------------------------------------------------8<--------------------------------------------------*/
    $("pieceToPlay").addEventListener("mouseover", function(event) {
        $("pieceToPlay").setAttribute("class", "opaque over clickable");
    }, false);

    $("pieceToPlay").addEventListener("mouseout", function(event) {
        $("pieceToPlay").setAttribute("class", "opaque out clickable");
    }, false);

    $("pieceToPlay").addEventListener("click", function(event) {
        var pieceId = option.perfectMatch(selectedPositions);
        var data = [];
        for ( var position in selectedPositions.get()) {
            var p = JSON.parse([ position ]);
            data.push([ p.row, p.column ]);
        }
        new Ajax.Request("/blockplus/game-move", {
            onSuccess : function(response) {
                // audioManager.play("./audio/vector.mp3");
                $("pieceToPlay").hide();
                selectedPositions.clear();
                //getAvailablePieces();
                source.connect();
            },
            onFailure : function(response) {
                alert("failed!");
            },
            method : 'get',
            parameters : {
                id : pieceId,
                positions : JSON.stringify(data)
            }
        });

    }, false);
    /*--------------------------------------------------8<--------------------------------------------------*/
    var getAvailablePieces = function() {
        new Ajax.Request("/blockplus/game-state-pieces", {
            onSuccess : function(response) {
                
                $("available-pieces").innerHTML = "";
                
                //alert(currentColor);
                for ( var i = 1; i <= 21; ++i) { // TODO
                    var retrievedObject = localStorage.getItem(getLocalStoreKey(currentColor, "piece" + i));
                    var image = new Image();
                    image.setAttribute("id", "piece-" + i);
                    image.src = retrievedObject;
                    image.setAttribute("class", "not-available");
                    $("available-pieces").appendChild(image);
                }                            
                
                var array = JSON.parse(response.responseText);
                for ( var i = 1; i <= 21; ++i) { // TODO
                    $("piece-" + i).setAttribute("class", "not-available");
                }
                for ( var i = 0; i < array.length; ++i) {
                    $("piece-" + array[i]).setAttribute("class", "available");
                }
            },
            onFailure : function(response) {
                alert("failed!");
            },
            method : 'get',
        });
    };
    /*--------------------------------------------------8<--------------------------------------------------*/
    var showPotentialCells = function(position) {
        var context = boardRendering.getContext();
        
        context.globalAlpha = 0.35;
        //context.fillStyle = "rgba(0, 128, 0, 0.35)";
        context.fillStyle = currentColor;
        context.beginPath();
        context.arc(34 * position.getColumn() + 34 / 2, 34 * position.getRow() + 34 / 2, 4, 0, Math.PI * 2, true);
        context.closePath();
        context.fill();
        context.globalAlpha = 1;
        
        context.lineWidth = 1;
        context.strokeStyle = currentColor;
        context.stroke();
    };
    /*--------------------------------------------------8<--------------------------------------------------*/
    var offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);
    var boardRendering = new BoardRendering(new CellRendering("board", 34, 34, 33, 33));
    var selectedPositions = new SelectedPositions();
    /*--------------------------------------------------8<--------------------------------------------------*/
    var source = new EventSourceManager("/blockplus/game-state");
    source.addEventListener("open", openEventHandler, false);
    source.addEventListener("error", errorEventHandler, false);
    source.addEventListener("message", messsageEventHandler, false);
    source.addEventListener("gamenotover", gamenotoverEventHandler, false);
    source.addEventListener("gameover", gameoverEventHandler, false);
    source.addEventListener("options", optionsEventHandler, false);
    /*--------------------------------------------------8<--------------------------------------------------*/
    boardRendering.getCanvas().addEventListener("click", potentialCellClickEventHandler, false);
    /*--------------------------------------------------8<--------------------------------------------------*/
    localStorage.clear();
    createAllPiecesImages("/pieces.xml", new BoardRendering(new CellRendering("piece", 12, 12, 11, 11)));
    /*--------------------------------------------------8<--------------------------------------------------*/
    source.connect();
    /*--------------------------------------------------8<--------------------------------------------------*/    
    $("play-again").observe('click', function(event) {
        new Ajax.Request("/blockplus/game-reset", {
            onSuccess : function(response) {
                source.connect();
                // TODO utiliser des classes css
                $("play-again").hide();
                $("board").setAttribute("style", "opacity:1;");
                
            },
            onFailure : function(response) {
                alert("failed!");
            },
            method : 'get',
        });        
    });
    $("play-again").hide();
    /*--------------------------------------------------8<--------------------------------------------------*/
});