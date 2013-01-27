var AudioManager = function(audio) {
    this.audio = audio;
};

AudioManager.prototype = {

    constructor : AudioManager,

    play : function(url) {
        this.audio.pause();
        this.audio.src = url;
        this.audio.oncanplay = this.audio.play();
    },

    pause : function() {
        this.audio.pause();
    }

};