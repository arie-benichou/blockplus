/*
var music = document.getElementById("music");
music.loop = false;
music.addEventListener('ended', function() {
	window.location = "./game.html";
}, false);
music.play();
*/

//myAudio = new Audio('someSound.ogg');
myAudio = $("music");
myAudio.addEventListener('ended', function() {
    console.log("end");
    this.currentTime = 0;
    this.play();
}, false);
myAudio.play();