$(document).ready(function() {

    // TODO extract from url
    var location = "ws://artefact.hd.free.fr/talk/tome";

    Client.protocol.register("info", function(data) {
        console.log(data);
    });

    Client.protocol.register("welcome", function(data) {
        // var h1 = document.createElement("h1");
        // h1.innerHTML = data;
        // $("body").append(h1);
    });

    // TODO extract class
    var connection = function(name) {
        var object = {
            type : 'Client',
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
                $(canvas).click(function() {
                    var roomConnection = function(ordinal) {
                        var message = {
                            type : 'RoomConnection',
                            data : {
                                ordinal : ordinal
                            }
                        };
                        return message;
                    };
                    Client.protocol.register("enterRoom", function(data) {
                        alert("You are now in room " + data + ".");
                        Client.protocol.register("exitRoom", function(data) {
                            alert("You are now out of room " + data + ".");
                        });
                        
                        myGame = new Game(client);
                        myGame.bigMess();
                    });
                    Client.protocol.register("fullRoom", function(data) {
                        alert("There is no room left for you in room " + data + ": this room is full.");
                    });
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