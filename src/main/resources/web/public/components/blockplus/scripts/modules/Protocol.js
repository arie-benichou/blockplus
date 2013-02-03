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

function Protocol() {
    this.listeners = {};
}
Protocol.prototype = {

    constructor : Protocol,

    handle : function(message) {
        var object = null;
        try {
            object = JSON.parse(message);
        } catch (e) {
            console.error(message);
        }
        if (object != null)
            this.on(object);
    },

    on : function(json) {
        if (json.type in this.listeners)
            this.listeners[json.type](json.data);
        else {
            console.error(json);
            console.error("Protocol has no listener defined for event of type: " + json.type);
            console.error(json.data);
        }

    },

    register : function(type, listener) {
        this.listeners[type] = listener;
    }

};