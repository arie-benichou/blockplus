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

package blockplus.piece2.matching;

import static blockplus.piece2.matching.PieceTiles.PieceTile.NONE;
import static blockplus.piece2.matching.PieceTiles.PieceTile.SOME;

import java.util.List;
import java.util.Map;

import blockplus.piece2.matching.PieceTiles.PieceTile;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import components.cells.CellsInterface;
import components.cells.immutable.Cells;
import components.direction.Direction;
import components.direction.DirectionInterface;
import components.position.Position;
import components.position.PositionInterface;

public final class PieceAsCells {

    private final static List<DirectionInterface> DIRECTIONS = Lists.newArrayList(Direction.TOP, Direction.RIGHT, Direction.BOTTOM, Direction.LEFT);

    private final CellsInterface<PieceTile> cells;

    public CellsInterface<PieceTile> getCells() {
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

    public static PieceAsCells from(final String[] data) {
        return new PieceAsCells(data);
    }

    private PieceAsCells(final String[] data) {
        final int rows = data.length;
        final int columns = rows == 0 ? 0 : data[0].length();
        final CellsInterface<PieceTile> cells = Cells.from(rows, columns, NONE, NONE);
        final Map<PositionInterface, PieceTile> mutations = Maps.newHashMap();
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                final PieceTile tile = PieceTiles.from(data[i].charAt(j));
                if (tile.is(SOME)) mutations.put(Position.Position(i, j), tile);
            }
        }
        this.cells = cells.apply(mutations);
    }

    public PieceTile getCell(final int y, final int x) {
        return this.getCells().get(y, x);
    }

    public List<DirectionInterface> getDirections(final PositionInterface position) {
        final List<DirectionInterface> directions = Lists.newArrayList();
        for (final DirectionInterface direction : DIRECTIONS)
            if (this.getCells().get(position.apply(direction)).is(SOME)) directions.add(direction);
        return directions;
    }

    @Override
    public int hashCode() {
        return this.getCells().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        Preconditions.checkArgument(object instanceof PieceAsCells);
        final PieceAsCells that = (PieceAsCells) object;
        return this.getCells().equals(that.getCells());
    }
}