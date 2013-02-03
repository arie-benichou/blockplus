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

package blockplus.move;


import blockplus.color.ColorInterface;
import blockplus.piece.PieceInterface;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import components.position.PositionInterface;

// TODO extract Comparator(s)
public class Move implements Comparable<Move> {

    private final ColorInterface color;
    private final PieceInterface piece;

    public Move(final ColorInterface color, final PieceInterface piece) {
        this.color = color;
        this.piece = piece;
    }

    public ColorInterface getColor() {
        return this.color;
    }

    public PieceInterface getPiece() {
        return this.piece;
    }

    public PositionInterface getPosition() {
        return this.getPiece().getReferential();
    }

    @Override
    // TODO à revoir + caching
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Move)) return false;
        final Move that = (Move) object;
        final boolean haveSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqual = this.getColor().equals(that.getColor()) && this.getPiece().equals(that.getPiece());
        Preconditions.checkState(haveSameHashCode == isEqual);
        return isEqual;
    }

    @Override
    public int compareTo(final Move that) {

        final int size1 = this.getPiece().getSelfPositions().size();
        final int size2 = that.getPiece().getSelfPositions().size();
        final int compare1 = size1 - size2;
        if (compare1 < 0) return 1;
        if (compare1 > 0) return -1;

        final int compare2 = this.getPiece().getId() - that.getPiece().getId();
        if (compare2 < 0) return 1;
        if (compare2 > 0) return -1;

        return that.getPosition().compareTo(this.getPosition());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.getColor())
                .addValue(this.getPiece())
                .toString();
    }

    public boolean isNull() {
        return this.getPiece().getId() == 0; // TODO ? avoir un objet NullMove
    }

}