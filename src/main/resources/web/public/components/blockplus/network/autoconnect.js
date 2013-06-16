var computeLocation = function(suffix) {
	return document.location.origin.toString().replace('http://', 'ws://').replace('https://', 'wss://') + suffix;
};

var connection = function(name) {
	var object = {
		type : 'Client',
		data : {
			name : name
		}
	};
	return object;
};

var gameConnection = function(n) {
	var message = {
		type : 'GameConnection',
		data : {
			ordinal : n
		}
	};
	return message;
};

Client.protocol.register("enterGame", function(data) {
	myGame = new Game(client, that.boardManager);
	myGame.bigMess();
});

Client.protocol.register("games", function(data) {
	client.say(gameConnection(2));
});

Client.message = connection; // TODO à revoir
var url = computeLocation("/network/io");
var client = new Client("Android", url);
client.start(client.join);