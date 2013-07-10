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

package blockplus.model.board;

import static blockplus.model.board.Layer.State.Karuna;
import static blockplus.model.board.Layer.State.Metta;
import static blockplus.model.board.Layer.State.Mudita;
import static blockplus.model.board.Layer.State.Nirvana;
import static blockplus.model.board.Layer.State.Upekkha;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import blockplus.model.board.Layer.State;
import blockplus.model.entity.Polyomino;
import blockplus.model.entity.entity.Entity;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.cells.Cells;
import components.cells.ICells;
import components.cells.Positions;
import components.cells.Positions.Position;

public class LayerTest {

    private final static Layer LAYER = new Layer(new Positions(6, 4));

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
        final ICells<State> expected = Cells.from(new Positions(LAYER.rows(), LAYER.columns()), Nirvana, Mudita);
        final ICells<State> actual = LAYER.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMutable() {
        assertFalse(LAYER.isMutable(-1, 0));
        assertFalse(LAYER.isMutable(0, -1));
        assertFalse(LAYER.isMutable(LAYER.rows(), 0));
        assertFalse(LAYER.isMutable(0, LAYER.columns()));
        for (int row = 0; row < LAYER.rows(); ++row)
            for (int column = 0; column < LAYER.columns(); ++column)
                assertTrue(LAYER.isMutable(row, column));
    }

    @Test
    public void testApplyCellPositionState() {
        {
            final Layer newBoardLayer = LAYER.apply(LAYER.position(0, 0), Upekkha);
            final State expected = Upekkha;
            final State actual = newBoardLayer.get().get(LAYER.position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final Layer newBoardLayer = LAYER.apply(LAYER.position(0, 0), Mudita);
            final State expected = Mudita;
            final State actual = newBoardLayer.get().get(LAYER.position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final Layer newBoardLayer = LAYER.apply(LAYER.position(0, 0), Karuna);
            final State expected = Karuna;
            final State actual = newBoardLayer.get().get(LAYER.position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final Layer newBoardLayer = LAYER.apply(LAYER.position(0, 0), Metta);
            final State expected = Metta;
            final State actual = newBoardLayer.get().get(LAYER.position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final Layer newBoardLayer = LAYER.apply(LAYER.position(0, 0), Nirvana);
            final State expected = Nirvana;
            final State actual = newBoardLayer.get().get(LAYER.position(0, 0));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsLegal() {
        assertFalse(LAYER.isLegal(Sets.newHashSet(LAYER.position(-1, 0))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(LAYER.position(0, -1))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(LAYER.position(LAYER.rows(), 0))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(LAYER.position(0, LAYER.columns()))));
        for (int row = 0; row < LAYER.rows(); ++row)
            for (int column = 0; column < LAYER.columns(); ++column)
                assertFalse(LAYER.isLegal(Sets.newHashSet(LAYER.position(row, column))));
        final Layer newBoardLayer = LAYER.apply(LAYER.position(0, 0), Metta);
        assertTrue(newBoardLayer.isLegal(Sets.newHashSet(LAYER.position(0, 0))));
    }

    public void testApplyMapOfCellPositionState() {
        {
            final HashMap<Position, State> mutations = Maps.newHashMap();
            mutations.put(LAYER.position(0, 0), Upekkha);
            mutations.put(LAYER.position(0, 1), Karuna);
            mutations.put(LAYER.position(1, 0), Karuna);
            mutations.put(LAYER.position(1, 1), Metta);
            final Layer newBoardLayer = LAYER.apply(mutations);
            {
                final State expected = Upekkha;
                final State actual = newBoardLayer.get().get(LAYER.position(0, 0));
                assertEquals(expected, actual);
            }
            {
                final State expected = Karuna;
                final State actual = newBoardLayer.get().get(LAYER.position(0, 1));
                assertEquals(expected, actual);
            }
            {
                final State expected = Karuna;
                final State actual = newBoardLayer.get().get(LAYER.position(1, 0));
                assertEquals(expected, actual);
            }
            {
                final State expected = Metta;
                final State actual = newBoardLayer.get().get(LAYER.position(1, 1));
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testGetSelves() {
        {
            final Map<Position, State> expected = Maps.newHashMap();
            final Map<Position, State> actual = LAYER.getSelves();
            assertEquals(expected, actual);
        }
        {
            final Map<Position, State> expected = Maps.newHashMap();
            expected.put(LAYER.position(0, 0), Upekkha);
            final Entity entity = Polyomino._1.get();
            final Map<Position, State> mutation = new LayerMutationBuilder()
                    .setSelfPositions(entity.positions())
                    .setShadowPositions(entity.shadows())
                    .build();
            final Layer newBoardLayer = LAYER.apply(mutation);
            final Map<Position, State> actual = newBoardLayer.getSelves();
            assertEquals(expected, actual);
        }
    }

    //FIXME TODO !!!!
    public void testGetLights() {
        {
            final Map<Position, State> expected = Maps.newHashMap();
            final Map<Position, State> actual = LAYER.getLights();
            assertEquals(expected, actual);
        }
        {
            final Map<Position, State> expected = Maps.newHashMap();
            expected.put(LAYER.position(0, 0), Metta);
            expected.put(LAYER.position(0, 2), Metta);
            expected.put(LAYER.position(2, 0), Metta);
            expected.put(LAYER.position(2, 2), Metta);
            final Entity entity = Polyomino._1.get();
            final Map<Position, State> mutation = new LayerMutationBuilder()
                    .setSelfPositions(entity.positions())
                    .setShadowPositions(entity.shadows())
                    .setLightPositions(entity.lights())
                    .build();
            final Layer newBoardLayer = LAYER.apply(mutation);
            final Map<Position, State> actual = newBoardLayer.getLights();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsLight() {
        for (int row = 0; row < LAYER.rows(); ++row)
            for (int column = 0; column < LAYER.columns(); ++column)
                assertFalse(LAYER.isLight(LAYER.position(row, column)));
        final Position position = LAYER.position(0, 0);
        final Layer newBoardLayer = LAYER.apply(position, Metta);
        assertTrue(newBoardLayer.isLight(position));
    }

    @Test
    public void testToString() {
        final String expected = "Layer{rows=6, columns=4, initial=Nirvana, undefined=Mudita, mutations={}}";
        final String actual = LAYER.toString();
        assertEquals(expected, actual);
    }

}