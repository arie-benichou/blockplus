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

import static components.cells.Positions.Position;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;

public class PositionsTest {

    private IPosition referential;

    @Before
    public void setUp() throws Exception {
        this.referential = Position(3, 3);
    }

    @After
    public void tearDown() throws Exception {
        this.referential = null;
    }

    @Test
    public void testGetNeighboursPositions() {
        {
            final int radius = -1;
            final Iterable<IPosition> expected = Lists.newArrayList();
            final Iterable<IPosition> neighboursPositions = Positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 0;
            final Iterable<IPosition> expected = Lists.newArrayList(this.referential); // TODO à revoir
            final Iterable<IPosition> neighboursPositions = Positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 1;
            final Iterable<IPosition> expected = Lists.newArrayList(
                    Position(2, 2),
                    Position(2, 3),
                    Position(2, 4),
                    Position(3, 2),
                    Position(3, 4),
                    Position(4, 2),
                    Position(4, 3),
                    Position(4, 4)

                    );
            final Iterable<IPosition> neighboursPositions = Positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 2;
            final Iterable<IPosition> expected = Lists.newArrayList(
                    Position(1, 1),
                    Position(1, 2),
                    Position(1, 3),
                    Position(1, 4),
                    Position(1, 5),
                    Position(2, 1),
                    Position(2, 5),
                    Position(3, 1),
                    Position(3, 5),
                    Position(4, 1),
                    Position(4, 5),
                    Position(5, 1),
                    Position(5, 2),
                    Position(5, 3),
                    Position(5, 4),
                    Position(5, 5)
                    );
            final Iterable<IPosition> neighboursPositions = Positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }

        {
            final int radius = 3;
            final Iterable<IPosition> expected = Lists.newArrayList(
                    Position(0, 0),
                    Position(0, 1),
                    Position(0, 2),
                    Position(0, 3),
                    Position(0, 4),
                    Position(0, 5),
                    Position(0, 6),
                    Position(1, 0),
                    Position(1, 6),
                    Position(2, 0),
                    Position(2, 6),
                    Position(3, 0),
                    Position(3, 6),
                    Position(4, 0),
                    Position(4, 6),
                    Position(5, 0),
                    Position(5, 6),
                    Position(6, 0),
                    Position(6, 1),
                    Position(6, 2),
                    Position(6, 3),
                    Position(6, 4),
                    Position(6, 5),
                    Position(6, 6)
                    );
            final Iterable<IPosition> neighboursPositions = Positions.neighbours(this.referential, radius);
            assertEquals(expected, neighboursPositions);
        }
    }
}