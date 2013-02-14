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

package components.cells.immutable;

import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import components.cells.CellsInterface;
import components.position.PositionInterface;

public class CellsTest {

    private enum State {
        Initial, Undefined, Other;
    }

    private CellsInterface<State> cells;

    @Before
    public void setUp() throws Exception {
        this.cells = Cells.from(6, 4, State.Initial, State.Undefined);
    }

    @After
    public void tearDown() throws Exception {
        this.cells = null;
    }

    @Test
    public void testRows() {
        final int expected = 6;
        final int actual = this.cells.rows();
        assertEquals(expected, actual);
    }

    @Test
    public void testColumns() {
        final int expected = 4;
        final int actual = this.cells.columns();
        assertEquals(expected, actual);
    }

    @Test
    public void testInitialSymbol() {
        final State expected = State.Initial;
        final State actual = this.cells.initialSymbol();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUndefinedSymbol() {
        final State expected = State.Undefined;
        final State actual = this.cells.undefinedSymbol();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRowColumn() {
        {
            final State expected = this.cells.undefinedSymbol();
            final State actual = this.cells.get(-1, 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = this.cells.undefinedSymbol();
            final State actual = this.cells.get(0, -1);
            assertEquals(expected, actual);
        }
        {
            final State expected = this.cells.initialSymbol();
            final State actual = this.cells.get(0, 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = this.cells.undefinedSymbol();
            final State actual = this.cells.get(this.cells.rows(), 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = this.cells.undefinedSymbol();
            final State actual = this.cells.get(0, this.cells.columns());
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetPosition() {
        {
            final State expected = this.cells.undefinedSymbol();
            final State actual = this.cells.get(Position(-1, 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = this.cells.undefinedSymbol();
            final State actual = this.cells.get(Position(0, -1));
            assertEquals(expected, actual);
        }
        {
            final State expected = this.cells.initialSymbol();
            final State actual = this.cells.get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = this.cells.undefinedSymbol();
            final State actual = this.cells.get(Position(this.cells.rows(), 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = this.cells.undefinedSymbol();
            final State actual = this.cells.get(Position(0, this.cells.columns()));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testApply() {
        {
            final PositionInterface position = Position(0, 0);
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(position, State.Other);
            CellsInterface<State> newCells;
            newCells = this.cells.apply(mutations);
            final State expected = State.Other;
            final State actual = newCells.get(position);
            assertSame(expected, actual);
            assertFalse(expected.equals(this.cells.initialSymbol()));
            assertEquals(this.cells.initialSymbol(), this.cells.get(position));
        }
    }

    @Test
    public void testCopy() {
        {
            final CellsInterface<State> copy = this.cells.copy();
            final PositionInterface position = Position(0, 0);
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(position, State.Other);
            CellsInterface<State> newCells;
            newCells = copy.apply(mutations);
            final State expected = State.Other;
            final State actual = newCells.get(position);
            assertSame(expected, actual);
            assertFalse(expected.equals(this.cells.initialSymbol()));
            assertEquals(this.cells.initialSymbol(), this.cells.get(position));
        }
    }

    @Test
    public void testToString() {
        final PositionInterface position = Position(0, 0);
        final Map<PositionInterface, State> mutations = Maps.newHashMap();
        mutations.put(position, State.Other);
        final CellsInterface<State> newCells = this.cells.apply(mutations);
        final StringBuilder builder = new StringBuilder();
        builder.append("Cells{");
        builder.append("rows=6, ");
        builder.append("columns=4, ");
        builder.append("initial=Initial, ");
        builder.append("undefined=Undefined, ");
        builder.append("mutation={Position(0, 0)=Other}");
        builder.append('}');
        final String expected = builder.toString();
        final String actual = newCells.toString();
        assertEquals(expected, actual);
    }

}