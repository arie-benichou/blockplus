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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


import org.junit.Test;

import blockplus.piece.matrix.Matrix;

public class MatrixTest {

    @Test
    public void testGetNumberOfRows() {
        assertEquals(1, new Matrix(1, 2).rows());
        assertEquals(2, new Matrix(2, 1).rows());
    }

    @Test
    public void testGetNumberOfColumns() {
        assertEquals(1, new Matrix(2, 1).columns());
        assertEquals(2, new Matrix(1, 2).columns());
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

    @Test
    public void testMin() {
        {
            final Matrix matrix = new Matrix(1, 1, -1);
            final int expected = -1;
            final int actual = matrix.min();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(1, 1, 0);
            final int expected = 0;
            final int actual = matrix.min();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(1, 1, 1);
            final int expected = 1;
            final int actual = matrix.min();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 0 }, { 1 } });
            final int expected = 0;
            final int actual = matrix.min();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 1 }, { 0 } });
            final int expected = 0;
            final int actual = matrix.min();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 1, 0 } });
            final int expected = 0;
            final int actual = matrix.min();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 0, 1 } });
            final int expected = 0;
            final int actual = matrix.min();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testMax() {
        {
            final Matrix matrix = new Matrix(1, 1, -1);
            final int expected = -1;
            final int actual = matrix.max();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(1, 1, 0);
            final int expected = 0;
            final int actual = matrix.max();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(1, 1, 1);
            final int expected = 1;
            final int actual = matrix.max();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 0 }, { 1 } });
            final int expected = 1;
            final int actual = matrix.max();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 1 }, { 0 } });
            final int expected = 1;
            final int actual = matrix.max();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 1, 0 } });
            final int expected = 1;
            final int actual = matrix.max();
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 0, 1 } });
            final int expected = 1;
            final int actual = matrix.max();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testMinRow() {
        {
            final Matrix matrix = new Matrix(1, 1, -1);
            final int expected = -1;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(1, 1, 0);
            final int expected = 0;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(1, 1, 1);
            final int expected = 1;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 0 }, { 1 } });
            final int expected = 0;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 0 }, { 1 } });
            final int expected = 1;
            final int actual = matrix.min(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 1 }, { 0 } });
            final int expected = 1;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 1 }, { 0 } });
            final int expected = 0;
            final int actual = matrix.min(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 1, 0 } });
            final int expected = 0;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 1, 0 } });
            final int expected = 0;
            final int actual = matrix.min(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 0, 1 } });
            final int expected = 0;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 0, 1 } });
            final int expected = 0;
            final int actual = matrix.min(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 0, 1 } });
            final int expected = 0;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 0, 1 } });
            final int expected = 0;
            final int actual = matrix.min(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 1, 0 } });
            final int expected = 0;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 1, 0 } });
            final int expected = 0;
            final int actual = matrix.min(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 4, 3 }, { 2, 1 } });
            final int expected = 3;
            final int actual = matrix.min(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 4, 3 }, { 2, 1 } });
            final int expected = 1;
            final int actual = matrix.min(1);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testMaxRow() {
        {
            final Matrix matrix = new Matrix(1, 1, -1);
            final int expected = -1;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(1, 1, 0);
            final int expected = 0;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(1, 1, 1);
            final int expected = 1;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 0 }, { 1 } });
            final int expected = 0;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 0 }, { 1 } });
            final int expected = 1;
            final int actual = matrix.max(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 1 }, { 0 } });
            final int expected = 1;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 1, new int[][] { { 1 }, { 0 } });
            final int expected = 0;
            final int actual = matrix.max(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 1, 0 } });
            final int expected = 1;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 1, 0 } });
            final int expected = 1;
            final int actual = matrix.max(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 0, 1 } });
            final int expected = 1;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 0, 1 } });
            final int expected = 1;
            final int actual = matrix.max(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 0, 1 } });
            final int expected = 1;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 0, 1 }, { 0, 1 } });
            final int expected = 1;
            final int actual = matrix.max(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 1, 0 } });
            final int expected = 1;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 1, 0 }, { 1, 0 } });
            final int expected = 1;
            final int actual = matrix.max(1);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 4, 3 }, { 2, 1 } });
            final int expected = 4;
            final int actual = matrix.max(0);
            assertEquals(expected, actual);
        }
        {
            final Matrix matrix = new Matrix(2, 2, new int[][] { { 4, 3 }, { 2, 1 } });
            final int expected = 2;
            final int actual = matrix.max(1);
            assertEquals(expected, actual);
        }
    }
}