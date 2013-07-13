
package blockplus.model.polyomino;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.annotation.Nullable;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.cells.Cells;
import components.cells.Directions;
import components.cells.Directions.Direction;
import components.cells.ICells;
import components.cells.IPosition;
import components.cells.Positions;

public final class PolyominoProperties {

    public final static class Location implements IPosition {

        private final int column;
        private final int row;

        public Location(final int row, final int column) {
            this.row = row;
            this.column = column;
        }

        public Location(final IPosition position) {
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

        public Location apply(final Direction direction) {
            return new Location(this.row() + direction.rowDelta(), this.column() + direction.columnDelta());
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
        public boolean isNull() { // TODO à revoir dans Cells
            return false;
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

    public static ICells<Integer> computeCells(final String[] data) {
        final int rows = data.length;
        final int columns = rows == 0 ? 0 : data[0].length();
        final Positions cellPositions = new Positions(rows, columns);
        final ICells<Integer> cells = Cells.from(cellPositions, 1, 1);
        final Map<IPosition, Integer> mutations = Maps.newHashMap();
        for (int row = 0; row < rows; ++row) {
            for (int column = 0; column < columns; ++column) {
                final IPosition position = cellPositions.get(row, column);
                final char cell = data[row].charAt(column);
                if (cell != Polyomino.NONE) mutations.put(position, Integer.parseInt(String.valueOf(cell)));
            }
        }
        return cells.apply(mutations);
    }

    private static IPosition computeReferential(final ICells<Integer> cells) {
        IPosition referential;
        final Set<IPosition> positions = cells.get().keySet();
        if (positions.isEmpty()) referential = new Location(0, 0);
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
            referential = new Location(row, column);
        }
        return referential;
    }

    private static List<IPosition> computePositions(final Set<IPosition> position) {
        return Lists.transform(Lists.newArrayList(position), new Function<IPosition, IPosition>() {
            @Override
            public IPosition apply(@Nullable final IPosition position) {
                return new Location(position);
            }
        });
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
            for (final Direction direction : Directions.SIDES)
                sides.add(new Location(position.row(), position.column()).apply(direction));
        return sides;
    }

    private static Iterable<IPosition> computeShadows(final Set<IPosition> sides, final Set<IPosition> positions) {
        return Sets.difference(sides, positions);
    }

    private static Set<IPosition> computeCorners(final SortedSet<IPosition> positions) {
        final Set<IPosition> sides = Sets.newHashSet();
        for (final IPosition position : positions)
            for (final Direction direction : Directions.CORNERS)
                sides.add(new Location(position.row(), position.column()).apply(direction));
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

    public PolyominoProperties(final String[] data) {
        this.cells = computeCells(data);
        this.positions = ImmutableSortedSet.copyOf(computePositions(this.cells.get().keySet()));
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