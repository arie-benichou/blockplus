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

package blockplus.board;

import static blockplus.board.Layer.State.Karuna;
import static blockplus.board.Layer.State.Metta;
import static blockplus.board.Layer.State.Mudita;
import static blockplus.board.Layer.State.Nirvana;
import static blockplus.board.Layer.State.Upekkha;
import static blockplus.piece.PieceType.PIECE1;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import blockplus.board.Layer.State;
import blockplus.piece.PieceInterface;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.cells.CellsInterface;
import components.position.PositionInterface;

// FIXME add tests for toString, hashCode, equals
public class LayerTest {

    private final static Layer LAYER = new Layer(6, 4);

    @Test
    public void testRows() {
        final int expected = 6;
        final int actual = LAYER.rows();
        assertEquals(expected, actual);
    }

    @Test
    public void testColumns() {
        final int expected = 4;
        final int actual = LAYER.columns();
        assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        final CellsInterface<State> expected = components.cells.immutable.Cells.from(LAYER.rows(), LAYER.columns(), Nirvana, Mudita);
        final CellsInterface<State> actual = LAYER.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMutable() {
        assertFalse(LAYER.isMutable(Position(-1, 0)));
        assertFalse(LAYER.isMutable(Position(0, -1)));
        assertFalse(LAYER.isMutable(Position(LAYER.rows(), 0)));
        assertFalse(LAYER.isMutable(Position(0, LAYER.columns())));
        for (int row = 0; row < LAYER.rows(); ++row)
            for (int column = 0; column < LAYER.columns(); ++column)
                assertTrue(LAYER.isMutable(Position(row, column)));
    }

    @Test
    public void testApplyPositionInterfaceState() {
        {
            final Layer newBoardLayer = LAYER.apply(Position(0, 0), Upekkha);
            final State expected = Upekkha;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final Layer newBoardLayer = LAYER.apply(Position(0, 0), Mudita);
            final State expected = Mudita;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final Layer newBoardLayer = LAYER.apply(Position(0, 0), Karuna);
            final State expected = Karuna;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final Layer newBoardLayer = LAYER.apply(Position(0, 0), Metta);
            final State expected = Metta;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final Layer newBoardLayer = LAYER.apply(Position(0, 0), Nirvana);
            final State expected = Nirvana;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsLegal() {
        assertFalse(LAYER.isLegal(Sets.newHashSet(Position(-1, 0))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(Position(0, -1))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(Position(LAYER.rows(), 0))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(Position(0, LAYER.columns()))));
        for (int row = 0; row < LAYER.rows(); ++row)
            for (int column = 0; column < LAYER.columns(); ++column)
                assertFalse(LAYER.isLegal(Sets.newHashSet(Position(row, column))));
        final Layer newBoardLayer = LAYER.apply(Position(0, 0), Metta);
        assertTrue(newBoardLayer.isLegal(Sets.newHashSet(Position(0, 0))));
    }

    @Test
    public void testApplyMapOfPositionInterfaceState() {
        {
            final HashMap<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), Upekkha);
            mutations.put(Position(0, 1), Karuna);
            mutations.put(Position(1, 0), Karuna);
            mutations.put(Position(1, 1), Metta);
            final Layer newBoardLayer = LAYER.apply(mutations);
            {
                final State expected = Upekkha;
                final State actual = newBoardLayer.get().get(Position(0, 0));
                assertEquals(expected, actual);
            }
            {
                final State expected = Karuna;
                final State actual = newBoardLayer.get().get(Position(0, 1));
                assertEquals(expected, actual);
            }
            {
                final State expected = Karuna;
                final State actual = newBoardLayer.get().get(Position(1, 0));
                assertEquals(expected, actual);
            }
            {
                final State expected = Metta;
                final State actual = newBoardLayer.get().get(Position(1, 1));
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testGetSelves() {
        {
            final Map<PositionInterface, State> actual = LAYER.getSelves();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            assertEquals(expected, actual);
        }
        {
            // TODO à revoir
            final PieceInterface piece = PIECE1.iterator().next().translateTo(Position(1, 1));
            final Map<PositionInterface, State> mutation = new LayerMutationBuilder()
                    .setSelfPositions(piece.getSelfPositions())
                    .setShadowPositions(piece.getShadowPositions())
                    .build();

            final Layer newBoardLayer = LAYER.apply(mutation);
            final Map<PositionInterface, State> actual = newBoardLayer.getSelves();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            expected.put(Position(1, 1), Upekkha);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetLights() {
        {
            final Map<PositionInterface, State> actual = LAYER.getLights();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            assertEquals(expected, actual);
        }
        {
            // TODO à revoir
            final PieceInterface piece = PIECE1.iterator().next().translateTo(Position(1, 1));
            final Map<PositionInterface, State> mutation = new LayerMutationBuilder()
                    .setSelfPositions(piece.getSelfPositions())
                    .setShadowPositions(piece.getShadowPositions())
                    .setLightPositions(piece.getLightPositions())
                    .build();

            final Layer newBoardLayer = LAYER.apply(mutation);
            final Map<PositionInterface, State> actual = newBoardLayer.getLights();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            expected.put(Position(0, 0), Metta);
            expected.put(Position(0, 2), Metta);
            expected.put(Position(2, 0), Metta);
            expected.put(Position(2, 2), Metta);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsLight() {
        for (int row = 0; row < LAYER.rows(); ++row)
            for (int column = 0; column < LAYER.columns(); ++column)
                assertFalse(LAYER.isLight(Position(row, column)));
        final PositionInterface position = Position(0, 0);
        final Layer newBoardLayer = LAYER.apply(position, Metta);
        assertTrue(newBoardLayer.isLight(position));
    }

    @Test
    public void testToString() {
        final String expected = "Layer{rows=6, columns=4, initial=Nirvana, undefined=Mudita, mutations={}}";
        final String actual = LAYER.toString();
        assertEquals(expected, actual);
    }

    @Ignore
    public void testHashCode() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

}