Event.observe(window, 'load', function() {

    var onMessage = function(event) {
        myProtocol.handle(event.data);
    };

    var init = function(ws) {
        ws.onmessage = onMessage;
    };

    var myProtocol = new Protocol();

    var say = function(message) {
        myWebSocket.send(JSON.stringify(message));
    };

    var login = function(name) {
        var message = {
            type : 'Client',
            data : {
                name : name
            }
        };
        say(message);
    };

    var showRoom = function(ordinal) {
        var message = {
            type : 'ShowRoom',
            data : {
                ordinal : ordinal
            }
        };
        say(message);
    };

    var myWebSocket = new WebSocket("ws://artefact.hd.free.fr/talk/tome");
    
    var drawRoomNumber = function(context, ordinal) {
        
        /*
        context.fillStyle = "rgba(0,0,0,0.350)";
        context.fillRect(0, 0, 200, 200);
        */
        
        /*
        context.fillStyle = "rgba(0,0,0,0.4)";
        context.beginPath();
        context.arc(100, 100, 50, 0, Math.PI * 2, true);
        context.closePath();
        context.fill();
        context.lineWidth = 4;
        context.strokeStyle = "rgba(255,255,255,1)";
        context.stroke();
        */
        
        context.fillStyle = "rgba(255,255,255,0.90)";
        context.textAlign = "center";
        context.font = "bold 425% sans-serif";
        context.fillText(ordinal, 100, 122);            
        context.strokeStyle = "rgba(255,255,255,0.45)";
        context.lineWidth = 4;
        context.strokeText("" + ordinal, 100, 122);
    };

    myProtocol.register("rooms", function(data) {
        $("login").hide();
        $("rooms").show();
        var rooms = JSON.parse(data); // TODO à revoir coté serveur
        for ( var i = 0; i < rooms.length; ++i) {
            var canvas = document.createElement("canvas");
            var ordinal = rooms[i];
            canvas.setAttribute("id", "room" + ordinal);
            canvas.setAttribute("width", "200px");
            canvas.setAttribute("height", "200px");
            $("rooms").appendChild(canvas);
            var context = canvas.getContext("2d");
            
            context.fillStyle = "rgba(0,0,0,0.350)";
            context.fillRect(0, 0, 200, 200);
            
            drawRoomNumber(context, ordinal);
            showRoom(ordinal);
        }
    });

    myProtocol.register("room", function(data) {
        var ordinal = data.room;
        var canvas = $('room' + ordinal);
        //var boardRendering = new BoardRendering(new CellRendering(canvas, 10, 10, 9.25, 9.25));
        var boardRendering = new BoardRendering(new CellRendering(canvas, 10, 10, 9.5, 9.5));
        boardRendering.clear("#2a2d30"); // TODO à revoir
        var board = JSON.parse(data.board); // TODO à revoir coté serveur
        boardRendering.update(board);
        var context = boardRendering.getContext();
        //drawRoomNumber(context, ordinal);
       });

    init(myWebSocket);

    $("user").observe("change", function(event) {
        login($("user").value);
    });

    $("user").focus();

});