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

package blockplus.piece;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Equivalences;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class Pieces implements Iterable<Entry<PieceType, Integer>> {

    public final static Pieces EMPTY = new Builder().build();

    public final static class Builder {

        private final Map<PieceType, Integer> pieces;

        public Builder(final Map<PieceType, Integer> pieces) {
            this.pieces = Maps.newEnumMap(pieces);
        }

        public Builder() {
            this.pieces = Maps.newEnumMap(PieceType.class);
        }

        public Builder add(final PieceType piece) {
            Integer integer = this.pieces.get(piece);
            if (integer == null) integer = 0;
            this.pieces.put(piece, integer + 1);
            return this;
        }

        public Builder addAll(final Iterable<PieceType> pieces) {
            for (final PieceType piece : pieces) {
                this.add(piece);
            }
            return this;
        }

        public Builder addAll(final PieceType... pieces) {
            for (final PieceType piece : pieces) {
                this.add(piece);
            }
            return this;
        }

        public Builder remove(final PieceType piece) {
            this.pieces.remove(piece);
            return this;
        }

        public Pieces build() {
            return new Pieces(this.pieces);
        }

    }

    private final Map<PieceType, Integer> pieces;

    private Pieces(final Map<PieceType, Integer> pieces) {
        this.pieces = pieces;
    }

    public boolean isEmpty() {
        return this.pieces.isEmpty();
    }

    public boolean contains(final PieceType piece) {
        final Integer integer = this.pieces.get(piece);
        return integer != null && integer > 0;
    }

    public Pieces withdraw(final PieceType piece) {
        Preconditions.checkState(this.contains(piece));
        return new Builder(this.pieces).remove(piece).build();
    }

    @Override
    public Iterator<Entry<PieceType, Integer>> iterator() {
        return this.pieces.entrySet().iterator();
    }

    @Override
    public String toString() {
        return this.pieces.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.pieces);
    }

    @Override
    public boolean equals(final Object object) {
    	if(object==null) return false;
        Preconditions.checkArgument(object instanceof Pieces);
        final Pieces that = (Pieces) object;
        return Equivalences.equals().equivalent(this.pieces, that.pieces);
    }
}