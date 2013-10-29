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

package blockplus.model.polyomino;

import static components.cells.Positions.Position;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.cells.Cells;
import components.cells.Directions;
import components.cells.Directions.Direction;
import components.cells.ICells;
import components.cells.IPosition;
import components.cells.Positions;

public final class PolyominoProperties {

    private final static char NONE = ' ';

    public static PolyominoProperties from(final String[] data) {
        return new PolyominoProperties(data);
    }

    public static ICells<Integer> computeCells(final String[] data) {
        final int rows = data.length;
        final int columns = rows == 0 ? 0 : data[0].length();
        final ICells<Integer> cells = Cells.from(rows, columns, 1, 1);
        final Map<IPosition, Integer> mutations = Maps.newHashMap();
        for (int row = 0; row < rows; ++row) {
            for (int column = 0; column < columns; ++column) {
                final IPosition position = Position(row, column);
                final char cell = data[row].charAt(column);
                if (cell != NONE) mutations.put(position, Integer.parseInt(String.valueOf(cell)));
            }
        }
        return cells.apply(mutations);
    }

    private static IPosition computeReferential(final ICells<Integer> cells) {
        IPosition referential;
        final Set<IPosition> positions = cells.get().keySet();
        if (positions.isEmpty()) referential = Position(0, 0);
        else {
            int sumY = 0, sumX = 0;
            for (final IPosition position : positions) {
                sumY += position.row();
                sumX += position.column();
            }
            final double n = positions.size();
            final int refY = (int) Math.round(sumY / n - 0.1);
            final int refX = (int) Math.round(sumX / n - 0.1);
            int row = refY, column = refX;
            final Iterator<Direction> directions = Directions.ALL_AROUND.iterator();
            while (cells.get(row, column).equals(cells.initialSymbol())) {
                final Direction direction = directions.next();
                row = refY + direction.rowDelta();
                column = refX + direction.columnDelta();
            }
            referential = Position(row, column);
        }
        return referential;
    }

    private static int computeRadius(final IPosition referential, final Set<IPosition> positions) {
        final int referentialRow = referential.row(), referentialColumn = referential.column();
        int maxRowDelta = -1, maxColumnDelta = -1;
        for (final IPosition position : positions) {
            final int rowDelta = Math.abs(referentialRow - position.row()), columnDelta = Math.abs(referentialColumn - position.column());
            if (rowDelta > maxRowDelta) maxRowDelta = rowDelta;
            if (columnDelta > maxColumnDelta) maxColumnDelta = columnDelta;
        }
        return Math.max(maxRowDelta, maxColumnDelta);
    }

    private static Set<IPosition> computeSides(final Set<IPosition> positions) {
        final Set<IPosition> sides = Sets.newHashSet();
        for (final IPosition position : positions)
            sides.addAll(Positions.neighbours(position, Directions.SIDES));
        return sides;
    }

    private static Iterable<IPosition> computeShadows(final Set<IPosition> sides, final Set<IPosition> positions) {
        return Sets.difference(sides, positions);
    }

    private static Set<IPosition> computeCorners(final SortedSet<IPosition> positions) {
        final Set<IPosition> sides = Sets.newHashSet();
        for (final IPosition position : positions)
            sides.addAll(Positions.neighbours(position, Directions.CORNERS));
        return sides;
    }

    private static Iterable<IPosition> computeLights(final Set<IPosition> corners, final Set<IPosition> sides) {
        return Sets.difference(corners, sides);
    }

    private final ICells<Integer> cells;
    private final SortedSet<IPosition> positions;
    private final IPosition referential;
    private final int weight;
    private final int radius;
    private final SortedSet<IPosition> shadows;
    private final SortedSet<IPosition> lights;

    private PolyominoProperties(final String[] data) {
        this.cells = computeCells(data);
        this.positions = ImmutableSortedSet.copyOf(this.cells.get().keySet());
        this.weight = this.cells.get().size();
        this.referential = computeReferential(this.cells);
        this.radius = computeRadius(this.referential, this.positions);
        final Set<IPosition> sides = computeSides(this.positions);
        this.shadows = ImmutableSortedSet.copyOf(computeShadows(sides, this.positions));
        final Set<IPosition> corners = computeCorners(this.positions);
        this.lights = ImmutableSortedSet.copyOf(computeLights(corners, sides));
    }

    public Iterable<IPosition> positions() {
        return this.positions;
    }

    public int weight() {
        return this.weight;
    }

    public IPosition referential() {
        return this.referential;
    }

    public int radius() {
        return this.radius;
    }

    public Iterable<IPosition> shadows() {
        return this.shadows;
    }

    public Iterable<IPosition> lights() {
        return this.lights;
    }

}