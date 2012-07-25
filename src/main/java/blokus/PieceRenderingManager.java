
package blokus;

import blokus.piece.PieceInterface;

public class PieceRenderingManager {

    private static final int GRID_DIMENSION = 5; // TODO à extraire

    public String render(final PieceInterface piece) {
        final StringBuilder stringBuilder = new StringBuilder();
        final boolean[][] grid = new boolean[GRID_DIMENSION][GRID_DIMENSION];
        for (int n = 0; n < piece.getNumberOfCells(); ++n)
            grid[piece.get(0, n)][piece.get(1, n)] = true;
        for (final boolean[] row : grid) {
            for (final boolean cell : row)
                stringBuilder.append((cell ? "x" : "o") + " ");
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

}