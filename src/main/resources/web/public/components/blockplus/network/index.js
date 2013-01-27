$(document).ready(function() {

    // TODO extract from url
    var location = "ws://artefact.hd.free.fr/talk/tome";

    Client.protocol.register("info", function(data) {
        console.log(data);
    });

    Client.protocol.register("welcome", function(data) {
        //var h1 = document.createElement("h1");
        //h1.innerHTML = data;
        //$("body").append(h1);
    });

    var connection = function(name) {
        var object = {
            type : 'Client', // TODO Connection
            data : {
                name : name
            }
        };
        return object;
    };

    Client.message = connection; // TODO à revoir

    $("#connect").click(function(event) {

        var name = $("#user").val();

        var client = new Client(name, location);
        client.start(client.join);

        var drawRoomNumber = function(context, ordinal) {
            context.fillStyle = "rgba(255,255,255,0.90)";
            context.textAlign = "center";
            context.font = "bold 425% sans-serif";
            context.fillText(ordinal, 100, 122);
            context.strokeStyle = "rgba(255,255,255,0.45)";
            context.lineWidth = 4;
            context.strokeText("" + ordinal, 100, 122);
        };

        Client.protocol.register("rooms", function(data) {

            $("#rooms").show();

            var rooms = JSON.parse(data); // TODO à revoir coté serveur
            for ( var i = 0; i < rooms.length; ++i) {

                var canvas = document.createElement("canvas");
                var ordinal = rooms[i];

                canvas.setAttribute("id", "room-" + ordinal);
                canvas.setAttribute("width", "200px");
                canvas.setAttribute("height", "200px");
                $("#rooms").append(canvas);

                var context = canvas.getContext("2d");
                context.fillStyle = "rgba(0,0,0,0.350)";
                context.fillRect(0, 0, 200, 200);
                drawRoomNumber(context, ordinal);

                client.say(showRoom(ordinal));

                var roomConnection = function(ordinal) {
                    var message = {
                        type : 'RoomConnection',
                        data : {
                            ordinal : ordinal
                        }
                    };
                    return message;
                };

                $(canvas).click(function() {

                    $("#rooms").hide();
                    $("#game").show();

                    Client.protocol.register("color", function(data) {
                        currentColor = data; // TODO avoir un objet game
                    });

                    Client.protocol.register("pieces", function(data) {
                        $("#available-pieces").html('');
                        for ( var i = 1; i <= 21; ++i) { // TODO
                            var key = getLocalStoreKey(currentColor, "piece" + i);
                            var retrievedObject = localStorage.getItem(key);
                            var image = new Image();
                            image.setAttribute("id", "piece-" + i);
                            image.src = retrievedObject;
                            image.setAttribute("class", "not-available");
                            $("#available-pieces").append(image);
                        }
                        var array = data;
                        for ( var i = 1; i <= 21; ++i) { // TODO
                            $("#piece-" + i).attr("class", "not-available");
                        }
                        for ( var i = 0; i < array.length; ++i) {
                            $("#piece-" + array[i]).attr("class", "available");
                        }
                        $("#available-pieces").show();
                    });

                    Client.protocol.register("board", function(data) {
                        boardRendering.update(data);
                    });

                    Client.protocol.register("options", function(data) {
                        option = new Options(data); // TODO
                        console.log(data);
                    });

                    Client.protocol.register("potential", function(data) {
                        selectedPositions.clear();
                        potentialPositions = new PotentialPositions(data);
                        for ( var i = 0; i < data.length; ++i) {
                            var position = new Position(data[i][0], data[i][1]);
                            showPotentialCells(position);
                        }
                    });

                    Client.protocol.register("link", function(data) {
                        console.log(data);
                        sessionStorage.setItem("blockplus.network.hashcode", JSON.stringify(data));
                    });

                    // TODO extract object
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

                    // TODO extract object
                    var roomReconnection = function(data) {
                        var message = {
                            type : 'RoomReconnection',
                            data : {
                                link : data
                            }
                        };
                        say(message);
                    };

                    var potentialCellClickEventHandler = function(event) {

                        event.preventDefault();

                        var position = offsetToPositionBuilder.build(event.offsetX, event.offsetY);
                        if (potentialPositions.match(position)) {
                            if (selectedPositions.contains(position)) {
                                boardRendering.updateCell(position, "White");
                                selectedPositions.remove(position);
                                showPotentialCells(position);
                            } else {
                                boardRendering.getContext().globalAlpha = 0.5;
                                boardRendering.updateCell(position, Colors[currentColor]);
                                boardRendering.getContext().globalAlpha = 1;
                                selectedPositions.add(position);
                            }
                            var matches = option.matches(selectedPositions);
                            for ( var i = 1; i <= 21; ++i) {
                                $(("#piece-" + i)).attr("class", "not-available");
                            }
                            var hasPotential = false;
                            for ( var id in matches) {
                                hasPotential = true;
                                $(("#piece-" + id)).attr("class", "available");
                            }
                            if (!hasPotential) {
                                audioManager.play("../audio/none.mp3");
                            }
                            var id = option.perfectMatch(selectedPositions);
                            if (id) {
                                $("#submitPiece").show();
                                audioManager.play("../audio/subtle.mp3");
                                $("#piece-" + id).attr("class", "perfect-match");
                                var topLeft = selectedPositions.getTopLeftPosition();
                                var bottomRight = selectedPositions.getBottomRightPosition();
                                var copy = function(topLeft, bottomRight) {
                                    var width = 2 + 1 + bottomRight.column - topLeft.column;
                                    var height = 2 + 1 + bottomRight.row - topLeft.row;
                                    var newCanvas = document.getElementById("pieceToPlay");
                                    newCanvas.width = 34 * width;
                                    newCanvas.height = 34 * height;
                                    var tmpBoardRendering = new BoardRendering(new CellRendering(newCanvas, 34, 34, 33, 33));
                                    var positions = selectedPositions.get();
                                    tmpBoardRendering.clear("#2a2d30");
                                    for ( var position in positions) {
                                        var p = JSON.parse(position); // TODO
                                        tmpBoardRendering.updateCell(new Position(1 + p.row - topLeft.row, 1 + p.column - topLeft.column), currentColor);
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
                    $("#pieceToPlay").click(function(event) {
                        $("#submitPiece").hide();
                        var pieceId = option.perfectMatch(selectedPositions);
                        var data = [];
                        for ( var position in selectedPositions.get()) {
                            var p = JSON.parse([ position ]);
                            data.push([ p.row, p.column ]);
                        }
                        client.say(moveSubmit(pieceId, data)); // TODO ? se contenter des positions
                    });
                    /*--------------------------------------------------8<--------------------------------------------------*/
                    var showPotentialCells = function(position) {
                        var context = boardRendering.getContext();
                        context.globalAlpha = 0.4;
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
                    var board = document.getElementById("board");
                    var boardRendering = new BoardRendering(new CellRendering(board, 34, 34, 33, 33));
                    var selectedPositions = new SelectedPositions();
                    var audioManager = new AudioManager(new Audio());
                    /*--------------------------------------------------8<--------------------------------------------------*/
                    $(boardRendering.getCanvas()).click(potentialCellClickEventHandler);
                    /*--------------------------------------------------8<--------------------------------------------------*/
                    // localStorage.clear();
                    var piece = document.getElementById("piece");
                    createAllPiecesImages("/xml/pieces.xml", new BoardRendering(new CellRendering(piece, 13, 13, 12, 12)));
                    /*--------------------------------------------------8<--------------------------------------------------*/

                    var id = this.getAttribute("id");
                    var n = id.substr(id.indexOf("-") + 1);
                    client.say(roomConnection(n));
                });

            }

        });

        Client.protocol.register("room", function(data) {
            var ordinal = data.room;
            var canvas = document.getElementById('room-' + ordinal);
            var boardRendering = new BoardRendering(new CellRendering(canvas, 10, 10, 9.5, 9.5));
            boardRendering.clear("#2a2d30"); // TODO à revoir
            var board = JSON.parse(data.board); // TODO à revoir coté serveur
            boardRendering.update(board);
        });

        var showRoom = function(ordinal) {
            var object = {
                type : 'ShowRoom',
                data : {
                    ordinal : ordinal
                }
            };
            return object;
        };

    });

    $('#connection').modal('show');
    $("#user").focus();

});