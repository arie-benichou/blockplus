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

        var drawGameNumber = function(context, ordinal) {
            context.fillStyle = "rgba(255,255,255,0.90)";
            context.textAlign = "center";
            context.font = "bold 425% sans-serif";
            context.fillText(ordinal, 100, 122);
            context.strokeStyle = "rgba(255,255,255,0.45)";
            context.lineWidth = 4;
            context.strokeText("" + ordinal, 100, 122);
        };

        Client.protocol.register("games", function(data) {
            $("#games").show();
            var games = JSON.parse(data); // TODO à revoir coté serveur
            for ( var i = 0; i < games.length; ++i) {
                var canvas = document.createElement("canvas");
                var ordinal = games[i];
                canvas.setAttribute("id", "game-" + ordinal);
                canvas.setAttribute("width", "200px");
                canvas.setAttribute("height", "200px");
                $("#games").append(canvas);
                var context = canvas.getContext("2d");
                context.fillStyle = "rgba(0,0,0,0.350)";
                context.fillRect(0, 0, 200, 200);
                drawGameNumber(context, ordinal);
                client.say(showGame(ordinal));
                $(canvas).click(function() {
                    var gameConnection = function(ordinal) {
                        var message = {
                            type : 'GameConnection',
                            data : {
                                ordinal : ordinal
                            }
                        };
                        return message;
                    };
                    Client.protocol.register("enterGame", function(data) {
                        // TODO ...
                        Client.protocol.register("exitGame", function(data) {
                            // TODO ...
                        });
                        myGame = new Game(client);
                        myGame.bigMess();
                    });
                    var id = this.getAttribute("id");
                    var n = id.substr(id.indexOf("-") + 1);
                    client.say(gameConnection(n));
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

        Client.protocol.register("game", function(data) {
            var ordinal = data.game;
            var canvas = document.getElementById('game-' + ordinal);
            var boardRendering = new BoardRendering(new CellRendering(canvas, 10, 10, 9.5, 9.5));
            boardRendering.clear("#2a2d30"); // TODO à revoir
            boardRendering.update(new Board(JSON.parse(data.board)));
        });

        var showGame = function(ordinal) {
            var object = {
                type : 'ShowGame',
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