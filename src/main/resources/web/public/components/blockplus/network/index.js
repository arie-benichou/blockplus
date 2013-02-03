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

var main = function() {

    var computeLocation = function(suffix) {
        return document.location.toString().replace('http://', 'ws://').replace('https://', 'wss://') + suffix;
    };

    var location = computeLocation("io");

    // TODO à revoir
    Client.protocol.register("info", function(data) {
        console.log(data);
        var to = function() {
            $("#alert").hide();
        };
        window.clearTimeout(to);
        $("#notification").html(data);
        $("#alert").show();
        $("#alert").alert();
        window.setTimeout(to, 1000 * 3);
    });

    Client.protocol.register("welcome", function(data) {
        // TODO
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

        // TODO extract method
        // TODO sanitize on server side
        var input = $("#user").val();
        if (input == "")
            return false;
        var pattern = new RegExp("[<&?#'\"= >:;,/!*.\{\}\\]\\[]", "g");
        var name = input.replace(pattern, "").substring(0, 26);
        $("#user").val(name);
        if (name != input)
            return false;

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
                        // TODO ...
                        Client.protocol.register("exitRoom", function(data) {
                            // TODO ...
                        });
                        myGame = new Game(client);
                        myGame.bigMess();
                    });
                    var id = this.getAttribute("id");
                    var n = id.substr(id.indexOf("-") + 1);
                    client.say(roomConnection(n));
                });

            }

            // TODO extract class
            var feedback = function(name, content) {
                var object = {
                    type : 'Feedback',
                    data : {
                        name : name,
                        content : content
                    }
                };
                return object;
            };
            $('#feedback').show();
            $('#feedback-dialog').on('shown', function() {
                $('#feedback-content').focus();
            });
            $('#send-feedback').click(function() {
                var content = $('#feedback-content').val();
                client.say(feedback(client.name, content));
            });
        });

        Client.protocol.register("room", function(data) {
            var ordinal = data.room;
            var canvas = document.getElementById('room-' + ordinal);
            var boardRendering = new BoardRendering(new CellRendering(canvas, 10, 10, 9.5, 9.5));
            boardRendering.clear("#2a2d30"); // TODO à revoir
            boardRendering.update(new Board(JSON.parse(data.board)));
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

    $('#connection').on('shown', function() {
        $('#user').focus();
    });

    $('#connection').modal('show');

};