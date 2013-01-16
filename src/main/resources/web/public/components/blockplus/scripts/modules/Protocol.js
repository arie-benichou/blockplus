function Protocol() {
    this.listeners = {};
}
Protocol.prototype = {

    constructor : Protocol,

    handle : function(message) {
        try {
            this.on(JSON.parse(message));
            console.log(message);
        } catch (e) {
            console.log(e);
            console.error(message);
        }
    },

    on : function(json) {
        if (json.type in this.listeners)
            this.listeners[json.type](json.data);
        else
            console.error("Protocol has no listener defined for event of type: " + json.type);
    },

    register : function(type, listener) {
        this.listeners[type] = listener;
    }
    
};