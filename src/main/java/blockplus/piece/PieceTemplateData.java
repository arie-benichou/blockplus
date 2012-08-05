/*
 * Copyright 2012 Arie Benichou
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

package blockplus.piece;

import blockplus.piece.matrix.Matrix;

public enum PieceTemplateData {

    /*      
          NULL OBJECT
    */

    ENTRY0(new int[][] { {} }),

    /*      0  
          -----
        0 | x |
          -----
    */

    ENTRY1(new int[][] { { 0 }, { 0 } }),

    /*      0  
          -----
        0 | x |
          -----
        1 | x |
          -----
    */

    ENTRY2(new int[][] { { 0, 1 }, { 0, 0 } }),

    /*      0  
          -----
        0 | x |
          -----
        1 | x |
          -----
        2 | x |
          -----
    */

    ENTRY3(new int[][] { { 1, 0, 2 }, { 0, 0, 0 } }),

    /*      0   1  
          ---------
        0 | x | x |
          ---------
        1 | x |   |
          ---------
    */

    ENTRY4(new int[][] { { 0, 0, 1 }, { 0, 1, 0 } }),

    /*      0  
          -----
        0 | x |
          -----
        1 | x |
          -----
        2 | x |
          -----
        3 | x |
          -----
    */
    ENTRY5(new int[][] { { 0, 1, 2, 3 }, { 0, 0, 0, 0 } }),

    /*      0   1  
          ---------
        0 | x | x |
          ---------
        1 | x |   |
          ---------
        2 | x |   |
          ---------
    */

    ENTRY6(new int[][] { { 0, 0, 1, 2 }, { 0, 1, 0, 0 } }),

    /*      0   1  
          ---------
        0 | x |   |
          ---------
        1 | x | x |
          ---------
        2 | x |   |
          ---------
    */

    ENTRY7(new int[][] { { 1, 0, 1, 2 }, { 0, 0, 1, 0 } }),

    /*      0   1  
          ---------
        0 | x | x |
          ---------
        1 | x | x |
          ---------
    */

    ENTRY8(new int[][] { { 0, 0, 1, 1 }, { 0, 1, 0, 1 } }),

    /*      0   1   2  
          -------------
        0 | x | x |   |
          -------------
        1 |   | x | x |
          -------------
    */

    ENTRY9(new int[][] { { 1, 0, 0, 1 }, { 1, 0, 1, 2 } }),

    /*      0   1  
          ---------
        0 | x | x |
          ---------
        1 | x |   |
          ---------
        2 | x |   |
          ---------
        3 | x |   |
          ---------
    */

    ENTRY10(new int[][] { { 0, 0, 1, 2, 3 }, { 0, 1, 0, 0, 0 } }),

    /*      0  
          -----
        0 | x |
          -----
        1 | x |
          -----
        2 | x |
          -----
        3 | x |
          -----
        4 | x |
          -----
    */

    ENTRY11(new int[][] { { 2, 0, 1, 3, 4 }, { 0, 0, 0, 0, 0 } }),

    /*      0   1   2   3  
          -----------------
        0 | x | x |   |   |
          -----------------
        1 |   | x | x | x |
          -----------------
    */

    ENTRY12(new int[][] { { 0, 0, 1, 1, 1 }, { 0, 1, 1, 2, 3 } }),

    /*      0   1   2  
          -------------
        0 | x | x | x |
          -------------
        1 |   | x | x |
          -------------
    */

    ENTRY13(new int[][] { { 0, 0, 0, 1, 1 }, { 0, 1, 2, 1, 2 } }),

    /*      0   1  
          ---------
        0 | x | x |
          ---------
        1 |   | x |
          ---------
        2 | x | x |
          ---------
    */

    ENTRY14(new int[][] { { 1, 0, 0, 2, 2 }, { 1, 0, 1, 0, 1 } }),

    /*      0   1  
          ---------
        0 | x |   |
          ---------
        1 | x | x |
          ---------
        2 | x |   |
          ---------
        3 | x |   |
          ---------
    */

    ENTRY15(new int[][] { { 0, 1, 1, 2, 3 }, { 0, 0, 1, 0, 0 } }),

    /*      0   1   2  
          -------------
        0 | x |   |   |
          -------------
        1 | x | x | x |
          -------------
        2 | x |   |   |
          -------------
    */

    ENTRY16(new int[][] { { 1, 0, 1, 1, 2 }, { 0, 0, 1, 2, 0 } }),

    /*      0   1   2  
          -------------
        0 | x | x | x |
          -------------
        1 | x |   |   |
          -------------
        2 | x |   |   |
          -------------
    */

    ENTRY17(new int[][] { { 0, 0, 0, 1, 2 }, { 0, 1, 2, 0, 0 } }),

    /*      0   1   2  
          -------------
        0 | x | x |   |
          -------------
        1 |   | x | x |
          -------------
        2 |   |   | x |
          -------------
    */

    ENTRY18(new int[][] { { 1, 0, 0, 1, 2 }, { 1, 0, 1, 2, 2 } }),

    /*      0   1   2  
          -------------
        0 | x |   |   |
          -------------
        1 | x | x | x |
          -------------
        2 |   |   | x |
          -------------
    */

    ENTRY19(new int[][] { { 1, 0, 1, 1, 2 }, { 1, 0, 0, 2, 2 } }),

    /*      0   1   2  
          -------------
        0 | x |   |   |
          -------------
        1 | x | x | x |
          -------------
        2 |   | x |   |
          -------------
    */

    ENTRY20(new int[][] { { 1, 0, 1, 1, 2 }, { 1, 0, 0, 2, 1 } }),

    /*      0   1   2  
          -------------
        0 |   | x |   |
          -------------
        1 | x | x | x |
          -------------
        2 |   | x |   |
          -------------
    */

    ENTRY21(new int[][] { { 1, 0, 1, 1, 2 }, { 1, 1, 0, 2, 1 } });

    /*----------------------------8<----------------------------*/

    private static final String ENTRY_NAME_PATTERN = "ENTRY";
    private final static int DIMENSION = 2;

    public final static PieceTemplateData get(final int ordinal) {
        return PieceTemplateData.valueOf(ENTRY_NAME_PATTERN + ordinal);
    }

    private final Matrix matrix;
    private int numberOfCells;

    /**
     * PieceData constructor.
     * 
     * @param data
     *            The first data row contains row indexes and the second data
     *            row contains column indexes. The first [row][column] always
     *            contains the referential.
     */
    private PieceTemplateData(final int[][] data) {
        this.numberOfCells = data[0].length;
        this.matrix = new Matrix(DIMENSION, this.getNumberOfCells(), data);
    }

    public Matrix getMatrix() {
        return this.matrix;
    }

    public int getNumberOfCells() {
        return this.numberOfCells;
    }

}