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

import com.google.common.collect.ImmutableList;

public final class Directions {

    public final static class Direction {

        private final int rowDelta;
        private final int columnDelta;

        private Direction(final int rowDelta, final int columnDelta) {
            this.rowDelta = rowDelta;
            this.columnDelta = columnDelta;
        }

        public int rowDelta() {
            return this.rowDelta;
        }

        public int columnDelta() {
            return this.columnDelta;
        }

    }

    public static Direction Direction(final int rowDelta, final int columnDelta) {
        return new Direction(rowDelta, columnDelta);
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
    public final static Direction TOP_LEFT = Direction(-1, -1);

    /*
    -------------
    |   | x |   |
    -------------
    |   | . |   |
    -------------
    |   |   |   |
    -------------
     */
    public final static Direction TOP = Direction(-1, 0);

    /*
    -------------
    |   |   | x |
    -------------
    |   | . |   |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction TOP_RIGHT = Direction(-1, 1);

    /*
    -------------
    |   |   |   |
    -------------
    | x | . |   |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction LEFT = Direction(0, -1);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . |   |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction NULL = Direction(0, 0);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . | x |
    -------------
    |   |   |   |
    -------------
    */
    public final static Direction RIGHT = Direction(0, 1);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . |   |
    -------------
    | x |   |   |
    -------------
    */
    public final static Direction BOTTOM_LEFT = Direction(1, -1);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . |   |
    -------------
    |   | x |   |
    -------------
    */
    public final static Direction BOTTOM = Direction(1, 0);

    /*
    -------------
    |   |   |   |
    -------------
    |   | . |   |
    -------------
    |   |   | x |
    -------------
    */
    public final static Direction BOTTOM_RIGHT = Direction(1, 1);

    public final static Iterable<Direction> SIDES = ImmutableList.of(TOP, LEFT, RIGHT, BOTTOM);

    public final static Iterable<Direction> CORNERS = ImmutableList.of(TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT);

    public final static Iterable<Direction> ALL_AROUND = ImmutableList.of(TOP_LEFT, TOP, TOP_RIGHT, LEFT, RIGHT, BOTTOM_LEFT, BOTTOM, BOTTOM_RIGHT);

}