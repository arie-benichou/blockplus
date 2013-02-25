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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.base.Equivalences;
import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import components.cells.CellsInterface;
import components.position.PositionInterface;

public class CellsTest {

    private enum State {
        Initial, Undefined, Other;
    }

    private final static CellsInterface<State> CELLS = Cells.from(6, 4, State.Initial, State.Undefined);

    private final static Predicate<Entry<PositionInterface, State>> UNDEFINED_PREDICATE = new Predicate<Map.Entry<PositionInterface, State>>() {

        @Override
        public boolean apply(final Entry<PositionInterface, State> entry) {
            return entry.getValue().equals(State.Undefined);
        }

    };

    private final static Predicate<Entry<PositionInterface, State>> OTHER_PREDICATE = new Predicate<Map.Entry<PositionInterface, State>>() {

        @Override
        public boolean apply(final Entry<PositionInterface, State> entry) {
            return entry.getValue().equals(State.Other);
        }

    };

    @Ignore
    public void testFromIntIntTTMapOfPositionInterfaceTMapOfPositionInterfaceT() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testFromIntIntTTMapOfPositionInterfaceT() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testFromIntIntTT() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testFromCellsOfT() {
        fail("Not yet implemented");
    }

    @Test
    public void testRows() {
        final int expected = 6;
        final int actual = CELLS.rows();
        assertEquals(expected, actual);
    }

    @Test
    public void testColumns() {
        final int expected = 4;
        final int actual = CELLS.columns();
        assertEquals(expected, actual);
    }

