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
var RemainingPieces = function(id) {
    this.element = document.getElementById(id);
};

RemainingPieces.prototype = {

    constructor : RemainingPieces,

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

    update : function(color, remainingPieces) {
        for ( var i = 0, n = remainingPieces.length; i < n; ++i) {
            // TODO à revoir
            if (remainingPieces[i] != 0) {
                // TODO à revoir
                var key = getLocalStoreKey(color, "piece" + remainingPieces[i]);
                var retrievedObject = localStorage.getItem(key);
                var image = new Image();
                image.src = retrievedObject;
                image.setAttribute("style", "width:55px; height:55px;");
                this.getElement().appendChild(image);
            }
        }
    }

};