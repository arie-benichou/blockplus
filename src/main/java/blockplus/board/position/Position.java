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

package blockplus.board.position;

import java.util.Map;

import blockplus.board.direction.DirectionInterface;

import com.google.common.collect.Maps;

public final class Position implements PositionInterface {

    /*-------------------------------------8<-------------------------------------*/

    public final static PositionInterface NULL = new Position(0, 0);

    /*-------------------------------------8<-------------------------------------*/

    private final static int computeHashCode(final int row, final int column) {
        //return (17 * 31 + row) * 31 + column;
        return (row + "|" + column).hashCode();
    }

    /*-------------------------------------8<-------------------------------------*/

    public final static class Factory { // TODO à revoir

        private static int cacheHits;

        private final static Map<Integer, PositionInterface> CACHE = Maps.newHashMap();

        public static PositionInterface get(final int row, final int column) {
            //if (row < 0) row = 0;
            //if (column < 0) column = 0;
            //if (row == -1 || column == -1) return NULL;
            final int address = computeHashCode(row, column);
            PositionInterface instance = CACHE.get(address);
            if (instance == null) {
                instance = new Position(row, column);
                CACHE.put(address, instance);
            }
            else ++cacheHits;
            return instance;
        }

        public final static int size() {
            return CACHE.size();
        }

        public final static int cacheHits() {
            return cacheHits;
        }

    }

    /*-------------------------------------8<-------------------------------------*/

    private final int row;

    @Override
    public int row() {
        return this.row;
    }

    /*-------------------------------------8<-------------------------------------*/

    private final int column;

    @Override
    public int column() {
        return this.column;
    }

    /*-------------------------------------8<-------------------------------------*/

    private final int hashCode;

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    /*-------------------------------------8<-------------------------------------*/

    public static PositionInterface from(final int row, final int column) {
        return NULL.apply(row, column);
    }

    private Position(final int row, final int column) {
        this.row = row;
        this.column = column;
        this.hashCode = computeHashCode(row, column);
    }

    /*-------------------------------------8<-------------------------------------*/

    public PositionInterface apply() {
        return this;
    }

    @Override
    public PositionInterface apply(final int row, final int column) {
        return row == this.row() && column == this.column() ? this.apply() : Factory.get(row, column);
    }

    @Override
    public PositionInterface apply(final DirectionInterface direction) {
        return direction == null ? this.apply() : this.apply(this.row() + direction.rowDelta(), this.column() + direction.columnDelta());
    }

    /*-------------------------------------8<-------------------------------------*/

    @Override
    public boolean equals(final Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (!(object instanceof PositionInterface)) return false;
        final PositionInterface that = (PositionInterface) object;
        //if (that.hashCode() != this.hashCode()) return false;
        return that.row() == this.row() && that.column() == this.column();
    }

    /*-------------------------------------8<-------------------------------------*/

    @Override
    public int compareTo(final PositionInterface that) {
        if (this.row() < that.row()) return -1;
        if (this.row() > that.row()) return 1;
        if (this.column() < that.column()) return -1;
        if (this.column() > that.column()) return 1;
        return 0;
    }

    /*-------------------------------------8<-------------------------------------*/

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(" + this.row() + ", " + this.column() + ")";
    }

    /*-------------------------------------8<-------------------------------------*/

    public boolean isNull() {
        return this.equals(NULL);
        //return this.row() == 0 && this.column() == 0;
    }
}