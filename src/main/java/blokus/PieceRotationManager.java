
package blokus;

import java.util.Arrays;
import java.util.List;

import matrix.Matrix;
import blokus.piece.PieceInterface;
import blokus.piece.domino.Piece2;
import blokus.piece.monomino.Piece1;
import blokus.piece.pentomino.Piece10;
import blokus.piece.pentomino.Piece11;
import blokus.piece.pentomino.Piece12;
import blokus.piece.pentomino.Piece13;
import blokus.piece.pentomino.Piece14;
import blokus.piece.pentomino.Piece15;
import blokus.piece.pentomino.Piece16;
import blokus.piece.pentomino.Piece17;
import blokus.piece.pentomino.Piece18;
import blokus.piece.pentomino.Piece19;
import blokus.piece.pentomino.Piece20;
import blokus.piece.pentomino.Piece21;
import blokus.piece.tetromino.Piece5;
import blokus.piece.tetromino.Piece6;
import blokus.piece.tetromino.Piece7;
import blokus.piece.tetromino.Piece8;
import blokus.piece.tetromino.Piece9;
import blokus.piece.triomino.Piece3;
import blokus.piece.triomino.Piece4;

import com.google.common.collect.Lists;

public class PieceRotationManager {

    /*      0   1   2   3   4
          ---------------------
        0 |   |   |   |   |   |
          ---------------------
        1 |   |   |   |   |   |
          ---------------------
        2 |   |   | * |   |   |
          ---------------------
        3 |   |   |   |   |   |
          ---------------------
        4 |   |   |   |   |   |
          ---------------------
    */

    private final static int FIXED_ROW_INDEX = 2;
    private final static int FIXED_COLUMN_INDEX = 2;

    private final static Matrix ROTATION = new Matrix(2, 2, new int[][] { { 0, -1 }, { 1, 0 } });

    private static int computeDelta(final int k, final int fixedIndex) {
        int delta = 0;
        if (k < fixedIndex) delta = fixedIndex - k;
        else if (k > fixedIndex) delta = k - fixedIndex;
        return delta;
    }

    public PieceInterface rotate(final PieceInterface piece) {
        final Matrix rotated = ROTATION.multiply(piece.getMatrix());
        final int deltaY = PieceRotationManager.computeDelta(rotated.get(0, 0), FIXED_ROW_INDEX);
        final int deltaX = PieceRotationManager.computeDelta(rotated.get(1, 0), FIXED_COLUMN_INDEX);
        final int[] delta1 = new int[piece.getNumberOfCells()];
        Arrays.fill(delta1, deltaY);
        final int[] delta2 = new int[piece.getNumberOfCells()];
        Arrays.fill(delta2, deltaX);
        final int[][] delta = { delta1, delta2 };
        final Matrix matrix = rotated.add(new Matrix(2, piece.getNumberOfCells(), delta));
        return piece.newInstance(matrix); // TODO factory 
    }

    public static void main(final String[] args) {

        final PieceRotationManager pieceRotationManager = new PieceRotationManager();
        final PieceRenderingManager pieceRenderingManager = new PieceRenderingManager();

        final List<PieceInterface> pieces = Lists.newArrayList(
                new Piece1(),
                new Piece2(),
                new Piece3(), new Piece4(),
                new Piece5(), new Piece6(), new Piece7(), new Piece8(), new Piece9(),
                new Piece10(), new Piece11(), new Piece12(), new Piece13(), new Piece14(), new Piece15(),
                new Piece16(), new Piece17(), new Piece18(), new Piece19(), new Piece20(), new Piece21());

        for (final PieceInterface piece : pieces) {

            System.out.println("--------------------8<--------------------");
            System.out.println();
            System.out.println(piece.getClass().getSimpleName());
            System.out.println();

            final PieceInterface pieceA = piece;
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
}