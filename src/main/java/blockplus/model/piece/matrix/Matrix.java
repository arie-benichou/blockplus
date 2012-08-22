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

package blockplus.model.piece.matrix;

import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class Matrix {

    public final static Matrix NULL = new Matrix(0, 0);

    private final int numberOfRows;

    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    private final int numberOfColumns;

    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    private final int min;

    public int min() {
        return this.min;
    }

    private final int max;

    public int max() {
        return this.max;
    }

    private final int[][] data;

    private transient volatile Integer hashCode = null;

    // TODO retourner la matrice nulle si numberOfRows * numberOfColumns = 0
    public Matrix(final int numberOfRows, final int numberOfColumns, final int[][] data) {

        Preconditions.checkArgument(numberOfRows >= 0);
        Preconditions.checkArgument(numberOfColumns >= 0);

        final int rows = data.length;
        Preconditions.checkArgument(numberOfRows == rows, numberOfRows + " != " + rows);

        final int columns = rows > 0 ? data[0].length : 0;
        Preconditions.checkArgument(numberOfColumns == columns, numberOfColumns + " != " + columns);

        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.data = new int[numberOfRows][numberOfColumns];

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        for (int i = 0; i < numberOfRows; ++i) {
            for (int j = 0; j < numberOfColumns; ++j) {
                final int value = data[i][j];
                this.data[i][j] = value;
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        }

        this.min = min;
        this.max = max;

    }

    public Matrix(final int numberOfRows, final int numberOfColumns, final int value) {

        Preconditions.checkArgument(numberOfRows >= 0);
        Preconditions.checkArgument(numberOfColumns >= 0);

        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.data = new int[numberOfRows][numberOfColumns];

        for (int i = 0; i < numberOfRows; ++i)
            for (int j = 0; j < numberOfColumns; ++j)
                this.data[i][j] = value;

        this.min = this.max = value;
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

    public int min(final int rowIndex) {
        Preconditions.checkArgument(rowIndex < this.numberOfRows);
        int min = Integer.MAX_VALUE;
        final int[] row = this.data[rowIndex];
        for (final int value : row) {
            if (value == this.min()) return this.min();
            if (value < min) min = value;
        }
        return min;
    }

    public int max(final int rowIndex) {
        Preconditions.checkArgument(rowIndex < this.numberOfRows);
        int max = Integer.MIN_VALUE;
        final int[] row = this.data[rowIndex];
        for (final int value : row) {
            if (value == this.max()) return this.max();
            if (value > max) max = value;
        }
        return max;
    }

    public void debug() {
        final int n = Math.max(String.valueOf(this.min()).length(), String.valueOf(this.max()).length());
        for (int i = 0; i < this.getNumberOfRows(); ++i) {
            for (int j = 0; j < this.getNumberOfColumns(); ++j)
                System.out.print(Strings.padStart(String.valueOf(this.data[i][j]), n, ' ') + " ");
            System.out.println();
        }
        System.out.println();
    }

}