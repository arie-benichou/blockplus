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

package blockplus.piece.matrix;

import java.util.Arrays;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public final class Matrix {

    public final static Matrix NULL = new Matrix(0, 0);

    public static String debug(final Matrix matrix) {
        final int n = Math.max(String.valueOf(matrix.min()).length(), String.valueOf(matrix.max()).length());
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < matrix.rows(); ++i) {
            for (int j = 0; j < matrix.columns(); ++j)
                stringBuilder.append((Strings.padStart(String.valueOf(matrix.data[i][j]), n, ' ') + " "));
            stringBuilder.append('\n');
        }
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    private final int rows;

    public int rows() {
        return this.rows;
    }

    private final int columns;

    public int columns() {
        return this.columns;
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

    private volatile Integer hashCode = null;

    // TODO return null matrix if number of rows * number of columns = 0
    public Matrix(final int expectedRows, final int expectedColumns, final int[][] data) {
        Preconditions.checkArgument(expectedRows >= 0);
        Preconditions.checkArgument(expectedColumns >= 0);
        final int rows = data.length;
        Preconditions.checkArgument(expectedRows == rows, expectedRows + " != " + rows);
        final int columns = rows > 0 ? data[0].length : 0;
        Preconditions.checkArgument(expectedColumns == columns, expectedColumns + " != " + columns);
        final int[][] copy = new int[rows][columns];
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < columns; ++j) {
                final int value = data[i][j];
                copy[i][j] = value;
                min = Math.min(min, value);
                max = Math.max(max, value);
            }
        }
        this.rows = expectedRows;
        this.columns = expectedColumns;
        this.data = copy;
        this.min = min;
        this.max = max;
    }

    public Matrix(final int rows, final int columns, final int value) {
        Preconditions.checkArgument(rows >= 0);
        Preconditions.checkArgument(columns >= 0);
        this.rows = rows;
        this.columns = columns;
        this.data = new int[rows][columns];
        for (int i = 0; i < rows; ++i)
            for (int j = 0; j < columns; ++j)
                this.data[i][j] = value;
        this.min = this.max = value;
    }

    public Matrix(final int rows, final int columns) {
        this(rows, columns, 0);
    }

    public int get(final int rowIndex, final int columnIndex) {
        Preconditions.checkArgument(rowIndex >= 0 && rowIndex < this.rows());
        Preconditions.checkArgument(columnIndex >= 0 && columnIndex < this.columns());
        return this.data[rowIndex][columnIndex];
    }

    public Matrix transpose() {
        final Matrix result = new Matrix(this.columns(), this.rows());
        for (int i = 0; i < this.columns(); ++i)
            for (int j = 0; j < this.rows(); ++j)
                result.data[i][j] = this.data[j][i];
        return result;
    }

    public Matrix add(final Matrix matrix) {
        Preconditions.checkArgument(this.rows() == matrix.rows() && this.columns() == matrix.columns());
        final Matrix result = new Matrix(this.rows(), this.columns());
        for (int i = 0; i < this.rows(); ++i)
            for (int j = 0; j < this.columns(); ++j)
                result.data[i][j] = this.data[i][j] + matrix.data[i][j];
        return result;
    }

    public Matrix multiply(final Matrix matrix) {
        Preconditions.checkArgument(this.columns() == matrix.rows());
        final Matrix result = new Matrix(this.rows(), matrix.columns());
        for (int i = 0; i < this.rows(); ++i) {
            for (int j = 0; j < matrix.columns(); ++j) {
                int sum = 0;
                for (int k = 0; k < this.columns(); ++k)
                    sum += this.data[i][k] * matrix.data[k][j];
                result.data[i][j] = sum;
            }
        }
        return result;
    }

    public int min(final int rowIndex) {
        Preconditions.checkArgument(rowIndex < this.rows);
        int min = Integer.MAX_VALUE;
        final int[] row = this.data[rowIndex];
        for (final int value : row) {
            if (value == this.min()) return this.min();
            if (value < min) min = value;
        }
        return min;
    }

    public int max(final int rowIndex) {
        Preconditions.checkArgument(rowIndex < this.rows);
        int max = Integer.MIN_VALUE;
        final int[] row = this.data[rowIndex];
        for (final int value : row) {
            if (value == this.max()) return this.max();
            if (value > max) max = value;
        }
        return max;
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
        if (this.rows() != that.rows()) return false;
        if (this.columns() != that.columns()) return false;
        final boolean haveSameHashCode = this.hashCode() == that.hashCode();
        boolean isEqual = true;
        for (int i = 0; isEqual && i < this.rows(); ++i)
            for (int j = 0; isEqual && j < this.columns(); ++j)
                if (this.data[i][j] != that.data[i][j]) isEqual = false;
        Preconditions.checkState(haveSameHashCode == isEqual);
        return isEqual;
    }

    @Override
    public String toString() {
        final ToStringHelper toStringHelper = Objects.toStringHelper(this).addValue(this.rows()).addValue(this.columns());
        final StringBuilder stringBuilder = new StringBuilder('[');
        for (int i = 0; i < this.rows(); ++i)
            stringBuilder.append(Arrays.toString(this.data[i]));
        stringBuilder.append(']');
        return toStringHelper.addValue(stringBuilder.toString()).toString();
    }

}