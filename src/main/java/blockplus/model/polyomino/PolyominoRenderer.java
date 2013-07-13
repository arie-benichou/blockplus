
package blockplus.model.polyomino;

import java.util.SortedSet;

import blockplus.model.polyomino.PolyominoInstances.PolyominoInstance;
import blockplus.model.polyomino.PolyominoProperties.Location;

import com.google.common.collect.Sets;
import components.cells.IPosition;

public final class PolyominoRenderer {

    private static int minColumn(final SortedSet<IPosition> positions) {
        int minColumn = Integer.MAX_VALUE;
        for (final IPosition position : positions)
            if (position.column() < minColumn) minColumn = position.column();
        return minColumn;
    }

    private static int maxColumn(final SortedSet<IPosition> positions) {
        int maxColumn = Integer.MIN_VALUE;
        for (final IPosition position : positions) {
            if (position.column() > maxColumn) maxColumn = position.column();
        }
        return maxColumn;
    }

    private static IPosition topLeftCorner(final SortedSet<IPosition> positions) {
        final int minRow = positions.isEmpty() ? 0 : positions.first().row();
        final int minColumn = positions.isEmpty() ? 0 : minColumn(positions);
        return new Location(minRow, minColumn);
    }

    private static char[][] computeRendering(final Polyomino polyomino) {
        final SortedSet<IPosition> positions = (SortedSet<IPosition>) polyomino.positions();
        int rows = 3;
        int columns = 3;
        if (!positions.isEmpty()) {
            rows += positions.last().row();
            columns += maxColumn(positions);
        }
        final char[][] rendering = new char[rows][columns];
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j)
                rendering[i][j] = '·';
        for (final IPosition position : polyomino.positions())
            rendering[position.row() + 1][position.column() + 1] = 'O';
        final IPosition referential = polyomino.referential();
        rendering[referential.row() + 1][referential.column() + 1] = '0';
        for (final IPosition position : polyomino.lights())
            rendering[position.row() + 1][position.column() + 1] = '¤';
        for (final IPosition position : polyomino.shadows())
            rendering[position.row() + 1][position.column() + 1] = '•';
        return rendering;
    }

    private static SortedSet<IPosition> normalize(final SortedSet<IPosition> positions) {
        final SortedSet<IPosition> normalizedPositions = Sets.newTreeSet();
        final IPosition topLeftCornerPosition = topLeftCorner(positions);
        for (final IPosition position : positions) {
            final int row = position.row() - topLeftCornerPosition.row();
            final int column = position.column() - topLeftCornerPosition.column();
            normalizedPositions.add(new Location(row, column));
        }
        return normalizedPositions;
    }

    private static char[][] computeRendering(final SortedSet<IPosition> positions) {
        final SortedSet<IPosition> normalizedPositions = normalize(positions);
        int rows = 1;
        int columns = 1;
        if (!normalizedPositions.isEmpty()) {
            rows += normalizedPositions.last().row();
            columns += maxColumn(normalizedPositions);
        }
        final char[][] rendering = new char[rows][columns];
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j)
                rendering[i][j] = '·';
        for (final IPosition position : normalizedPositions)
            rendering[position.row()][position.column()] = 'O';
        return rendering;
    }

    private static String toString(final char[][] rendering) {
        String string = new String(rendering[0]);
        for (int i = 1; i < rendering.length; ++i)
            string = string + "\n" + new String(rendering[i]);
        return string;
    }

    public static String render(final Polyomino polyomino) {
        return toString(computeRendering(polyomino));
    }

    public static String render(final PolyominoInstance instance) {
        return toString(computeRendering((SortedSet<IPosition>) instance.positions()));
    }

    public static String render(final SortedSet<IPosition> positions) {
        return toString(computeRendering(positions));
    }

    private PolyominoRenderer() {}
}