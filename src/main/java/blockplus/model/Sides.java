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

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import blockplus.model.Sides.Side;
import blockplus.model.polyomino.Polyomino;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

public final class Sides implements Iterable<Entry<Colors, Side>> {

    public final static class Side {

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

        @Override
        public String toString() {
            return this.remainingPieces.toString();
        }

    }

    public final static class Builder {

        private final SidesOrdering sidesOrdering;

        private final Map<Colors, Side> sides = Maps.newTreeMap();

        public Builder(final SidesOrdering sidesOrdering) {
            this.sidesOrdering = sidesOrdering;
        }

        public Builder add(final Side side) {
            final Colors color = this.sidesOrdering.next(this.sides.size() - 1);
            this.sides.put(color, side);
            return this;
        }

        public Sides build() {
            Preconditions.checkState(this.sides.size() == this.sidesOrdering.sides().size());
            return new Sides(this.sidesOrdering, this.sides);
        }
    }

    private final SidesOrdering sidesOrdering;
    private final Map<Colors, Side> sides;

    private Sides(final SidesOrdering sidesOrdering, final Map<Colors, Side> sides) {
        this.sidesOrdering = sidesOrdering;
        this.sides = sides;
    }

    public boolean hasSide() {
        for (final Side side : this.sides.values()) {
            if (!side.isNull()) return true;
        }
        return false;
    }

    public Side getSide(final Colors color) {
        return this.sides.get(color);
    }

    public Sides apply(final Colors color, final Polyomino polyomino) {
        final Side player = this.sides.get(color);
        final Map<Colors, Side> sides = Maps.newHashMap(this.sides);
        sides.put(color, player.apply(polyomino));
        return new Sides(this.sidesOrdering, sides);
    }

    public Colors next(final Colors color) {
        return this.sidesOrdering.next(color);
    }

    @Override
    public Iterator<Entry<Colors, Side>> iterator() {
        return this.sides.entrySet().iterator();
    }

    @Override
    public String toString() {
        return this.sides.toString();
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Sides)) return false;
        final Sides that = (Sides) object;
        return that.hashCode() == this.hashCode();
    }

}