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

package components.cells;

import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public final class Directions {

    public final static class Direction implements Comparable<Direction> {

        private final int id;
        private final int rowDelta;
        private final int columnDelta;

        private Direction(final int id, final int rowDelta, final int columnDelta) {
            this.id = id;
            this.rowDelta = rowDelta;
            this.columnDelta = columnDelta;
        }

        public int rowDelta() {
            return this.rowDelta;
        }

        public int columnDelta() {
            return this.columnDelta;
        }

        @Override
        public int hashCode() {
            return this.id;
        }

        @Override
        public boolean equals(final Object object) {
            if (object == this) return true;
            if (object == null) return false;
            if (!(object instanceof Direction)) return false;
            final Direction that = (Direction) object;
            return this.id == that.id;
        }

        @Override
        public int compareTo(final Direction that) {
            if (this.rowDelta() < that.rowDelta()) return -1;
            if (this.rowDelta() > that.rowDelta()) return 1;
            if (this.columnDelta() < that.columnDelta()) return -1;
            if (this.columnDelta() > that.columnDelta()) return 1;
            return 0;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("rowDelta", this.rowDelta()).add("columnDelta", this.columnDelta()).toString();
        }

    }

    private final static Map<Integer, Direction> DIRECTIONS = Maps.newTreeMap();

    public static Direction get(final int rowDelta, final int columnDelta) {
        final int id = (rowDelta + "|" + columnDelta).hashCode();
        Direction instance = DIRECTIONS.get(id);
        if (instance == null) DIRECTIONS.put(id, instance = new Direction(id, rowDelta, columnDelta));
        return instance;
    }

    /*
    -------------
    | x |   |   |
    -------------
    |   | . |   |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction TOP_LEFT = Directions.get(-1, -1);

    /*
    -------------
    |   | x |   |
    -------------
    |   | . |   |
    -------------
    |   |   |   |
    -------------
     */
    public final static Direction TOP = Directions.get(-1, 0);

    /*
    -------------
    |   |   | x |
    -------------
    |   | . |   |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction TOP_RIGHT = Directions.get(-1, 1);

    /*
    -------------
    |   |   |   |
    -------------
    | x | . |   |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction LEFT = Directions.get(0, -1);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . |   |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction NULL = Directions.get(0, 0);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . | x |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction RIGHT = Directions.get(0, 1);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . |   |
    -------------
    | x |   |   |
    -------------
    */
    public final static Direction BOTTOM_LEFT = Directions.get(1, -1);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . |   |
    -------------
    |   | x |   |
    -------------
    */
    public final static Direction BOTTOM = Directions.get(1, 0);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . |   |
    -------------
    |   |   | x |
    -------------
    */
    public final static Direction BOTTOM_RIGHT = Directions.get(1, 1);

    public final static Iterable<Direction> SIDES = ImmutableList.of(TOP, LEFT, RIGHT, BOTTOM);

    public final static Iterable<Direction> CORNERS = ImmutableList.of(TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT);

    public final static Iterable<Direction> ALL_AROUND = ImmutableList.of(TOP_LEFT, TOP, TOP_RIGHT, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT);

}