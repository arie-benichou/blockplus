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

// TODO injecter le PieceManager
var AvailablePieces = function(id) {
    this.element = document.getElementById(id);
};

AvailablePieces.prototype = {

    constructor : AvailablePieces,

    getElement : function() {
        return this.element;
    },

    clear : function() {
        $(this.getElement()).html('');
    },

    show : function() {
        $(this.getElement()).show();
    },

    hide : function() {
        $(this.getElement()).hide();
    },

    update : function(color, availablePieces) {
        for ( var i = 1; i <= 21; ++i) { // TODO à revoir
            var key = getLocalStoreKey(color, "piece" + i);
            var retrievedObject = localStorage.getItem(key);
            var image = new Image();
            image.setAttribute("id", "piece-" + i);
            image.src = retrievedObject;
            image.setAttribute("class", "not-available");
            this.getElement().appendChild(image);
        }
        for ( var i = 0, n = availablePieces.length; i < n; ++i) {
            var x = availablePieces[i];
            if (x != 0) { // TODO à revoir
                document.getElementById("piece-" + x).setAttribute("class", "available");
            }
        }
    }

};