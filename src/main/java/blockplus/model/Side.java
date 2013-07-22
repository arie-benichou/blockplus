/*
 * Copyright 2012-2013 Arie Benichou
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

package blockplus.model;

import blockplus.model.polyomino.Polyomino;

public final class Side {

    public static Side with(final Pieces remainingPieces) {
        return new Side(remainingPieces);
    }

    private final Pieces remainingPieces;

    public Pieces remainingPieces() {
        return this.remainingPieces;
    }

    private Side(final Pieces remainingPieces) {
        this.remainingPieces = remainingPieces;
    }

    public Side apply(final Polyomino polyomino) {
        return new Side(this.remainingPieces().remove(polyomino));
    }

    public boolean isNull() {
        return !this.remainingPieces().contains(Polyomino._0);
    }

}