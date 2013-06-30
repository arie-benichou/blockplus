/**
 * @constructor
 */
AudioManager = function(element) {
	this.audio = document.getElementById(element);
};
AudioManager.prototype = {
	constructor : AudioManager,
	play : function(uri) {
		this.audio.pause();
		this.audio.src = uri
		this.audio.play();
	}
};