room = 0;
Event.observe(window, 'load', function() {
    /*--------------------------------------------------8<--------------------------------------------------*/
    var audioManager = new AudioManager(new Audio());
    // audioManager.play("./audio/none.mp3");
    // audioManager.play("./audio/subtle.mp3");
    // audioManager.play("./audio/vector.mp3");
    /*--------------------------------------------------8<--------------------------------------------------*/
    var messsageEventHandler = function(event) {
        var newChild = document.createElement("div");
        newChild.setAttribute("id", "last-message");
        newChild.innerHTML = JSON.parse(event.data);
        $("last-message").remove();
        $("messages").appendChild(newChild);

        // TODO define json structure for a game representation
        currentColor = JSON.parse(event.data)[0][0];
    };
    var gameoverEventHandler = function(event) {
        $("last-message").innerHTML = "";
        audioManager.play("./audio/game-is-over.mp3");
        // $("game-is-over").play();
        event.target.close();
        boardRendering.update(JSON.parse(event.data));
        getAvailablePieces();
        $("board").setAttribute("style", "opacity:0.33;");
        $("play-again").show();
        $("left").setAttribute("style", "width:0");
        $("available-pieces").hide();

        var getAvailablePiecesByColor = function(color) {
            new Ajax.Request("/blockplus/game-room/" + room + "/game-state-pieces-color", {
                onSuccess : function(response) {
                    var array = JSON.parse(response.responseText);
                    console.log(array.length);
                    // $(color).innerHTML = "";
                    for ( var i = 0; i < array.length; ++i) {
                        var retrievedObject = localStorage.getItem(getLocalStoreKey(color, "piece" + array[i]));
                        console.log(getLocalStoreKey(color, "piece" + array[i]));
                        // console.log(retrievedObject);
                        var image = new Image();
                        // image.setAttribute("id", "piece-" + i);
                        image.src = retrievedObject;
                        // image.width = "45px";
                        // image.height = "45px;";
                        console.log(image);
                        image.setAttribute("style", "width:55px; height:55px;");
                        // document.body.appendChild(image);
                        // $(color).appendChild(image);
                        $("remaining-pieces").appendChild(image);
                    }
                },
                onFailure : function(response) {
                    alert("failed!");
                },
                method : 'get',
                parameters : {
                    color : color
                }
            });
        };

        $("remaining-pieces").innerHTML = "";
        for ( var color in Colors) {
            console.log(color);
            getAvailablePiecesByColor(color);
        }

        $("remaining-pieces").show();
    };
    var optionsEventHandler = function(event) {
        getAvailablePieces(); // TODO
        var array = JSON.parse(event.data);
        if (array == null || array.length == 0) {
            new Ajax.Request("/blockplus/game-room/" + room + "/game-null-move", {
                onSuccess : function(response) {
                    console.log("you have no more legal move");
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

        event.preventDefault();

        var position = offsetToPositionBuilder.build(event.offsetX, event.offsetY);
        if (potentialPositions.match(position)) {
            if (selectedPositions.contains(position)) {
                boardRendering.updateCell(position, "White");
                selectedPositions.remove(position);
                showPotentialCells(position);
            } else {
                /*
                 * boardRendering.getContext().globalAlpha = 0.55;
                 * boardRendering.updateCell(position, "black");
                 */
                boardRendering.getContext().globalAlpha = 0.5;
                boardRendering.updateCell(position, Colors[currentColor]);
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
                $("submit").show();
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
                    // tmpBoardRendering.clear("#a0a6ab");
                    tmpBoardRendering.clear("#2a2d30");
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
    $("pieceToPlay").addEventListener("click", function(event) {
        var pieceId = option.perfectMatch(selectedPositions);
        var data = [];
        for ( var position in selectedPositions.get()) {
            var p = JSON.parse([ position ]);
            data.push([ p.row, p.column ]);
        }
        moveSubmit(pieceId, data); // TODO ? se contenter des positions
    }, false);
    /*--------------------------------------------------8<--------------------------------------------------*/
    var showPotentialCells = function(position) {
        var context = boardRendering.getContext();
        context.globalAlpha = 0.4;
        // context.fillStyle = "rgba(0, 128, 0, 0.35)";
        context.fillStyle = Colors[currentColor];
        context.beginPath();
        context.arc(34 * position.getColumn() + 34 / 2, 34 * position.getRow() + 34 / 2, 7, 0, Math.PI * 2, true);
        context.closePath();
        context.fill();
        context.globalAlpha = 0.8;
        context.lineWidth = 2;
        context.strokeStyle = Colors[currentColor];
        context.stroke();
        context.globalAlpha = 1;
    };
    /*--------------------------------------------------8<--------------------------------------------------*/
    var offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);
    var boardRendering = new BoardRendering(new CellRendering("board", 34, 34, 33, 33));
    var selectedPositions = new SelectedPositions();
    /*--------------------------------------------------8<--------------------------------------------------*/
    boardRendering.getCanvas().addEventListener("click", potentialCellClickEventHandler, false);
    /*--------------------------------------------------8<--------------------------------------------------*/
    localStorage.clear();
    createAllPiecesImages("/pieces.xml", new BoardRendering(new CellRendering("piece", 13, 13, 12, 12)));
    /*--------------------------------------------------8<--------------------------------------------------*/
    $("remaining-pieces").hide(); // TODO
    /*--------------------------------------------------8<--------------------------------------------------*/
    $("play-again").observe('click', function(event) {
        new Ajax.Request("/blockplus/game-room/" + room + "/game-reset", {
            onSuccess : function(response) {
                source.connect();
                // TODO utiliser des classes css
                $("left").setAttribute("style", "width:303px;");
                $("board").setAttribute("style", "opacity:1;");
                $("play-again").hide();
                $("remaining-pieces").hide();
                audioManager.pause();
            },
            onFailure : function(response) {
                alert("failed");
            },
            method : 'get',
        });
    });
    $("play-again").hide(); // TODO
    $("submit").hide(); // TODO
    /*--------------------------------------------------8<--------------------------------------------------*/
    myWebSocket = new WebSocket("ws://localhost:8181/talk/tome");
    /*--------------------------------------------------8<--------------------------------------------------*/
    
    var myProtocol = new Protocol();

    var infoEventHandler = function(data) {
        var newChild = document.createElement("div");
        newChild.setAttribute("id", "last-message");
        newChild.innerHTML = data;
        $("last-message").remove();
        $("messages").appendChild(newChild);
    };

    myProtocol.register("info", infoEventHandler);

    myProtocol.register("rooms", function(data) {
        var room = $("room");
        var rooms = JSON.parse(data);
        var n = rooms.length;
        for ( var i = 0; i < n; ++i) {
            var current = rooms[i];
            var option = document.createElement("option");
            option.setAttribute("value", current);
            option.innerHTML = current;
            room.appendChild(option);
        }
    });

    myProtocol.register("color", function(data) {
        currentColor = data; // TODO
        $("user").hide(); // TODO à revoir
    });

    myProtocol.register("pieces", function(data) {
        $("available-pieces").innerHTML = "";
        for ( var i = 1; i <= 21; ++i) { // TODO
            var retrievedObject = localStorage.getItem(getLocalStoreKey(currentColor, "piece" + i));
            var image = new Image();
            image.setAttribute("id", "piece-" + i);
            image.src = retrievedObject;
            image.setAttribute("class", "not-available");
            $("available-pieces").appendChild(image);
        }
        var array = data;
        for ( var i = 1; i <= 21; ++i) { // TODO
            $("piece-" + i).setAttribute("class", "not-available");
        }
        for ( var i = 0; i < array.length; ++i) {
            $("piece-" + array[i]).setAttribute("class", "available");
        }
        $("available-pieces").show();
    });

    myProtocol.register("board", function(data) {
        boardRendering.update(data);
    });

    myProtocol.register("options", function(data) {
        option = new Options(data); // TODO
        console.log(data);
    });

    myProtocol.register("potential", function(data) {
        
        // window.focus();
        //alert(currentColor);
        
        selectedPositions.clear();
        potentialPositions = new PotentialPositions(data);
        for ( var i = 0; i < data.length; ++i) {
            var position = new Position(data[i][0], data[i][1]);
            showPotentialCells(position);
        }
    });

    myProtocol.register("link", function(data) {
        console.log(data);
        sessionStorage.setItem("blockplus.network.hashcode", JSON.stringify(data));
    });

    /*--------------------------------------------------8<--------------------------------------------------*/

    var onMessage = function(event) {
        myProtocol.handle(event.data);
    };

    var onOpen = function(event) {
        console.log("Channel has been open: " + new Date());
        var link = sessionStorage.getItem("blockplus.network.hashcode");
        if(link != null) roomReconnection(JSON.parse(link));
    };

    var onClose = function(event) {
        console.log("Channel has been closed: " + new Date());
        myWebSocket = new WebSocket("ws://localhost:8181/talk/tome");
        init(myWebSocket);
    };

    var init = function(ws) {
        ws.onmessage = onMessage;
        ws.onopen = onOpen;
        ws.onclose = onClose;
    };

    /*
     * var showMessage = function(message) { var div =
     * document.createElement('div'); div.innerHTML = '>' + message;
     * $("io").appendChild(div); };
     */

    var say = function(message) {
        myWebSocket.send(JSON.stringify(message));
    };
    
    /*--------------------------------------------------8<--------------------------------------------------*/

    var login = function(name) {
        var message = {
            type : 'Client',
            data : {
                name : name
            }
        };
        say(message);
    };

    var roomConnection = function(ordinal) {
        var message = {
            type : 'RoomConnection',
            data : {
                ordinal : ordinal
            }
        };
        say(message);
    };
    
    var moveSubmit = function(id, positions) {
        var message = {
            type : 'MoveSubmit',
            data : {
                id : id,
                positions : positions
            }
        };
        console.log(message);
        say(message);
    };    

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
    $("user").observe("change", function(event) {
        login($("user").value);
        $("user").hide();
        $("room").show();
    });

    $("room").observe("change", function(event) {
        roomConnection($("room").value);
        $("room").hide();
    });
    /*--------------------------------------------------8<--------------------------------------------------*/
    $("user").focus();
    /*--------------------------------------------------8<--------------------------------------------------*/
    init(myWebSocket);
    /*--------------------------------------------------8<--------------------------------------------------*/
});