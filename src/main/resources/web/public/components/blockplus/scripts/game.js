/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * Position
 */
var Position = Class.create({
	initialize : function(y, x) {
		this.row = y;
		this.column = x;
	},
	getRow : function() {
		return this.row;
	},
	getColumn : function() {
		return this.column;
	},
	toString : function() {
		return "Position{" + "row=" + this.getRow() + ", " + "column=" + this.getColumn() + "}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * OffsetToPositionBuilder
 */
var OffsetToPositionBuilder = Class.create({
	initialize : function(offsetX, offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	},
	getOffsetX : function() {
		return this.offsetX;
	},
	getOffsetY : function() {
		return this.offsetY;
	},
	build : function(x, y) {
		var row = Math.floor(y / this.offsetY);
		var column = Math.floor(x / this.offsetX);
		newPosition = new Position(row, column);
		console.log(newPosition.toString());
		return newPosition;

	},
	toString : function() {
		return "OffsetToPositionBuilder{" + "offsetX=" + this.getOffsetX() + ", " + "offsetY=" + this.getOffsetY() + "}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * CellRendering
 */
var CellRendering = Class.create({
	initialize : function(canvas, offsetY, offsetX, width, length) {
		this.canvas = $(canvas);
		this.context = this.canvas.getContext("2d");
		this.offsetY = offsetY;
		this.offsetX = offsetX;
		this.width = width;
		this.length = length;
	},
	getCanvas : function() {
		return this.canvas;
	},
	getContext : function() {
		return this.context;
	},
	update : function(position, state) {
		switch (state) {
		case "Blue":
			this.getContext().fillStyle = "blue";
			break;
		case "Yellow":
			this.getContext().fillStyle = "yellow";
			break;
		case "Green":
			this.getContext().fillStyle = "green";
			break;
		case "Red":
			this.getContext().fillStyle = "red";
			break;
		case "White":
			this.getContext().fillStyle = "gray";
			break;
		default:
			this.getContext().fillStyle = "black";
		}
		this.getContext().fillRect(this.offsetX * position.getColumn(), this.offsetY * position.getRow(), this.width, this.length);
	},
	// TODO ajouter canvas et context
	toString : function() {
		return "CellRendering" + "{" + "offsetY=" + this.offsetY + ", " + "offsetX=" + this.offsetX + ", " + "width=" + this.width + ", " + "length="
				+ this.length;
		"}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * BoardRendering
 */
var BoardRendering = Class.create({
	initialize : function(cellRendering) {
		this.cellRendering = cellRendering;
	},
	getCanvas : function() {
		return this.cellRendering.getCanvas();
	},
	getContext : function(board) {
		return this.cellRendering.getContext();
	},
	updateCell : function(position, state) {
		this.cellRendering.update(position, state);
	},
	update : function(board) {
		for ( var i = 0; i < board.length; i++) {
			var row = board[i];
			for ( var j = 0; j < row.length; j++) {
				var state = board[i][j];
				var position = new Position(i, j);
				this.updateCell(position, state);
			}
		}
	},
	clear : function() {
		this.getContext().clearRect(0, 0, this.getCanvas().width, this.getCanvas().height);
	},	
	toString : function() {
		return "BoardRendering{" + "cellRendering=" + this.cellRendering + "}";
	},
});
/*--------------------------------------------------8<--------------------------------------------------*/
var offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);
console.log(offsetToPositionBuilder.toString());
var boardRendering = new BoardRendering(new CellRendering("board", 34, 34, 33, 33));
console.log(boardRendering.toString());
/*--------------------------------------------------8<--------------------------------------------------*/
/*
boardRendering.getCanvas().addEventListener("click", function(event) {
	if (event.ctrlKey)
		window.location = event.srcElement.toDataURL("image/png");
	else
		boardRendering.updateCell(offsetToPositionBuilder.build(event.offsetX, event.offsetY), "black");
}, false);
*/
/*--------------------------------------------------8<--------------------------------------------------*/
function EventSourceManager(url) {
    this.url = url;
    this.es = null;
    this.listeners = {};
}
EventSourceManager.prototype = {
    constructor: EventSourceManager,
    connect: function() {
        this.es = new EventSource(this.url);
        this.bindEvents();
    },
    disconnect: function() {
        this.es.close();
        this.es = null;
    },
    bindEvents: function() {
        for ( var type in this.listeners ) {
            var evs = this.listeners[type];
            for( var i = 0; i < evs.length; ++i ) {
                this.es.addEventListener( type, evs[i], false );
            }
        }
    },
    addEventListener: function( type, fn ) {
        if( !this.listeners[type] ) {
            this.listeners[type] = [];
        }
        this.listeners[type].push( fn );
        if( this.es ) {
            this.bindEvents();
        }
    }
};
/*--------------------------------------------------8<--------------------------------------------------*/
var source = new EventSourceManager("/blockplus/data");
//source.addEventListener("something", somefunction, false);
//source.disconnect();
/*--------------------------------------------------8<--------------------------------------------------*/
// TODO ! extract EventManager
// TODO ! ajouter une zone de notifications dans la page
// TODO ! check origin & integrity of messages
// TODO ! utiliser le local storage pour enregistrer le currentime de la
// musique, par exemple...
// TODO ? émettre l"événement newgame
//var source = new EventSource("/blockplus/data");

source.addEventListener("open", function(event) {
	console.log("Event listening");
}, false);

source.addEventListener("message", function(event) {
	console.log(event.data);
	// TODO extract method
	{
		var newChild = document.createElement("div");
		newChild.setAttribute("id", "last-message");
		newChild.innerHTML = JSON.parse(event.data);
		$("last-message").remove();
		$("messages").appendChild(newChild);
	}
}, false);

source.addEventListener("error", function(event) {
	console.log(event);
	//console.log(event.data);
	console.log(event.type);
	//console.log(event.target);
	console.log(event.readyState);
	console.log(event.srcElement.readyState);
	if (event.readyState == EventSource.CLOSED) {
		console.log("Event handling error");
		alert('error');
	}
		
}, false);

source.addEventListener("gamenotover", function(event) {
	boardRendering.update(JSON.parse(event.data));
	$("board").className = "game-is-not-over";
	if ($("game-is-not-over").currentTime == 0) {
		//$("game-is-not-over").loop = true;
		$("game-is-not-over").play();
	}
}, false);

/*
source.addEventListener("bag", function(event) {
	console.log(event.data);
	var array = JSON.parse(event.data);
	alert(array);
	for ( var i=0 ; i < array.length; ++i ) {
		$(("piece-" + array[i])).setAttribute("class", "available"); 
	}
}, false);
*/

function showPotentialCells(position) {
	var context = boardRendering.getContext();	
	context.fillStyle = "rgba(0, 128, 0, 0.35)";
	context.beginPath();
	context.arc(34 * position.getColumn() + 34/2, 34 * position.getRow() + 34/2, 4, 0, Math.PI*2, true);
	context.closePath();
	context.fill();		
	context.lineWidth = 1;
	context.strokeStyle = "green";
	context.stroke();
}

source.addEventListener("options", function(event) {
	
	console.log(event.data);
	var array = JSON.parse(event.data);
	
	if(array == null) return // TODO
	
	console.log(array);
	source.disconnect();
	for ( var i=0 ; i < array.length; ++i ) {
		var position = new Position(array[i][0], array[i][1]);
		showPotentialCells(position);
	}
	
	new Ajax.Request("/blockplus/pieces", {
		onSuccess: function(response) {
			var array = JSON.parse(response.responseText);
			for ( var i = 1 ; i <= 21; ++i ) { // TODO
				$("piece-" + i).setAttribute("class", "not-available"); 
			}			
			for ( var i=0 ; i < array.length; ++i ) {
				$("piece-" + array[i]).setAttribute("class", "available"); 
			}
		},
		onFailure: function(response) {
			alert("failed!");
		},		
		method: 'get',
	});	

	new Ajax.Request("/blockplus/options", {
		onSuccess: function(response) {
			option = new Options(JSON.parse(response.responseText));
			console.log(response.responseText);
			$("submit").show();
		},
		onFailure: function(response) {
			alert("failed!");
		},		
		method: 'get',
	});
	
}, false);

source.addEventListener("gameover", function(event) {
	$("game-is-over").play();
	event.target.close();
	boardRendering.update(JSON.parse(event.data));
	$("board").className = "game-is-over";
	$("game-is-not-over").pause();
}, false);
/*--------------------------------------------------8<--------------------------------------------------*/
for ( var i=1 ; i <= 21; ++i ) {
	var retrievedObject = localStorage.getItem("piece" + i);
	var image = new Image();
	image.setAttribute("id", "piece-" + i);
	image.src = retrievedObject;
	//image.setAttribute("class", "available"); 
	image.setAttribute("class", "not-available");
	$("available-pieces").appendChild(image);
}
/*--------------------------------------------------8<--------------------------------------------------*/
source.connect();
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * Options
 */
var Options = Class.create({
	initialize : function(data) {
		var tmpData = [];
		for ( var p in data) {
			var pieceInstances = data[p];
			var pieceInstancesObject = {
					id: parseInt(p.substring(5,7), 10),
					size: pieceInstances[0].length,
					instances: []
			};
			var n = pieceInstances.length;
			for ( var i = 0; i < n; ++i) {
				var pieceInstance = pieceInstances[i];
				var size = pieceInstance.length;
				var pieceInstanceObject = {};				
				for ( var j = 0; j < size; ++j) {
					var p = new Position(pieceInstance[j][0],pieceInstance[j][1]);
					pieceInstanceObject[JSON.stringify(p)] = true;
				}
				pieceInstancesObject["instances"].push(pieceInstanceObject);
			}
			tmpData.push(pieceInstancesObject);
		}
		//console.log(tmpData);
		this.data = tmpData;
	},
	get : function() {
		return this.data;
	},
	matches : function(selectedPositions) {
		var matches = {};
		var min = selectedPositions.getSize();
		//console.log("#####################");
		var n = this.data.length;
		for ( var i = 0; i < n; ++i) {
			var pieceInstances = this.data[i];
			if(pieceInstances.size >= min) {
				//console.log(pieceInstances);
				var instances = pieceInstances.instances;
				for ( var j = 0; j < instances.length; ++j) {
					var match = true;
					for ( var position in selectedPositions.get()) {
						if(!(position in instances[j])) {
							match = false;
							break;
						}
					}
					if (match) {
						//console.log(instances[j]);
						matches[pieceInstances.id] = true;
					}
				}
			}
		}
		//console.log(matches);
		//console.log("#####################");
		return matches;
	},
	perfectMatch : function(selectedPositions) {
		//var matches = {};
		var min = selectedPositions.getSize();
		//console.log("#####################");
		var n = this.data.length;
		for ( var i = 0; i < n; ++i) {
			var pieceInstances = this.data[i];
			if(pieceInstances.size == min) {
				//console.log(pieceInstances);
				var instances = pieceInstances.instances;
				for ( var j = 0; j < instances.length; ++j) {
					var match = true;
					for ( var position in selectedPositions.get()) {
						if(!(position in instances[j])) {
							match = false;
							break;
						}
					}
					if (match) {
						//console.log(instances[j]);
						//matches[pieceInstances.id] = true;
						return pieceInstances.id;
					}
				}
			}
		}
		//console.log(matches);
		//console.log("#####################");
		return 0;
	}	
});
/*--------------------------------------------------8<--------------------------------------------------*/
/**
 * SelectedPositions
 */
var SelectedPositions = Class.create({
	initialize : function() {
		this.data = {};
		this.size = 0;
	},
	get : function() {
		return this.data;
	},
	getSize : function() {
		return this.size;
	},	
	add : function(position) {
		//if(!this.contains(position)) {
			this.data[JSON.stringify(position)] = true;
			++this.size;
		//}
	},
	remove : function(position) {
			delete this.data[JSON.stringify(position)];
			--this.size;
	},	
	contains : function(position) {
		return (JSON.stringify(position) in this.data);
	},
	clear : function(position) {
		this.data = {};
		this.size = 0;
	}		
});
/*--------------------------------------------------8<--------------------------------------------------*/
var selectedPositions = new SelectedPositions();
/*--------------------------------------------------8<--------------------------------------------------*/
boardRendering.getCanvas().addEventListener("click", function(event) {
	if (event.ctrlKey)
		window.location = event.srcElement.toDataURL("image/png");
	else {
		var position = offsetToPositionBuilder.build(event.offsetX, event.offsetY);
		if(selectedPositions.contains(position)) {
			boardRendering.updateCell(position, "White");
			selectedPositions.remove(position);
			showPotentialCells(position);
		}
		else {
			boardRendering.updateCell(position, "black");
			selectedPositions.add(position);
		}
		var matches = option.matches(selectedPositions);
		console.log(matches);
		for ( var i = 1 ; i <= 21; ++i ) {
			$(("piece-" + i)).setAttribute("class", "not-available"); 
		}
		for ( var id in matches) {
			$(("piece-" + id)).setAttribute("class", "available");
		}
	}
}, false);
/*--------------------------------------------------8<--------------------------------------------------*/
$("submit").addEventListener("click", function(event) {
	
	var pieceId = option.perfectMatch(selectedPositions);
	console.log(pieceId);
	if(pieceId == 0) alert("Pas si vite mon coco...");
	else {
		var data = [];
		for ( var position in selectedPositions.get()) {
			var p = JSON.parse([position]);
			data.push([p.row, p.column]);
		}
		console.log(JSON.stringify(data));
		new Ajax.Request("/blockplus/submit", {
			onSuccess: function(response) {
				console.log(response.responseText);
				$("submit").hide();
				selectedPositions.clear();
				source.connect(); // TODO ...
			},
			onFailure: function(response) {
				alert("failed!");
			},		
			method: 'get',
			parameters: {
				id: pieceId,
				positions: JSON.stringify(data)
			}
		});
	}
}, false);
/*--------------------------------------------------8<--------------------------------------------------*/