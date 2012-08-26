//////////////////////////////////////////////////////////////////////////////////////////////////////
var source = new EventSource('/blockplus/data');
var handler = source.addEventListener('message', function(event) {
	var obj = JSON.parse(event.data);
	updateGame(obj);
	event.target.close();
}, false);
//////////////////////////////////////////////////////////////////////////////////////////////////////
var music = document.getElementById("music");
music.loop=false;
//////////////////////////////////////////////////////////////////////////////////////////////////////
var ok = false;
music.addEventListener('timeupdate', function(event) {
	//console.log(event.srcElement.currentTime);
	if(!ok && event.srcElement.currentTime > 4 ) {
		console.log(event.srcElement.currentTime);
		ok = true;
		var source = new EventSource('/blockplus/data');
		source.addEventListener('message', function(event) {
			var obj = JSON.parse(event.data);
			updateGame(obj);
		}, false);
	}
}, false);
//////////////////////////////////////////////////////////////////////////////////////////////////////
/*
music.addEventListener('ended', function(event) {
	//source.removeEventListener('message', handler);
	var source = new EventSource('/blockplus/data');
	source.addEventListener('message', function(event) {
		var obj = JSON.parse(event.data);
		updateGame(obj);
	}, false);
}, false);
*/
//////////////////////////////////////////////////////////////////////////////////////////////////////
music.play();
//////////////////////////////////////////////////////////////////////////////////////////////////////


var updateGame = function(obj) {
	var canvas = document.getElementById("board");
	var ctx = canvas.getContext("2d");
	for ( var i = 0; i < obj.length; i++) {
		var row = obj[i];
		for ( var j = 0; j < obj.length; j++) {
			// console.log(row[j]);
			switch (row[j]) {
			case "Self":
				ctx.fillStyle = "blue";
				break;
			case "Light":
				ctx.fillStyle = "yellow";
				break;
			case "Shadow":
				ctx.fillStyle = "green";
				break;
			case "Other":
				ctx.lineWidth = 5;
				ctx.stroke();
				ctx.fillStyle = "red";
				break;
			default:
	
				ctx.fillStyle = "gray";
			}
			ctx.fillRect(34 * j, 34 * i, 33, 33);
		}
	}
}

source.addEventListener('open', function(event) {
	console.log("Event listening...");
}, false);

/*
source.addEventListener('message', function(event) {
	//event.target.close();
	var obj = JSON.parse(event.data);
	updateGame(obj);
}, false);
*/

source.addEventListener('error', function(event) {
	if (event.readyState == EventSource.CLOSED)
		console.log("Event handling error");
}, false);
// ////////////////////////////////////////////////////////////////////////////////////////////////////
var canvas = document.getElementById("board");
canvas.addEventListener("click",
	function(event) {
		console.log(event);
		console.log(document.getElementById(event.srcElement.id));
		console.log(event.srcElement.toDataURL("image/png"));
		window.location = event.srcElement.toDataURL("image/png");
	},
	false
);
//////////////////////////////////////////////////////////////////////////////////////////////////////