    @Test
    public void testInitialSymbol() {
        final State expected = State.Initial;
        final State actual = CELLS.initialSymbol();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUndefinedSymbol() {
        final State expected = State.Undefined;
        final State actual = CELLS.undefinedSymbol();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRowColumn() {
        {
            final State expected = CELLS.undefinedSymbol();
            final State actual = CELLS.get(-1, 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = CELLS.undefinedSymbol();
            final State actual = CELLS.get(0, -1);
            assertEquals(expected, actual);
        }
        {
            final State expected = CELLS.initialSymbol();
            final State actual = CELLS.get(0, 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = CELLS.undefinedSymbol();
            final State actual = CELLS.get(CELLS.rows(), 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = CELLS.undefinedSymbol();
            final State actual = CELLS.get(0, CELLS.columns());
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetPosition() {
        {
            final State expected = CELLS.undefinedSymbol();
            final State actual = CELLS.get(Position(-1, 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = CELLS.undefinedSymbol();
            final State actual = CELLS.get(Position(0, -1));
            assertEquals(expected, actual);
        }
        {
            final State expected = CELLS.initialSymbol();
            final State actual = CELLS.get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = CELLS.undefinedSymbol();
            final State actual = CELLS.get(Position(CELLS.rows(), 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = CELLS.undefinedSymbol();
            final State actual = CELLS.get(Position(0, CELLS.columns()));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testApply() {
        final PositionInterface position = Position(0, 0);
        final Map<PositionInterface, State> mutations = Maps.newHashMap();
        mutations.put(position, State.Other);
        CellsInterface<State> newCells;
        newCells = CELLS.apply(mutations);
        final State expected = State.Other;
        final State actual = newCells.get(position);
        assertSame(expected, actual);
        assertFalse(expected.equals(CELLS.initialSymbol()));
        assertEquals(CELLS.initialSymbol(), CELLS.get(position));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testExposedMutationsImmutability() {
        CELLS.mutations().put(Position(0, 0), State.Undefined);
    }

    @Test
    public void testMutations() {
        final Map<PositionInterface, State> mutations = Maps.newHashMap();
        mutations.put(Position(0, 0), State.Initial);
        mutations.put(Position(0, 1), State.Initial);
        mutations.put(Position(1, 0), State.Initial);
        mutations.put(Position(1, 1), State.Undefined);
        final Map<PositionInterface, State> actual = CELLS.apply(mutations).mutations();
        final Map<PositionInterface, State> expected = Maps.newHashMap();
        expected.put(Position(1, 1), State.Undefined);
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterNull() {
        CELLS.filter(null);
    }

    @Test
    public void testFilter() {
        {
            final Map<PositionInterface, State> cells = CELLS.filter(UNDEFINED_PREDICATE);
            assertTrue(cells.isEmpty());
        }
        {
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), State.Undefined);
            final Map<PositionInterface, State> expected = mutations;
            final Map<PositionInterface, State> actual = CELLS.apply(mutations).filter(UNDEFINED_PREDICATE);
            assertEquals(expected, actual);
        }
        {
            final Map<PositionInterface, State> cells = CELLS.filter(OTHER_PREDICATE);
            assertTrue(cells.isEmpty());
        }
        {
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), State.Other);
            final Map<PositionInterface, State> actual = CELLS.apply(mutations).filter(OTHER_PREDICATE);
            final Map<PositionInterface, State> expected = mutations;
            assertTrue(Equivalences.equals().equivalent(actual, expected));
        }
        {
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), State.Undefined);
            mutations.put(Position(1, 0), State.Other);
            mutations.put(Position(0, 1), State.Undefined);
            mutations.put(Position(1, 1), State.Other);
            {
                final Map<PositionInterface, State> actual = CELLS.apply(mutations).filter(UNDEFINED_PREDICATE);
                final Map<PositionInterface, State> expected = Maps.newHashMap();
                expected.put(Position(0, 0), State.Undefined);
                expected.put(Position(0, 1), State.Undefined);
                assertEquals(expected, actual);
            }
            {
                final Map<PositionInterface, State> actual = CELLS.apply(mutations).filter(OTHER_PREDICATE);
                final Map<PositionInterface, State> expected = Maps.newHashMap();
                expected.put(Position(1, 0), State.Other);
                expected.put(Position(1, 1), State.Other);
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testCopy() {
        {
            final CellsInterface<State> copy = CELLS.copy();
            final PositionInterface position = Position(0, 0);
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(position, State.Other);
            CellsInterface<State> newCells;
            newCells = copy.apply(mutations);
            final State expected = State.Other;
            final State actual = newCells.get(position);
            assertSame(expected, actual);
            assertFalse(expected.equals(CELLS.initialSymbol()));
            assertEquals(CELLS.initialSymbol(), CELLS.get(position));
        }
    }

    @Test
    public void testToString() {
        final PositionInterface position = Position(0, 0);
        final Map<PositionInterface, State> mutations = Maps.newHashMap();
        mutations.put(position, State.Other);
        final CellsInterface<State> newCells = CELLS.apply(mutations);
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

    @Test
    public void testHashCode() {
        assertEquals(CELLS.toString().hashCode(), CELLS.hashCode());
    }

    public void testEqualsWithNull() {
        assertFalse(CELLS.equals(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEqualsUnexpectedType() {
        CELLS.equals(this);
    }

    @Test
    public void testEqualsObject() {
        assertTrue(CELLS.equals(CELLS));
        assertTrue(CELLS.equals(Cells.from(6, 4, State.Initial, State.Undefined)));
        assertTrue(Cells.from(6, 4, State.Initial, State.Undefined).equals(CELLS));
        assertFalse(Cells.from(0, 4, State.Initial, State.Undefined).equals(CELLS));
        assertFalse(Cells.from(6, 0, State.Initial, State.Undefined).equals(CELLS));
        assertFalse(Cells.from(6, 4, State.Undefined, State.Undefined).equals(CELLS));
        assertFalse(Cells.from(6, 4, State.Initial, State.Initial).equals(CELLS));
        assertFalse(Cells.from(4, 6, State.Initial, State.Undefined).equals(CELLS));
        assertFalse(Cells.from(6, 4, State.Undefined, State.Initial).equals(CELLS));
        {
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), State.Initial);
            assertTrue(CELLS.apply(mutations).equals(CELLS));
        }
        {
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), State.Undefined);
            assertFalse(CELLS.apply(mutations).equals(CELLS));
        }
        {
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), State.Other);
            assertFalse(CELLS.apply(mutations).equals(CELLS));
        }
    }
}