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

package components.cells;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class PositionsTest {

    private IPosition referential;
    private Positions positions;

    @Before
    public void setUp() throws Exception {
        this.positions = new Positions(7, 7);
        this.referential = this.positions.get(3, 3);
    }

    @After
    public void tearDown() throws Exception {
        this.positions = null;
        this.referential = null;
    }

    @Test
    public void testGetNeighboursPositions() {
        {
            final int radius = -1;
            final Iterable<IPosition> expected = Lists.newArrayList();
            final Iterable<IPosition> neighboursPositions = this.positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 0;
            final Iterable<IPosition> expected = Lists.newArrayList(this.referential); // TODO à revoir
            final Iterable<IPosition> neighboursPositions = this.positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 1;
            final Iterable<IPosition> expected = Lists.newArrayList(
                    this.positions.get(2, 2),
                    this.positions.get(2, 3),
                    this.positions.get(2, 4),
                    this.positions.get(3, 2),
                    this.positions.get(3, 4),
                    this.positions.get(4, 2),
                    this.positions.get(4, 3),
                    this.positions.get(4, 4)

                    );
            final Iterable<IPosition> neighboursPositions = this.positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 2;
            final Iterable<IPosition> expected = Lists.newArrayList(
                    this.positions.get(1, 1),
                    this.positions.get(1, 2),
                    this.positions.get(1, 3),
                    this.positions.get(1, 4),
                    this.positions.get(1, 5),
                    this.positions.get(2, 1),
                    this.positions.get(2, 5),
                    this.positions.get(3, 1),
                    this.positions.get(3, 5),
                    this.positions.get(4, 1),
                    this.positions.get(4, 5),
                    this.positions.get(5, 1),
                    this.positions.get(5, 2),
                    this.positions.get(5, 3),
                    this.positions.get(5, 4),
                    this.positions.get(5, 5)
                    );
            final Iterable<IPosition> neighboursPositions = this.positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 3;
            final Iterable<IPosition> expected = Lists.newArrayList(
                    this.positions.get(0, 0),
                    this.positions.get(0, 1),
                    this.positions.get(0, 2),
                    this.positions.get(0, 3),
                    this.positions.get(0, 4),
                    this.positions.get(0, 5),
                    this.positions.get(0, 6),
                    this.positions.get(1, 0),
                    this.positions.get(1, 6),
                    this.positions.get(2, 0),
                    this.positions.get(2, 6),
                    this.positions.get(3, 0),
                    this.positions.get(3, 6),
                    this.positions.get(4, 0),
                    this.positions.get(4, 6),
                    this.positions.get(5, 0),
                    this.positions.get(5, 6),
                    this.positions.get(6, 0),
                    this.positions.get(6, 1),
                    this.positions.get(6, 2),
                    this.positions.get(6, 3),
                    this.positions.get(6, 4),
                    this.positions.get(6, 5),
                    this.positions.get(6, 6)
                    );
            final Iterable<IPosition> neighboursPositions = this.positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }
    }
}