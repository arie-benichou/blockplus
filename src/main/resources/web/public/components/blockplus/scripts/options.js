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
	}
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
	}
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
	}
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
	}
});
/*--------------------------------------------------8<--------------------------------------------------*/
var offsetToPositionBuilder = new OffsetToPositionBuilder(34, 34);
var boardRendering = new BoardRendering(new CellRendering("board", 34, 34, 33, 33));
/*--------------------------------------------------8<--------------------------------------------------*/
for ( var i=1 ; i <= 21; ++i ) {
	var retrievedObject = localStorage.getItem("piece" + i);
	var image = new Image();
	image.setAttribute("id", "piece-" + i);
	image.src = retrievedObject;
	image.setAttribute("class", "available"); 
	$("available-pieces").appendChild(image);
}
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
	},	
	toString : function() {
		return "Position{" + "data=" + this.get() + "}";
	}
});
/*--------------------------------------------------8<--------------------------------------------------*/
/*
new Ajax.Request("/legal-positions-by-piece.json", {onSuccess: function(response) {
	option = new Options(JSON.parse(response.responseText));
}});
*/
new Ajax.Request("/blockplus/options", {
	onSuccess: function(response) {
		option = new Options(JSON.parse(response.responseText));
		console.log(response.responseText);
	},
	onFailure: function(response) {
		alert("failed!");
	},		
	method: 'get',
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
		}
		else {
			boardRendering.updateCell(position, "black");
			selectedPositions.add(position);
		}
		
		/*
		console.log(selectedPositions);		
		console.log(selectedPositions.get());
		var poss = selectedPositions.get();
		console.log("ok1");
		for ( var p in poss) {
			console.log(p);
		}
		console.log("ok2");
		*/

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