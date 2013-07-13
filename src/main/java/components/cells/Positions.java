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
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.cells.Directions.Direction;

// TODO caching factory
public final class Positions {

    public final static class Position implements IPosition {

        private final int column;
        private final int row;

        public Position(final int row, final int column) {
            this.row = row;
            this.column = column;
        }

        public Position(final IPosition position) {
            this(position.row(), position.column());
        }

        @Override
        public int row() {
            return this.row;
        }

        @Override
        public int column() {
            return this.column;
        }

        @Override
        public Position apply(final int rowDelta, final int columnDelta) {
            return new Position(this.row() + rowDelta, this.column() + columnDelta);
        }

        @Override
        public Position apply(final Direction direction) {
            return this.apply(direction.rowDelta(), direction.columnDelta());
        }

        @Override
        public int compareTo(final IPosition that) {
            if (this.row() < that.row()) return -1;
            if (this.row() > that.row()) return 1;
            if (this.column() < that.column()) return -1;
            if (this.column() > that.column()) return 1;
            return 0;
        }

        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }

        @Override
        public boolean equals(final Object that) {
            Preconditions.checkArgument(that instanceof IPosition);
            return this.hashCode() == that.hashCode();
        }

        @Override
        public String toString() {
            return Lists.newArrayList(this.row(), this.column()).toString();
        }

    }

    private final static class Neighbourhood {

        private final static Map<IPosition, Map<Integer, Iterable<IPosition>>> NEIGHBOURS_BY_RADIUS_BY_POSITION = Maps.newTreeMap();

        private static Iterable<IPosition> computeNeighbours(final IPosition position, final int radius) {
            final Builder<IPosition> builder = ImmutableList.builder();
            for (int i = -radius; i <= radius; ++i) {
                final int absi = Math.abs(i);
                for (int j = -radius; j <= radius; ++j)
                    if (absi == radius || Math.abs(j) == radius) builder.add(position.apply(i, j));
            }
            return builder.build();
        }

        public static Iterable<IPosition> getNeighboursPositions(final IPosition position, final int radius) {
            Map<Integer, Iterable<IPosition>> neighboursByRadius = NEIGHBOURS_BY_RADIUS_BY_POSITION.get(position);
            if (neighboursByRadius == null) NEIGHBOURS_BY_RADIUS_BY_POSITION.put(position, neighboursByRadius = Maps.newTreeMap());
            Iterable<IPosition> neighbours = neighboursByRadius.get(radius);
            if (neighbours == null) neighboursByRadius.put(radius, neighbours = computeNeighbours(position, radius));
            return neighbours;
        }

        private Neighbourhood() {}

    }

    /*
    public Iterable<Position> apply(final Iterable<Direction> directions) {
        final Set<Position> positions = Sets.newLinkedHashSet();
        for (final Direction direction : directions) {
            final Position position = this.apply(direction);
            if (!position.isNull()) positions.add(position);
        }
        return positions;
    }
    */

    private final int rows;

    private final int columns;

    public Positions(final int rows, final int columns) {
        this.rows = rows;
        this.columns = columns;
    }

    public int rows() {
        return this.rows;
    }

    public int columns() {
        return this.columns;
    }

    public IPosition get(final int row, final int column) {
        return new Position(row, column);
        /*
        if (!this.isLegal(row, column)) return this.NULL;
        final Integer id = row * this.columns + column;
        Position instance = this.positions.get(id);
        if (instance == null) {
            this.positions.put(id, instance = new Position(id, row, column));
        }
        return instance;
        */
    }

    public Iterable<IPosition> neighbours(final IPosition position, final int radius) {
        return Neighbourhood.getNeighboursPositions(position, radius);
    }

    public Iterable<IPosition> neighbours(final IPosition position) {
        return this.neighbours(position, 1);
    }

    public IPosition neighbours(final IPosition position, final Direction direction) {
        return Positions.this.get(position.row() + direction.rowDelta(), position.column() + direction.columnDelta());
    }

    public Iterable<IPosition> neighbours(final IPosition position, final Iterable<Direction> directions) {
        final Set<IPosition> positions = Sets.newLinkedHashSet();
        for (final Direction direction : directions)
            positions.add(this.neighbours(position, direction));
        return positions;
    }

}