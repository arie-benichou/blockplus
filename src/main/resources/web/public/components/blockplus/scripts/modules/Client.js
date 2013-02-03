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

var Client = function(name, location) {
    this.name = name;
    this.location = location;
    this.ws = null;
};

Client.timeout = 5 * 1000;

Client.protocol = new Protocol();

Client.prototype = {

    constructor : Client,

    start : function(onReady) {
        console.log("Connecting to server...");
        this.ws = new WebSocket(this.location);
        this.ws.onopen = this.onopen;
        this.ws.onmessage = this.onmessage;
        this.ws.onerror = this.onerror;
        this.ws.onclose = this.onclose;
        var that = this;
        var timeout = window.setTimeout(function() {
            clearInterval();
            console.error("Connection with server timed out.");
        }, Client.timeout);
        var clearTimeout = function() {
            window.clearTimeout(timeout);
        };
        var interval = window.setInterval(function() {
            var status = that.ws.readyState;
            if (status == 1) {
                clearTimeout();
                clearInterval();
                onReady(that);
            } else if (status == 3) {
                clearTimeout();
                clearInterval();
            }
        }, 50);
        var clearInterval = function() {
            window.clearInterval(interval);
        };
    },

    onopen : function(message) {
        console.log("Connected.");
    },

    // TODO incomingProtocol
    onmessage : function(message) {
        if (message.data)
            Client.protocol.handle(message.data);
    },

    onerror : function(message) {
        console.error("Connection error.");
    },

    onclose : function(message) {
        console.error("Connection has been closed.");
        this.ws = null;
    },

    say : function(message) {
        if (this.ws) {
            this.ws.send(JSON.stringify(message));
        } else {
            console.error("Connection with server has not been started yet.");
        }
    },

    join : function(that) {
        that.say(Client.message(that.name));
    }

};