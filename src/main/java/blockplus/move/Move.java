/*
 * Copyright 2012 Arie Benichou
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

import blockplus.color.Color;
import blockplus.piece.PieceInterface;
import blockplus.position.PositionInterface;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

// TODO extract Comparator(s)
public class Move implements Comparable<Move> {

    private final Color color;
    private final PieceInterface piece;

    public Move(final Color color, final PieceInterface piece) {
        this.color = color;
        this.piece = piece;
    }

    public Color getColor() {
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
        final boolean haveSameHashCode = this.hashCode() == that.hashCode(); // TODO à commenter
        final boolean isEqual = this.getColor().equals(that.getColor()) && this.getPiece().equals(that.getPiece());
        Preconditions.checkState(haveSameHashCode == isEqual); // TODO à commenter
        return isEqual;
    }

    @Override
    public int compareTo(final Move that) {
        final int compare = this.getPosition().compareTo(that.getPosition());
        if (compare != 0) return compare;
        return that.getPiece().getId() - this.getPiece().getId();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.getColor())
                .addValue(this.getPiece())
                .toString();
    }

}