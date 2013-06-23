var Blockplus = Blockplus || {};

// TODO ? use lowlag library: http://lowlag.alienbill.com/playground.cgi
Blockplus.AudioManager = function(element) {
	this.audio = document.getElementById(element);
};

Blockplus.AudioManager.prototype = {

	constructor : Blockplus.AudioManager,

	play : function(uri) {
		this.audio.pause();
		this.audio.src= uri
		this.audio.play();
	},

};