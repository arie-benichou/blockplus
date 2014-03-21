var Blokus = Blokus || {};
/**
 * @constructor
 */
Blokus.Game = function() {
	this.colors = {
		B : "blue",
		Y : "yellow",
		R : "red",
		G : "green"
	};
};
Blokus.Game.prototype = {
	constructor : Blokus.Game,

	populate : function() {
		var board = $("#board");
		for ( var i = 0; i < 20; ++i) {
			board.append("<tr></tr>");
			var row = $("#board tr:nth-child(" + (i + 1) + ")");
			for ( var j = 0; j < 20; ++j)
				row.append("<td id='" + (i + ':' + j) + "'></td>");
		}
	},

	registerEvents : function() {
		$("#board td").bind("click", $.proxy(this.handleSelection, this));
		$("#submit").bind("click", $.proxy(this.handleSubmit, this));
		$("#board").bind("mousedown", function(event) {
			event.preventDefault();
		});
	},

	refresh : function() {
		$.ajax({
			url : "/context",
			success : $.proxy(this.update, this)
		});
	},

	cell : function(row, column) {
		var tr = $("#board tr:nth-child(" + (row + 1) + ")");
		var td = tr.find("td:nth-child(" + (column + 1) + ")");
		return td;
	},

	parse : function(moves) {
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
						color : this.colors[color],
						row : parseInt(position[0]),
						column : parseInt(position[1])
					}
					positions.push(object);
				}
			}
		}
		return positions;
	},

	renderCell : function(obj) {
		this.cell(obj.row, obj.column).attr('class', obj.color)
	},

	render : function(cells) {
		for (i in cells)
			this.renderCell(cells[i])
	},

	resetCells : function() {
		for ( var i = 0; i < 20; ++i)
			for ( var j = 0; j < 20; ++j) {
				this.cell(i, j).attr('class', "black")
				this.cell(i, j).html('')
			}
	},
	
	updatePotentialCells : function(options, selection) {
		var potentialPositions = this.partialMatch(options, selection);
		for (position in distinctPositions) {
			data = position.split(':')
			var row = parseInt(data[0]);
			var column = parseInt(data[1]);
			this.cell(row, column).html('')
		}
		for (position in potentialPositions) {
			data = position.split(':')
			var row = parseInt(data[0]);
			var column = parseInt(data[1]);
			this.cell(row, column).html("O")
			this.cell(row, column).attr("class", context.color);
		}
	},

	update : function(ctx) {
		context = ctx;
		var c = context.color.charAt(0);
		color = this.colors[c];
		this.resetCells();
		this.render(this.parse(context.path));
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
		this.updatePotentialCells(options, selection);
		scores = context.scores;
		console.log(scores);
	},

	partialMatch : function(options, selection) {
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
	},

	match : function(options, selection) {
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
	},

	handleSelection : function(event) {
		var p = event.target.id
		var potentialPositions = this.partialMatch(options, selection);
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
				this.renderCell(object);
			}
			this.updatePotentialCells(options, selection);
			for (i in selection) {
				var position = selection[i].split(':');
				var object = {
					row : parseInt(position[0]),
					column : parseInt(position[1]),
					color : color
				};
				this.renderCell(object);
				this.cell(object.row, object.column).addClass('transparent');
			}
			if (this.match(options, selection))
				$("#submit").show();
			else
				$("#submit").hide();
		}
	},

	handleSubmit : function(event) {
		$("#submit").hide();
		var move = context.color.charAt(0) + selection.join("-");
		$.ajax({
			url : "/play/" + move,
			method : "post",
			success : $.proxy(this.update, this)
		});
	}

};