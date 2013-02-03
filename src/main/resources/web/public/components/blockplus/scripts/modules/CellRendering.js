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

var CellRendering = function(canvas, offsetY, offsetX, width, length) {
    this.canvas = canvas;
    this.context = canvas.getContext("2d");
    this.offsetY = offsetY;
    this.offsetX = offsetX;
    this.width = width;
    this.length = length;
};

CellRendering.prototype = {

    constructor : CellRendering,

    getCanvas : function() {
        return this.canvas;
    },

    getContext : function() {
        return this.context;
    },

    update : function(position, color) {
        if (color in Colors) {
            this.getContext().fillStyle = Colors[color];
        } else if (color == "White") {
            this.getContext().fillStyle = "#2a2d30";
        } else {
            this.getContext().fillStyle = color;
        }
        this.getContext().fillRect(this.offsetX * position.column, this.offsetY * position.row, this.width, this.length);
    }

};