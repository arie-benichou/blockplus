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

import com.google.common.base.Objects;

public final class Side {

    public static Side from(final ColoredPolyominoSet remainingPieces) {
        return new Side(remainingPieces);
    }

    private final ColoredPolyominoSet remainingPieces;

    public ColoredPolyominoSet remainingPieces() {
        return this.remainingPieces;
    }

    private Side(final ColoredPolyominoSet remainingPieces) {
        this.remainingPieces = remainingPieces;
    }

    public Side apply(final Polyomino polyomino) {
        return new Side(this.remainingPieces().remove(polyomino));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("pieces", this.remainingPieces()).toString();
    }

}