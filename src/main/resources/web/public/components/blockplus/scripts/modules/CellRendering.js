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