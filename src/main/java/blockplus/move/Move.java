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

import interfaces.move.MoveInterface;
import blockplus.Color;
import blockplus.piece.PieceInterface;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class Move implements MoveInterface {

    private final Color color;

    public Color getColor() {
        return this.color;
    }

    private final PieceInterface piece;

    public PieceInterface getPiece() {
        return this.piece;
    }

    public Move(final Color color, final PieceInterface piece) {
        this.color = color;
        this.piece = piece;
    }

    @Override
    public boolean isNull() {
        return this.getPiece().getId() == 0;
    }

    @Override
    public int hashCode() { // TODO à revoir + caching
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
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.getColor())
                .addValue(this.getPiece())
                .toString();
    }

}