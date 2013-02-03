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

package components.position;

import java.util.Map;


import com.google.common.collect.Maps;
import components.direction.DirectionInterface;

// TODO à revoir
public final class Position implements PositionInterface {

    public final static PositionInterface ORIGIN = new Position(0, 0);

    private final static int computeHashCode(final int row, final int column) {
        return (row + "|" + column).hashCode();
    }

    public final static class Factory {

        private static int cacheHits;

        private final static Map<Integer, PositionInterface> CACHE = Maps.newHashMap();

        public static PositionInterface get(final int row, final int column) {
            final int address = computeHashCode(row, column);
            PositionInterface instance = CACHE.get(address);
            if (instance == null) {
                CACHE.put(address, instance = new Position(row, column));
            }
            else ++cacheHits; // TODO ?! vérifier des éventuelles collisions
            return instance;
        }

        public final static int size() {
            return CACHE.size();
        }

        public final static int cacheHits() {
            return cacheHits;
        }

    }

    private final int row;

    @Override
    public int row() {
        return this.row;
    }

    private final int column;

    @Override
    public int column() {
        return this.column;
    }

    private final int hashCode;

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    public static PositionInterface from() {
        return ORIGIN; // TODO utiliser directement la factory
    }

    @SuppressWarnings("all")
    public static PositionInterface Position() {
        return ORIGIN; // TODO utiliser directement la factory
    }

    public static PositionInterface from(final int row, final int column) {
        return ORIGIN.apply(row, column); // TODO utiliser directement la factory
    }

    @SuppressWarnings("all")
    public static PositionInterface Position(final int row, final int column) {
        return ORIGIN.apply(row, column); // TODO utiliser directement la factory
    }

    private Position(final int row, final int column) {
        this.row = row;
        this.column = column;
        this.hashCode = computeHashCode(row, column);
    }

    public PositionInterface apply() {
        return this;
    }

    @Override
    public PositionInterface apply(final int row, final int column) {
        return row == 0 && column == 0 ? this.apply() : Factory.get(this.row() + row, this.column() + column);
    }

    @Override
    public PositionInterface apply(final DirectionInterface direction) {
        return direction == null ? this.apply() : this.apply(direction.rowDelta(), direction.columnDelta());
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (!(object instanceof PositionInterface)) return false;
        final PositionInterface that = (PositionInterface) object;
        return that.row() == this.row() && that.column() == this.column();
    }

    @Override
    public int compareTo(final PositionInterface that) {
        if (this.row() < that.row()) return -1;
        if (this.row() > that.row()) return 1;
        if (this.column() < that.column()) return -1;
        if (this.column() > that.column()) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.row() + ", " + this.column() + ")";
    }

    @Override
    public boolean isNull() {
        return false;
    }

    public static void main(final String[] args) {
        final PositionInterface position = Position(1, 2);
        System.out.println(position);
    }
}