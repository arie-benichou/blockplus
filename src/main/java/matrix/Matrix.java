
package matrix;

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

    public Matrix(final int numberOfRows, final int numberOfColumns, final int[][] data) {
        Preconditions.checkArgument(numberOfRows > 0);
        Preconditions.checkArgument(numberOfColumns > 0);
        // TODO check sur data ou bien utiliser un builder
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.data = new int[numberOfRows][numberOfColumns];
        for (int i = 0; i < numberOfRows; ++i)
            for (int j = 0; j < numberOfColumns; ++j)
                this.data[i][j] = data[i][j];
    }

    public Matrix(final int numberOfRows, final int numberOfColumns, final int value) {
        Preconditions.checkArgument(numberOfRows > 0);
        Preconditions.checkArgument(numberOfColumns > 0);
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
    public int hashCode() {
        return this.data.hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Matrix)) return false;
        final Matrix that = (Matrix) object;
        if (this.getNumberOfRows() != that.getNumberOfRows()) return false;
        if (this.getNumberOfColumns() != that.getNumberOfColumns()) return false;
        for (int i = 0; i < this.getNumberOfRows(); ++i)
            for (int j = 0; j < this.getNumberOfColumns(); ++j)
                if (this.data[i][j] != that.data[i][j]) return false;
        return true;
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

    public void debug() {
        int n = 1;
        for (int i = 0; i < this.getNumberOfRows(); ++i)
            for (int j = 0; j < this.getNumberOfColumns(); ++j)
                n = Math.max(n, ("" + this.data[i][j]).length());
        //System.out.println();
        for (int i = 0; i < this.getNumberOfRows(); ++i) {
            for (int j = 0; j < this.getNumberOfColumns(); ++j)
                System.out.print(Strings.padStart(String.valueOf(this.data[i][j]), n, '0') + " ");
            System.out.println();
        }
        System.out.println();
    }

}