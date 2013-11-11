var colors = {
	B : "blue",
	Y : "yellow",
	R : "red",
	G : "green"
};

var populate = function() {
	var board = $("#board");
	for ( var i = 0; i < 20; ++i) {
		board.append("<tr></tr>");
		var row = $("#board tr:nth-child(" + (i + 1) + ")");
		for ( var j = 0; j < 20; ++j)
			row.append("<td id='" + (i + ':' + j) + "'></td>");
	}
}

var cell = function(row, column) {
	var tr = $("#board tr:nth-child(" + (row + 1) + ")");
	var td = tr.find("td:nth-child(" + (column + 1) + ")");
	return td;
};

var parse = function(moves) {
	var positions = [];
	for (i in moves) {
		var move = moves[i];
		var color = move.charAt(0)
		var tail = move.substring(1)
		if (tail !== "") {
			var ps = tail.split('-')
			for (j in ps) {
				var position = ps[j].split(':')
				var object = {
					color : colors[color],
					row : parseInt(position[0]),
					column : parseInt(position[1])
				}
				positions.push(object);
			}
		}
	}
	return positions;
};

var renderCell = function(obj) {
	cell(obj.row, obj.column).attr('class', obj.color)
};

var render = function(cells) {
	for (i in cells)
		renderCell(cells[i])
};

var resetCells = function() {
	for ( var i = 0; i < 20; ++i)
		for ( var j = 0; j < 20; ++j) {
			cell(i, j).attr('class', "black")
			cell(i, j).html('')
		}
};

var updatePotentialCells = function(options, selection) {
	var potentialPositions = partialMatch(options, selection);
	for (position in distinctPositions) {
		data = position.split(':')
		var row = parseInt(data[0]);
		var column = parseInt(data[1]);
		cell(row, column).html('')
	}
	for (position in potentialPositions) {
		data = position.split(':')
		var row = parseInt(data[0]);
		var column = parseInt(data[1]);
		cell(row, column).html("O")
		cell(row, column).attr("class", context.color);
	}
}

var update = function(ctx) {
	context = ctx;
	var c = context.color.charAt(0);
	color = colors[c];
	resetCells();
	render(parse(context.path));
	selection = [];
	distinctPositions = {};
	options = [];
	for (i in context.options) {
		var positions = context.options[i].split('-')
		var object = {}
		for (j in positions) {
			distinctPositions[positions[j]] = true
			object[positions[j]] = true
		}
		options.push(object)
	}
	updatePotentialCells(options, selection);
	scores = context.scores;
	console.log(scores);
}

var partialMatch = function(options, selection) {
	var matches = {};
	var min = selection.length;
	for (i in options) {
		var option = options[i];
		var order = Object.keys(option).length;
		if (order >= min) {
			var match = true;
			for (j in selection) {
				var position = selection[j];
				if (!(position in option)) {
					match = false;
					break;
				}
			}
			if (match)
				for (position in option)
					matches[position] = true;
		}
	}
	return matches;
};

var match = function(options, selection) {
	var n = selection.length;
	for (i in options) {
		var option = options[i];
		var order = Object.keys(option).length;
		if (order == n) {
			var match = true;
			for (j in selection) {
				var position = selection[j];
				if (!(position in option)) {
					match = false;
					break;
				}
			}
			if (match)
				return true;
		}
	}
	return false;
};

var handleSelection = function(event) {
	var p = event.target.id
	var potentialPositions = partialMatch(options, selection);
	if (p in potentialPositions) {
		var index = selection.indexOf(p);
		if (index == -1)
			selection.push(p)
		else {
			selection.splice(index, 1);
			var position = p.split(':');
			var object = {
				row : parseInt(position[0]),
				column : parseInt(position[1]),
				color : "black"
			};
			renderCell(object);
		}
		updatePotentialCells(options, selection);
		for (i in selection) {
			var position = selection[i].split(':');
			var object = {
				row : parseInt(position[0]),
				column : parseInt(position[1]),
				color : color
			};
			renderCell(object);
			cell(object.row, object.column).addClass('transparent');
		}
		if (match(options, selection))
			$("#submit").show();
		else
			$("#submit").hide();
	}
}

var handleSubmit = function(event) {
	$("#submit").hide();
	var move = context.color.charAt(0) + selection.join("-");
	$.ajax({
		url : "/play/" + move,
		method : "post",
		success : update
	});
}