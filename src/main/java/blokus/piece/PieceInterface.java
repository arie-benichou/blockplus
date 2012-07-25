
package blokus.piece;

import matrix.Matrix;

public interface PieceInterface {

    Matrix getMatrix();

    int getNumberOfCells();

    PieceInterface newInstance(final Matrix matrix);

    int get(final int index, final int n); // TODO ? getReferentialCell

}