var Blockplus = Blockplus || {};

Blockplus.Client = function(name, location) {
    this.name = name;
    this.location = location;
    this.ws = null;
};

Blockplus.Client.timeout = 5 * 1000;

Blockplus.Client.protocol = new Protocol(false);

Blockplus.Client.prototype = {

    constructor : Blockplus.Client,

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