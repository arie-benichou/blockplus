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

package blockplus.piece.matrix;

import java.util.Arrays;

import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class Matrix {

    private final int numberOfRows;

    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    private final int numberOfColumns;

    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    private final int[][] data;

    private volatile Integer hashCode = null;

    public Matrix(final int numberOfRows, final int numberOfColumns, final int[][] data) {
        Preconditions.checkArgument(numberOfRows >= 0);
        Preconditions.checkArgument(numberOfColumns >= 0);
        // TODO check sur data ou bien utiliser un builder
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.data = new int[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; ++i)
            for (int j = 0; j < numberOfColumns; ++j)
                this.data[i][j] = data[i][j];
    }

    public Matrix(final int numberOfRows, final int numberOfColumns, final int value) {
        // TODO à revoir
        //Preconditions.checkArgument(numberOfRows > 0);
        //Preconditions.checkArgument(numberOfColumns > 0);
        Preconditions.checkArgument(numberOfRows >= 0);
        Preconditions.checkArgument(numberOfColumns >= 0);
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.data = new int[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; ++i)
            for (int j = 0; j < numberOfColumns; ++j)
                this.data[i][j] = value;
    }

    public Matrix(final int numberOfRows, final int numberOfColumns) {
        this(numberOfRows, numberOfColumns, 0);
    }

    @Override
    public String toString() {
        final ToStringHelper toStringHelper = Objects.toStringHelper(this)
                .addValue(this.getNumberOfRows())
                .addValue(this.getNumberOfColumns());
        final StringBuilder stringBuilder = new StringBuilder("[");
        for (int i = 0; i < this.getNumberOfRows(); ++i)
            stringBuilder.append(Arrays.toString(this.data[i]));
        stringBuilder.append("]");
        return toStringHelper.addValue(stringBuilder.toString()).toString();
    }

    @Override
    public int hashCode() {
        Integer value = this.hashCode;
        if (value == null)
            synchronized (this) {
                if ((value = this.hashCode) == null) this.hashCode = value = this.toString().hashCode();
            }
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Matrix)) return false;
        final Matrix that = (Matrix) object;
        if (this.getNumberOfRows() != that.getNumberOfRows()) return false;
        if (this.getNumberOfColumns() != that.getNumberOfColumns()) return false;
        final boolean haveSameHashCode = this.hashCode() == that.hashCode();
        boolean isEqual = true;
        for (int i = 0; isEqual && i < this.getNumberOfRows(); ++i)
            for (int j = 0; isEqual && j < this.getNumberOfColumns(); ++j)
                if (this.data[i][j] != that.data[i][j]) isEqual = false;
        Preconditions.checkState(haveSameHashCode == isEqual);
        return isEqual;
    }

    public int get(final int rowIndex, final int columnIndex) {
        Preconditions.checkArgument(rowIndex >= 0 && rowIndex < this.getNumberOfRows());
        Preconditions.checkArgument(columnIndex >= 0 && columnIndex < this.getNumberOfColumns());
        return this.data[rowIndex][columnIndex];
    }

    public Matrix transpose() {
        final Matrix result = new Matrix(this.getNumberOfColumns(), this.getNumberOfRows());
        for (int i = 0; i < this.getNumberOfColumns(); ++i)
            for (int j = 0; j < this.getNumberOfRows(); ++j)
                result.data[i][j] = this.data[j][i];
        return result;
    }

    public Matrix add(final Matrix matrix) {
        Preconditions.checkArgument(this.getNumberOfRows() == matrix.getNumberOfRows() && this.getNumberOfColumns() == matrix.getNumberOfColumns());
        final Matrix result = new Matrix(this.getNumberOfRows(), this.getNumberOfColumns());
        for (int i = 0; i < this.getNumberOfRows(); ++i)
            for (int j = 0; j < this.getNumberOfColumns(); ++j)
                result.data[i][j] = this.data[i][j] + matrix.data[i][j];
        return result;
    }

    public Matrix multiply(final Matrix matrix) {
        Preconditions.checkArgument(this.getNumberOfColumns() == matrix.getNumberOfRows());
        final Matrix result = new Matrix(this.getNumberOfRows(), matrix.getNumberOfColumns());
        for (int i = 0; i < this.getNumberOfRows(); ++i) {
            for (int j = 0; j < matrix.getNumberOfColumns(); ++j) {
                int sum = 0;
                for (int k = 0; k < this.getNumberOfColumns(); ++k)
                    sum += this.data[i][k] * matrix.data[k][j];
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    // TODO caching ou à computer lors de la construction
    public PositionInterface getPositionHavingHighestValue() {
        int max = Integer.MIN_VALUE;
        PositionInterface positionHavingHighestValue = Position.from(-1, -1);
        for (int i = 0; i < this.getNumberOfRows(); ++i)
            for (int j = 0; j < this.getNumberOfColumns(); ++j) {
                if (this.data[i][j] > max) {
                    max = this.data[i][j];
                    positionHavingHighestValue = Position.from(i, j);
                }
            }
        return positionHavingHighestValue;
    }

    // TODO caching ou à computer lors de la construction
    public PositionInterface getPositionHavingLowestValue() {
        int min = Integer.MAX_VALUE;
        PositionInterface positionHavingLowestValue = Position.from(-1, -1);
        for (int i = 0; i < this.getNumberOfRows(); ++i)
            for (int j = 0; j < this.getNumberOfColumns(); ++j) {
                if (this.data[i][j] < min) {
                    min = this.data[i][j];
                    positionHavingLowestValue = Position.from(i, j);
                }
            }
        return positionHavingLowestValue;
    }

    public void debug() {
        int max = 1;
        for (int i = 0; i < this.getNumberOfRows(); ++i)
            for (int j = 0; j < this.getNumberOfColumns(); ++j)
                max = Math.max(max, String.valueOf(this.data[i][j]).length());
        for (int i = 0; i < this.getNumberOfRows(); ++i) {
            for (int j = 0; j < this.getNumberOfColumns(); ++j)
                System.out.print(Strings.padStart(String.valueOf(this.data[i][j]), max, '0') + " ");
            System.out.println();
        }
        System.out.println();
    }

}