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

var OffsetToPositionBuilder = function(offsetX, offsetY) {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
};

OffsetToPositionBuilder.prototype = {

    constructor : OffsetToPositionBuilder,

    getOffsetX : function() {
        return this.offsetX;
    },

    getOffsetY : function() {
        return this.offsetY;
    },

    build : function(x, y) {
        var row = Math.floor(y / this.offsetY);
        var column = Math.floor(x / this.offsetX);
        return new Position(row, column);
    }

};