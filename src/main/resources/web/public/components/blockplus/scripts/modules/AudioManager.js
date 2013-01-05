var AudioManager = Class.create({
    initialize : function(audio) {
        this.audio = audio;
    },
    play : function(url) {
        this.audio.pause();
        this.audio.src = url;
        //console.log(audio.currentSrc);
        //console.log(audio.currentTime);
        //this.audio.pause();
        this.audio.oncanplay=this.audio.play();
    },
});