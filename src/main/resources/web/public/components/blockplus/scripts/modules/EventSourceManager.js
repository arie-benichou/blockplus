function EventSourceManager(url) {
    this.url = url;
    this.es = null;
    this.listeners = {};
}

EventSourceManager.prototype = {

    constructor : EventSourceManager,

    connect : function() {
        this.es = new EventSource(this.url);
        this.bindEvents();
    },

    disconnect : function() {
        this.es.close();
        this.es = null;
    },

    bindEvents : function() {
        for ( var type in this.listeners) {
            var evs = this.listeners[type];
            for ( var i = 0; i < evs.length; ++i) {
                this.es.addEventListener(type, evs[i], false);
            }
        }
    },

    addEventListener : function(type, fn) {
        if (!this.listeners[type]) {
            this.listeners[type] = [];
        }
        this.listeners[type].push(fn);
        if (this.es) {
            this.bindEvents();
        }
    }

};