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

var SelectedPositions = function() {
    this.data = {};
    this.size = 0;
};

SelectedPositions.prototype = {

    constructor : SelectedPositions,

    get : function() {
        return this.data;
    },

    getSize : function() {
        return this.size;
    },
    
    isEmpty : function() {
        return this.size == 0;
    },    

    add : function(position) {
        this.data[JSON.stringify(position)] = true;
        ++this.size;
    },

    remove : function(position) {
        delete this.data[JSON.stringify(position)];
        --this.size;
    },

    contains : function(position) {
        return (JSON.stringify(position) in this.data);
    },

    clear : function(position) {
        this.data = {};
        this.size = 0;
    },

    getTopLeftPosition : function() {
        var top = Infinity;
        var left = Infinity;
        for ( var entry in this.get()) {
            var position = JSON.parse(entry);
            var y = position.row;
            var x = position.column;
            if (y < top)
                top = y;
            if (x < left)
                left = x;
        }
        return new Position(top, left);
    },

    getBottomRightPosition : function() {
        var bottom = -Infinity;
        var right = -Infinity;
        for ( var entry in this.get()) {
            var position = JSON.parse(entry);
            var y = position.row;
            var x = position.column;
            if (y > bottom)
                bottom = y;
            if (x > right)
                right = x;
        }
        return new Position(bottom, right);
    }

};