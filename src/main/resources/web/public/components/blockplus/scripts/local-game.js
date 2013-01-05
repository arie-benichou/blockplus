Event.observe(window, 'load', function() {
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
    };
    var gamenotoverEventHandler = function(event) {
        boardRendering.update(JSON.parse(event.data));
        $("board").className = "game-is-not-over";
    };
    var gameoverEventHandler = function(event) {
        audio.src = "./audio/game-is-over.mp3";
        audio.play();
        event.target.close();
        boardRendering.update(JSON.parse(event.data));
        getAvailablePieces();
        $("content").setAttribute("style", "opacity:0.4;");
    };
    var optionsEventHandler = function(event) {
        var array = JSON.parse(event.data);
        if (array == null || array.length == 0) {
        } else {
            source.disconnect();
            for ( var i = 0; i < array.length; ++i) {
                var position = new Position(array[i][0], array[i][1]);
                showPotentialCells(position);
            }
            getAvailablePieces();
            new Ajax.Request("/blockplus/options", {
                onSuccess : function(response) {
                    option = new Options(JSON.parse(response.responseText));
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
        if (event.ctrlKey)
            window.location = event.srcElement.toDataURL("image/png");
        else {
            var position = offsetToPositionBuilder.build(event.offsetX, event.offsetY);
            if (selectedPositions.contains(position)) {
                boardRendering.updateCell(position, "White");
                selectedPositions.remove(position);
                showPotentialCells(position);
            } else {
                boardRendering.updateCell(position, "black");
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
                audio.src = "./audio/none.mp3";
                audio.play();
            }
            var id = option.perfectMatch(selectedPositions);
            if (id) {

                $("pieceToPlay").show();

                audio.src = "./audio/subtle.mp3";
                audio.play();

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
                        tmpBoardRendering.updateCell(new Position(1 + p.row - topLeft.row, 1 + p.column - topLeft.column), "Green");
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
        new Ajax.Request("/blockplus/submit", {
            onSuccess : function(response) {
                audio.src = "./audio/vector.mp3";
                audio.play();
                $("pieceToPlay").hide();
                selectedPositions.clear();
                getAvailablePieces();
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
        new Ajax.Request("/blockplus/pieces", {
            onSuccess : function(response) {
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
        context.fillStyle = "rgba(0, 128, 0, 0.35)";
        context.beginPath();
        context.arc(34 * position.getColumn() + 34 / 2, 34 * position.getRow() + 34 / 2, 4, 0, Math.PI * 2, true);
        context.closePath();
        context.fill();
        context.lineWidth = 1;
        context.strokeStyle = "green";
        context.stroke();
    };
    /*--------------------------------------------------8<--------------------------------------------------*/
    var offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);
    var boardRendering = new BoardRendering(new CellRendering("board", 34, 34, 33, 33));
    var selectedPositions = new SelectedPositions();
    /*--------------------------------------------------8<--------------------------------------------------*/
    var source = new EventSourceManager("/blockplus/data");
    source.addEventListener("open", openEventHandler, false);
    source.addEventListener("error", errorEventHandler, false);
    source.addEventListener("message", messsageEventHandler, false);
    source.addEventListener("gamenotover", gamenotoverEventHandler, false);
    source.addEventListener("gameover", gameoverEventHandler, false);
    source.addEventListener("options", optionsEventHandler, false);
    /*--------------------------------------------------8<--------------------------------------------------*/
    boardRendering.getCanvas().addEventListener("click", potentialCellClickEventHandler, false);
    /*--------------------------------------------------8<--------------------------------------------------*/
    var audio = new Audio();
    audio.src = "./audio/none.mp3";
    audio.play();
    audio.src = "./audio/subtle.mp3";
    audio.play();
    audio.src = "./audio/vector.mp3";
    audio.play();
    /*--------------------------------------------------8<--------------------------------------------------*/
    localStorage.clear();
    createAllPiecesImages("/pieces.xml", new BoardRendering(new CellRendering("piece", 12, 12, 11, 11)));
    /*--------------------------------------------------8<--------------------------------------------------*/
    source.connect();
    /*--------------------------------------------------8<--------------------------------------------------*/    
});