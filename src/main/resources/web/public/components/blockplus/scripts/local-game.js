// TODO ! check origin & integrity of messages
// TODO ! utiliser le local storage pour enregistrer le currentime de la musique, par exemple...
// TODO ? émettre l"événement newgame
/*--------------------------------------------------8<--------------------------------------------------*/
var offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);
var boardRendering = new BoardRendering(new CellRendering("board", 34, 34, 33, 33));
var selectedPositions = new SelectedPositions();
/*--------------------------------------------------8<--------------------------------------------------*/
var openEventHandler = function(event) {
    console.log("Event listening");
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
    if ($("game-is-not-over").currentTime == 0)
        $("game-is-not-over").play();
};
var gameoverEventHandler = function(event) {
    $("game-is-over").play();
    event.target.close();
    boardRendering.update(JSON.parse(event.data));
    // $("board").className = "game-is-over";
    $("game-is-not-over").pause();
    getAvailablePieces();
    $("content").setAttribute("style", "opacity:0.4;");
};

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

var optionsEventHandler = function(event) {
    var array = JSON.parse(event.data);

    console.log(array);
    if (array == null || array.length == 0) {
        //$("skip").show();
    }
    else {
        source.disconnect();
        for ( var i = 0; i < array.length; ++i) {
            var position = new Position(array[i][0], array[i][1]);
            showPotentialCells(position);
        }
        getAvailablePieces();
        new Ajax.Request("/blockplus/options", {
            onSuccess : function(response) {
                option = new Options(JSON.parse(response.responseText));
                if (option.get().length == 0)
                    alert("fuck");
                // $("submit").show();
            },
            onFailure : function(response) {
                alert("failed!");
            },
            method : 'get',
        });        
    }

};
/*--------------------------------------------------8<--------------------------------------------------*/
var source = new EventSourceManager("/blockplus/data");
source.addEventListener("open", openEventHandler, false);
source.addEventListener("error", errorEventHandler, false);
source.addEventListener("message", messsageEventHandler, false);
source.addEventListener("gamenotover", gamenotoverEventHandler, false);
source.addEventListener("gameover", gameoverEventHandler, false);
source.addEventListener("options", optionsEventHandler, false);
source.connect(); // TODO
/*--------------------------------------------------8<--------------------------------------------------*/

for ( var i = 1; i <= 21; ++i) {
    var retrievedObject = localStorage.getItem(getLocalStoreKey("Green", "piece" + i));
    console.log(retrievedObject);
    var image = new Image();
    image.setAttribute("id", "piece-" + i);
    image.src = retrievedObject;
    image.setAttribute("class", "not-available");
    $("available-pieces").appendChild(image);
    // image.addEventListener("click", mySubmit(), false);
}
/*--------------------------------------------------8<--------------------------------------------------*/
function showPotentialCells(position) {
    var context = boardRendering.getContext();
    context.fillStyle = "rgba(0, 128, 0, 0.35)";
    context.beginPath();
    context.arc(34 * position.getColumn() + 34 / 2, 34 * position.getRow() + 34 / 2, 4, 0, Math.PI * 2, true);
    context.closePath();
    context.fill();
    context.lineWidth = 1;
    context.strokeStyle = "green";
    context.stroke();
}
/*--------------------------------------------------8<--------------------------------------------------*/
boardRendering.getCanvas().addEventListener("click", function(event) {
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
        console.log(matches);
        for ( var i = 1; i <= 21; ++i) {
            $(("piece-" + i)).setAttribute("class", "not-available");
        }
        for ( var id in matches) {
            $(("piece-" + id)).setAttribute("class", "available");
        }

        var id = option.perfectMatch(selectedPositions);

        console.log("#######");
        console.log(id);
        console.log("#######");

        if (id) {

            $("piece-" + id).setAttribute("class", "perfect-match");

            var topLeft = selectedPositions.getTopLeftPosition();
            var bottomRight = selectedPositions.getBottomRightPosition();
            console.log(topLeft);
            console.log(bottomRight);
            var copy = function(topLeft, bottomRight) {

                var width = 2 + 1 + bottomRight.column - topLeft.column;
                var height = 2 + 1 + bottomRight.row - topLeft.row;

                var newCanvas = $("pieceToPlay");
                newCanvas.width = 34 * width;
                newCanvas.height = 34 * height;
                // var imageData =boardRendering.getContext().getImageData(34 *
                // topLeft.column,34 * topLeft.row, 33 * width, 33 * height);
                // newCanvas.getContext("2d").putImageData(imageData, 0, 0);
                var tmpBoardRendering = new BoardRendering(new CellRendering(newCanvas, 34, 34, 33, 33));
                var positions = selectedPositions.get();
                for ( var position in positions) {
                    var p = JSON.parse(position); // TODO !!
                    tmpBoardRendering.updateCell(new Position(1 + p.row - topLeft.row, 1 + p.column - topLeft.column), "Green");
                }
            };
            copy(topLeft, bottomRight);
            $("submit").show();
        } else {
            $("submit").hide();
        }

    }
}, false);
/*--------------------------------------------------8<--------------------------------------------------*/
$("pieceToPlay").addEventListener("mouseover", function(event) {
    $("pieceToPlay").setAttribute("style", "border:4px solid gray;");
}, false);

$("pieceToPlay").addEventListener("mouseout", function(event) {
    $("pieceToPlay").setAttribute("style", "border:4px solid #dadfe2;");
}, false);

$("pieceToPlay").addEventListener("click", function(event) {

    var pieceId = option.perfectMatch(selectedPositions);
    console.log(pieceId);

    var data = [];
    for ( var position in selectedPositions.get()) {
        var p = JSON.parse([ position ]);
        data.push([ p.row, p.column ]);
    }

    console.log(JSON.stringify(data));

    new Ajax.Request("/blockplus/submit", {
        onSuccess : function(response) {
            console.log(response.responseText);
            $("submit").hide();
            selectedPositions.clear();
            getAvailablePieces();
            source.connect();
            // TODO mettre à jour le bag
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
/*
$("skip").addEventListener("click", function(event) {
    new Ajax.Request("/blockplus/submit", {
        onSuccess : function(response) {
            console.log(response.responseText);
            $("submit").hide();
            selectedPositions.clear();
            getAvailablePieces();
            source.connect();
            // TODO mettre à jour le bag
        },
        onFailure : function(response) {
            alert("failed!");
        },
        method : 'get',
        parameters : {
            id : 0,
            positions : JSON.stringify([])
        }
    });
}, false);
*/
/*--------------------------------------------------8<--------------------------------------------------*/
