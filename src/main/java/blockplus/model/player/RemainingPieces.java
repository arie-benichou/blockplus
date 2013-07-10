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

package blockplus.model.player;


import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import blockplus.model.entity.Polyomino;

import com.google.common.base.Equivalences;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class RemainingPieces implements Iterable<Entry<Polyomino, Integer>> {

    public final static RemainingPieces EMPTY = new Builder().build();

    public final static class Builder {

        private final Map<Polyomino, Integer> pieces;

        public Builder(final Map<Polyomino, Integer> pieces) {
            this.pieces = Maps.newEnumMap(pieces);
        }

        public Builder() {
            this.pieces = Maps.newEnumMap(Polyomino.class);
        }

        public Builder add(final Polyomino piece) {
            Integer integer = this.pieces.get(piece);
            if (integer == null) integer = 0;
            this.pieces.put(piece, integer + 1);
            return this;
        }

        public Builder addAll(final Iterable<Polyomino> pieces) {
            for (final Polyomino piece : pieces) {
                this.add(piece);
            }
            return this;
        }

        public Builder addAll(final Polyomino... pieces) {
            for (final Polyomino piece : pieces) {
                this.add(piece);
            }
            return this;
        }

        public Builder remove(final Polyomino piece) {
            this.pieces.put(piece, 0);
            return this;
        }

        public RemainingPieces build() {
            return new RemainingPieces(this.pieces);
        }

    }

    private final Map<Polyomino, Integer> pieces;

    private RemainingPieces(final Map<Polyomino, Integer> pieces) {
        this.pieces = pieces;
    }

    public boolean contains(final Polyomino piece) {
        final Integer integer = this.pieces.get(piece);
        return integer != null && integer > 0;
    }

    public RemainingPieces withdraw(final Polyomino piece) {
        Preconditions.checkState(this.contains(piece));
        return new Builder(this.pieces).remove(piece).build();
    }

    @Override
    public Iterator<Entry<Polyomino, Integer>> iterator() {
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
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof RemainingPieces);
        final RemainingPieces that = (RemainingPieces) object;
        return Equivalences.equals().equivalent(this.pieces, that.pieces);
    }
}