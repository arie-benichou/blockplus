
package matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import blockplus.matrix.Matrix;

// TODO tester hashCode() et equals()
// TODO tester getPositionHavingLowestValue()
// TODO tester getPositionHavingHighestValue()
public class MatrixTest {

    @Test
    public void testGetNumberOfRows() {
        assertEquals(1, new Matrix(1, 2).getNumberOfRows());
        assertEquals(2, new Matrix(2, 1).getNumberOfRows());
    }

    @Test
    public void testGetNumberOfColumns() {
        assertEquals(1, new Matrix(2, 1).getNumberOfColumns());
        assertEquals(2, new Matrix(1, 2).getNumberOfColumns());
    }

    @Test
    public void testGet() {
        assertEquals(0, new Matrix(1, 1).get(0, 0));
    }

    @Test
    public void testMatrixIntInt() {
        {
            final Matrix matrix = new Matrix(1, 1);
            assertEquals(0, matrix.get(0, 0));
        }
        {
            final Matrix matrix = new Matrix(1, 2);
            assertEquals(0, matrix.get(0, 0));
            assertEquals(0, matrix.get(0, 1));
        }
        {
            final Matrix matrix = new Matrix(2, 2);
            assertEquals(0, matrix.get(0, 0));
            assertEquals(0, matrix.get(0, 1));
            assertEquals(0, matrix.get(1, 0));
            assertEquals(0, matrix.get(1, 1));
        }
    }

    @Test
    public void testMatrixIntIntInt() {
        {
            final Matrix matrix = new Matrix(1, 1, 1);
            assertEquals(1, matrix.get(0, 0));
        }
        {
            final Matrix matrix = new Matrix(1, 2, 1);
            assertEquals(1, matrix.get(0, 0));
            assertEquals(1, matrix.get(0, 1));
        }
        {
            final Matrix matrix = new Matrix(2, 2, 1);
            assertEquals(1, matrix.get(0, 0));
            assertEquals(1, matrix.get(0, 1));
            assertEquals(1, matrix.get(1, 0));
            assertEquals(1, matrix.get(1, 1));
        }
    }

    @Test
    public void testMatrixIntIntIntArrayArray() {
        {
            final int[][] data = { { 0 } };
            final Matrix matrix = new Matrix(1, 1, data);
            assertEquals(0, matrix.get(0, 0));
        }
        {
            final int[][] data = { { 1, 2 } };
            final Matrix matrix = new Matrix(1, 2, data);
            assertEquals(1, matrix.get(0, 0));
            assertEquals(2, matrix.get(0, 1));
        }
        {
            final int[][] data = { { 1, 2 }, { 3, 4 } };
            final Matrix matrix = new Matrix(2, 2, data);
            assertEquals(1, matrix.get(0, 0));
            assertEquals(2, matrix.get(0, 1));
            assertEquals(3, matrix.get(1, 0));
            assertEquals(4, matrix.get(1, 1));
        }
    }

    @Test
    public void testTranspose() {
        {
            final Matrix matrix = new Matrix(1, 1);
            final Matrix expected = matrix;
            final Matrix actual = matrix.transpose();
            assertTrue(expected.equals(actual));
        }
        {
            final int[][] data = { { 1, 2 } };
            final Matrix matrix = new Matrix(1, 2, data);
            final Matrix actual = matrix.transpose();
            assertFalse(matrix.equals(actual));
            final Matrix expected = new Matrix(2, 1, new int[][] { { 1 }, { 2 } });
            assertTrue(expected.equals(actual));
        }
        {
            final int[][] data = { { 1, 2 }, { 3, 4 } };
            final Matrix matrix = new Matrix(2, 2, data);
            final Matrix actual = matrix.transpose();
            assertFalse(matrix.equals(actual));
            final Matrix expected = new Matrix(2, 2, new int[][] { { 1, 3 }, { 2, 4 } });
            assertTrue(expected.equals(actual));
        }
    }

    @Test
    public void testAdd() {
        {
            final Matrix matrix = new Matrix(1, 1, 1);
            final Matrix expected = new Matrix(1, 1, 2);
            final Matrix actual = matrix.add(matrix);
            assertTrue(expected.equals(actual));
        }
        {
            final int[][] data = { { 1, 2 } };
            final Matrix matrix = new Matrix(1, 2, data);
            final Matrix actual = matrix.add(new Matrix(1, 2, 1));
            assertFalse(matrix.equals(actual));
            final Matrix expected = new Matrix(1, 2, new int[][] { { 2, 3 } });
            assertTrue(expected.equals(actual));
        }
        {
            final int[][] data = { { 1, 2 }, { 3, 4 } };
            final Matrix matrix = new Matrix(2, 2, data);
            final Matrix actual = matrix.add(new Matrix(2, 2, 0));
            assertTrue(matrix.equals(actual));
        }
    }

    @Test
    public void testMultiply() {
        {
            final Matrix matrix = new Matrix(1, 1, 1);
            final Matrix expected = matrix;
            final Matrix actual = matrix.multiply(matrix);
            assertTrue(expected.equals(actual));
        }
        {
            final int[][] data = { { 1, 2 } };
            final Matrix matrix = new Matrix(1, 2, data);
            final Matrix actual = matrix.multiply(matrix.transpose());
            assertFalse(matrix.equals(actual));
            final Matrix expected = new Matrix(1, 1, 5);
            assertTrue(expected.equals(actual));
        }
        {
            final int[][] data = { { 1, 2 }, { 3, 4 } };
            final Matrix matrix = new Matrix(2, 2, data);
            final Matrix identity = new Matrix(2, 2, new int[][] { { 1, 0 }, { 0, 1 } });
            final Matrix actual = matrix.multiply(identity);
            assertTrue(matrix.equals(actual));
        }
        {
            final int[][] data = { { 1, 2 }, { 3, 4 } };
            final Matrix matrix = new Matrix(2, 2, data);
            final Matrix actual = matrix.multiply(matrix);
            assertFalse(matrix.equals(actual));
            final Matrix expected = new Matrix(2, 2, new int[][] { { 7, 10 }, { 15, 22 } });
            assertTrue(expected.equals(actual));

        }
    }
}