var Blockplus = Blockplus || {};

// TODO ? use lowlag library: http://lowlag.alienbill.com/playground.cgi
Blockplus.AudioManager = function(element) {
	this.audio = document.getElementById(element);
	this.audio.src= "../audio/dummy.ogg";
	this.audio.load();
	this.audio.play();
};

Blockplus.AudioManager.prototype = {

	constructor : Blockplus.AudioManager,

	play : function(uri) {
		this.audio.pause();
		//this.audio.src='';
		this.audio.src= uri
		//this.audio.currentSrc = uri;
		//this.audio.load();
		this.audio.play();
	},

};

/*
function play(audioElement, uri) {
	console.debug(audioElement);
	audioElement.pause();
	audioElement.src = '';
	audioElement.src = uri;
	audioElement.currentSrc = uri;
	audioElement.load();
	playWhenReady(audioElement, uri);
}

	//var id = audioElement.getAttribute('id');
	//console.debug(id);
	var audio = document.getElementById('test');
	var audioReady = audio.readyState;
	if (audioReady > 2) {
		audio.play();
	} else {
		console.error(audio);
	}
}
*/