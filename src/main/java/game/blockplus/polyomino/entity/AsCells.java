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

package game.blockplus.polyomino.entity;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.cells.Cells;
import components.cells.Directions;
import components.cells.Directions.Direction;
import components.cells.ICells;
import components.cells.Positions;
import components.cells.Positions.Position;

public final class AsCells {

    public final static String SOME = "O";
    public final static String NONE = " ";

    private final static Set<Direction> DIRECTIONS = Sets.newHashSet(Directions.TOP, Directions.RIGHT, Directions.BOTTOM, Directions.LEFT);

    private final ICells<String> cells;

    public ICells<String> getCells() {
        return this.cells;
    }

    public int rows() {
        return this.getCells().rows();
    }

    public int columns() {
        return this.getCells().columns();
    }

    public int size() {
        return this.rows() * this.columns();
    }

    public static AsCells from(final String[] data) {

        final int rows = data.length;
        final int columns = rows == 0 ? 0 : data[0].length();
        final Positions cellPositions = new Positions(rows, columns);
        final ICells<String> cells = Cells.from(cellPositions, NONE, NONE);
        final Map<Position, String> mutations = Maps.newHashMap();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                final String valueOf = String.valueOf(data[i].charAt(j));
                if (valueOf.equals(SOME)) {
                    final Position position = cellPositions.get(i, j);
                    mutations.put(position, SOME);
                }
            }
        }
        return new AsCells(cells.apply(mutations));
    }

    public static AsCells from(final Iterable<Position> positions, final Positions cellPositions) {
        // TODO retrouver automatiquement le cellPositions
        final ICells<String> cells = Cells.from(cellPositions, NONE, NONE);
        final Map<Position, String> mutations = Maps.newHashMap();
        for (final Position position : positions) {
            mutations.put(position, SOME);
        }
        return new AsCells(cells.apply(mutations));
    }

    private AsCells(final ICells<String> cells) {
        this.cells = cells;
    }

    public String getCell(final int row, final int column) {
        return this.getCells().get(row, column);
    }

    public List<Direction> getDirections(final Position position) {
        final List<Direction> directions = Lists.newArrayList();
        for (final Direction direction : DIRECTIONS)
            if (this.getCell(position.row() + direction.rowDelta(), position.column() + direction.columnDelta()).equals(SOME))
                directions.add(direction);
        return directions;
    }

    @Override
    public int hashCode() {
        return this.getCells().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        Preconditions.checkArgument(object instanceof AsCells);
        final AsCells that = (AsCells) object;
        return this.getCells().equals(that.getCells());
    }

}