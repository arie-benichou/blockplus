/*
 * Copyright 2012-2013 ArteFact
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

// TODO use lowlag library: http://lowlag.alienbill.com/playground.cgi
var AudioManager = function() {
	this.audio = null;
	this.cache = {};
};

AudioManager.prototype = {

	constructor : AudioManager,

	load : function(url) {
		console.log("loading " + url);
		var audio = this.get(url);
		audio.autobuffer = true;
		audio.load();
	},

	play : function(url) {
		if(this.audio != null) this.audio.pause();
		this.audio = this.get(url).cloneNode(true);
		this.audio.play();
	},

	get : function(url) {
		if (url in this.cache) return this.cache[url];
		var audio = new Audio();
		audio.src = url;
		this.cache[url] = audio;
		return audio;
	}

};