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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.cells.Directions.Direction;

public final class Positions {

    public final static class Position implements IPosition {

        private final Integer id;

        //@JsonValue
        @Override
        public Integer id() {
            return this.id;
        }

        //@JsonIgnore
        private final int row;

        @Override
        public int row() {
            return this.row;
        }

        //@JsonIgnore
        private final int column;

        @Override
        public int column() {
            return this.column;
        }

        @JsonCreator
        private Position(
                @JsonProperty("id") final Integer id,
                @JsonProperty("row") final int row,
                @JsonProperty("column") final int column) {
            this.id = id;
            this.row = row;
            this.column = column;
        }

        @Override
        @JsonIgnore
        public boolean isNull() {
            return this.id == -1;
        }

        @Override
        public int hashCode() {
            return this.id();
        }

        @Override
        public boolean equals(final Object object) {
            if (object == this) return true;
            if (object == null) return false;
            if (!(object instanceof Position)) return false;
            final Position that = (Position) object;
            return that.id().equals(this.id()) && that.row() == this.row() && that.column() == this.column();
        }

        @Override
        public int compareTo(final IPosition that) {
            Preconditions.checkArgument(that != null);
            if (this.row() < that.row()) return -1;
            if (this.row() > that.row()) return 1;
            if (this.column() < that.column()) return -1;
            if (this.column() > that.column()) return 1;
            return 0;
        }

        @Override
        public String toString() {
            return Objects.toStringHelper(this).add("id", this.id()).add("row", this.row()).add("column", this.column()).toString();
        }

        /*
        public Position apply(final Direction direction) {
            return Positions.this.get(this.row() + direction.rowDelta(), this.column() + direction.columnDelta());
        }

        public Iterable<Position> apply(final Iterable<Direction> directions) {
            final Set<Position> positions = Sets.newLinkedHashSet();
            for (final Direction direction : directions) {
                final Position position = this.apply(direction);
                if (!position.isNull()) positions.add(position);
            }
            return positions;
        }
        */

    }

    private final Position NULL = new Position(-1, -1, -1);

    private static int check(final int naturalInteger) {
        Preconditions.checkArgument(naturalInteger > 0);
        return naturalInteger;
    }

    private final int rows;

    private final int columns;

    private final Map<Integer, Position> positions = Maps.newTreeMap();

    private volatile Neighbourhood neighbourhood;

    public Positions(final int rows, final int columns) {
        this.rows = check(rows);
        this.columns = check(columns);
    }

    public int rows() {
        return this.rows;
    }

    public int columns() {
        return this.columns;
    }

    private boolean isLegal(final int row, final int column) {
        return row > -1 && column > -1 && row < this.rows() && column < this.columns();
    }

    public Position get(final int row, final int column) {
        if (!this.isLegal(row, column)) return this.NULL;
        final Integer id = row * this.columns + column;
        Position instance = this.positions.get(id);
        if (instance == null) {
            this.positions.put(id, instance = new Position(id, row, column));
        }
        return instance;
    }

    private Position get(final int id, final int row, final int column) {
        return this.isLegal(row, column) ? new Position(id, row, column) : this.NULL;
    }

    public Position get(final int id) {
        Position instance = this.positions.get(id);
        if (instance == null) {
            this.positions.put(id, instance = this.get(id, id / this.columns, id % this.columns));
        }
        return instance;
    }

    private Neighbourhood neighbourhood() {
        Neighbourhood value = this.neighbourhood;
        if (value == null) synchronized (this) {
            if ((value = this.neighbourhood) == null) this.neighbourhood = value = new Neighbourhood(this);
        }
        return value;
    }

    public Iterable<Position> neighbours(final Position position, final int radius) {
        return this.neighbourhood().getNeighboursPositions(position, radius);
    }

    public Iterable<Position> neighbours(final Position position) {
        return this.neighbours(position, 1);
    }

    public Position neighbours(final Position position, final Direction direction) {
        return Positions.this.get(position.row() + direction.rowDelta(), position.column() + direction.columnDelta());
    }

    public Iterable<Position> neighbours(final Position position, final Iterable<Direction> directions) {
        final Set<Position> positions = Sets.newLinkedHashSet();
        for (final Direction direction : directions) {
            final Position neighbour = this.neighbours(position, direction);
            if (!neighbour.isNull()) positions.add(neighbour);
        }
        return positions;
    }

}