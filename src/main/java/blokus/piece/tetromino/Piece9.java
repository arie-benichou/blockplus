
package blokus.piece.tetromino;

import matrix.Matrix;
import blokus.PieceRenderingManager;
import blokus.PieceRotationManager;
import blokus.piece.PieceInterface;

// TODO ? avoir qu'une seule classe Piece
public class Piece9 implements PieceInterface {

    /*      0   1   2   3   4
          ---------------------
        0 |   |   |   |   |   |
          ---------------------
        1 |   | x | x |   |   |
          ---------------------
        2 |   |   | x | x |   |
          ---------------------
        3 |   |   |   |   |   |
          ---------------------
        4 |   |   |   |   |   |
          ---------------------
    */

    private final static int NUMBER_OF_CELL = 4;

    /*
     * first row   : row indexes
     * second row  : column indexes
     * first column: fixed point (referential) 
     */
    private final static int[][] INITIAL_DATA = {
            { 2, 1, 1, 2 },
            { 2, 1, 2, 3 },
    };

    private final static Matrix INITIAL_MATRIX = new Matrix(2, NUMBER_OF_CELL, INITIAL_DATA);

    private final Matrix matrix;

    private Piece9(final Matrix matrix) {
        this.matrix = matrix;
    }

    public Piece9() {
        this(INITIAL_MATRIX);
    }

    @Override
    public Matrix getMatrix() {
        return this.matrix;
    }

    @Override
    public int get(final int index, final int n) {
        return this.matrix.get(index, n);
    }

    @Override
    public int getNumberOfCells() {
        return NUMBER_OF_CELL;
    }

    @Override
    public Piece9 newInstance(final Matrix matrix) {
        return new Piece9(matrix);
    }

    public static void main(final String[] args) {

        final PieceRotationManager pieceRotationManager = new PieceRotationManager();
        final PieceRenderingManager pieceRenderingManager = new PieceRenderingManager();

        final PieceInterface pieceA = new Piece9();
        System.out.println(pieceRenderingManager.render(pieceA));

        final PieceInterface pieceB = pieceRotationManager.rotate(pieceA);
        System.out.println(pieceRenderingManager.render(pieceB));

        final PieceInterface pieceC = pieceRotationManager.rotate(pieceB);
        System.out.println(pieceRenderingManager.render(pieceC));

        final PieceInterface pieceD = pieceRotationManager.rotate(pieceC);
        System.out.println(pieceRenderingManager.render(pieceD));

        final PieceInterface pieceE = pieceRotationManager.rotate(pieceD);
        System.out.println(pieceRenderingManager.render(pieceE));

    }

}