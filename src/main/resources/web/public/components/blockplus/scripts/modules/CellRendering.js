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
	update : function(position, color) {
	    if(color in Colors) {
	        this.getContext().fillStyle = Colors[color];
	    }
        else if (color == "White"){
            //this.getContext().fillStyle = "gray";
            this.getContext().fillStyle = "#2a2d30";
        }	    
	    else {
	        this.getContext().fillStyle = color;
	    }
	    /*
		switch (color) {
		case "Blue":
			this.getContext().fillStyle = "blue";
			break;
		case "Yellow":
			this.getContext().fillStyle = "#ffc600";
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
			this.getContext().fillStyle = color;
		}
		*/
		this.getContext().fillRect(this.offsetX * position.getColumn(), this.offsetY * position.getRow(), this.width, this.length);
	},
	// TODO ajouter canvas et context
	toString : function() {
		return "CellRendering" + "{" + "offsetY=" + this.offsetY + ", " + "offsetX=" + this.offsetX + ", " + "width=" + this.width + ", " + "length="
				+ this.length;
		"}";
	},
});