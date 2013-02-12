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

package blockplus.piece;

import static components.position.Position.Position;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import blockplus.piece.matrix.Matrix;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import components.position.NullPosition;
import components.position.PositionInterface;

enum PieceTypeData {

    /*      
          NULL OBJECT
    */

    ENTRY0(NullPosition.getInstance(), new int[2][0]),

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

    ENTRY5(new int[][] { { 1, 0, 2, 3 }, { 0, 0, 0, 0 } }),

    /*      0   1  
          ---------
        0 | x | x |
          ---------
        1 | x |   |
          ---------
        2 | x |   |
          ---------
    */

    ENTRY6(new int[][] { { 1, 0, 0, 2 }, { 0, 0, 1, 0 } }),

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

    ENTRY10(new int[][] { { 1, 0, 0, 2, 3 }, { 0, 0, 1, 0, 0 } }),

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

    ENTRY12(new int[][] { { 1, 0, 0, 1, 1 }, { 1, 0, 1, 2, 3 } }),

    /*      0   1   2  
          -------------
        0 | x | x | x |
          -------------
        1 |   | x | x |
          -------------
    */

    ENTRY13(new int[][] { { 1, 0, 0, 0, 1 }, { 1, 0, 1, 2, 2 } }),

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

    ENTRY15(new int[][] { { 1, 0, 1, 2, 3 }, { 0, 0, 1, 0, 0 } }),

    /*      0   1   2  
          -------------
        0 | x |   |   |
          -------------
        1 | x | x | x |
          -------------
        2 | x |   |   |
          -------------
    */

    ENTRY16(new int[][] { { 1, 1, 0, 1, 2 }, { 1, 0, 0, 2, 0 } }),

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

    private final static String ENTRY_NAME_PATTERN = "ENTRY";

    private final static int DIMENSION = 2;

    private static PositionInterface extractPosition(final int[][] data, final int n) {
        return Position(data[0][n], data[1][n]);
    }

    private static Set<PositionInterface> extractPositions(final int[][] data, final int size) {
        final Builder<PositionInterface> builder = new ImmutableSortedSet.Builder<PositionInterface>(Ordering.natural());
        for (int i = 0; i < size; ++i) {
            builder.add(extractPosition(data, i));
        }
        return builder.build();
    }

    private static PositionInterface extractImplicitReferential(final int[][] data) {
        return extractPosition(data, 0);
    }

    private static int computeRadius(final PositionInterface referential, final Matrix matrix) {
        if (referential.isNull()) return -1;
        final int refY = referential.row(), refX = referential.column();
        final int minY = matrix.min(0), minX = matrix.min(1);
        final int maxY = matrix.max(0), maxX = matrix.max(1);
        final List<Integer> deltas = Lists.newArrayList(
                Math.abs(refY - minY), Math.abs(refY - maxY),
                Math.abs(refX - minX), Math.abs(refX - maxX));
        return Collections.max(deltas);
    }

    @SuppressWarnings("all")
    final static PieceTypeData PieceData(final int ordinal) {
        return PieceTypeData.valueOf(ENTRY_NAME_PATTERN + ordinal);
    }

    private final PositionInterface referential;

    public PositionInterface referential() {
        return this.referential;
    }

    private final Set<PositionInterface> positions;

    public Set<PositionInterface> positions() {
        return this.positions;
    }

    private final Matrix matrix;

    public Matrix matrix() {
        return this.matrix;
    }

    private int radius;

    public int radius() {
        return this.radius;
    }

    private int size;

    public int size() {
        return this.size;
    }

    /**
     * PieceData constructor.
     * 
     * @param referential
     *            The fixed point in transformation
     * 
     * @param data
     *            The first data row contains row indexes and the second data
     *            row contains column indexes.
     */
    private PieceTypeData(final PositionInterface referential, final int[][] data) {
        this.size = data.length == 0 ? 0 : data[0].length;
        this.positions = extractPositions(data, this.size());
        Preconditions.checkArgument(this.positions().size() == this.size());
        this.referential = referential;
        this.matrix = new Matrix(DIMENSION, this.size(), data);
        this.radius = computeRadius(this.referential(), this.matrix());
    }

    /**
     * Alternative of PieceData constructor.
     * 
     * @param data
     *            The first data row contains row indexes and the second data
     *            row contains column indexes. The referential will be extracted
     *            from the first [row][column].
     */
    private PieceTypeData(final int[][] data) {
        this(extractImplicitReferential(data), data);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("size", this.size())
                .add("radius", this.radius())
                .add("positions", this.positions())
                .add("matrix", this.matrix())
                .add("referential", this.referential())
                .toString();
    }

}