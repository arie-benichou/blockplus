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

package blockplus.model;

import static blockplus.model.Board.Layer.State.Karuna;
import static blockplus.model.Board.Layer.State.Metta;
import static blockplus.model.Board.Layer.State.Upekkha;
import static components.cells.Positions.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import blockplus.model.Board.Layer;
import blockplus.model.Board.Layer.State;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.cells.IPosition;

public class BoardLayerTest {

    private static final int COLUMNS = 4;
    private static final int ROWS = 6;
    private final static Layer LAYER = new Layer(ROWS, COLUMNS);

    @Test
    public void testIsMutable() {
        assertFalse(LAYER.isMutable(Position(-1, 0)));
        assertFalse(LAYER.isMutable(Position(0, -1)));
        assertFalse(LAYER.isMutable(Position(ROWS, 0)));
        assertFalse(LAYER.isMutable(Position(0, COLUMNS)));
        for (int row = 0; row < ROWS; ++row)
            for (int column = 0; column < COLUMNS; ++column)
                assertTrue(LAYER.isMutable(Position(row, column)));
    }

    @Test
    public void testIsLegal() {
        assertFalse(LAYER.isLegal(Sets.newHashSet(Position(-1, 0))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(Position(0, -1))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(Position(ROWS, 0))));
        assertFalse(LAYER.isLegal(Sets.newHashSet(Position(0, COLUMNS))));
        for (int row = 0; row < ROWS; ++row)
            for (int column = 0; column < COLUMNS; ++column)
                assertFalse(LAYER.isLegal(Sets.newHashSet(Position(row, column))));
        final Map<IPosition, State> mutations = Maps.newHashMap();
        mutations.put(Position(0, 0), Metta);
        final Layer newBoardLayer = LAYER.apply(mutations);
        assertTrue(newBoardLayer.isLegal(Sets.newHashSet(Position(0, 0))));
    }

    public void testApplyMapOfCellPositionState() {
        {
            final HashMap<IPosition, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), Upekkha);
            mutations.put(Position(0, 1), Karuna);
            mutations.put(Position(1, 0), Karuna);
            mutations.put(Position(1, 1), Metta);
            final Layer newBoardLayer = LAYER.apply(mutations);
            {
                final State expected = Upekkha;
                final State actual = newBoardLayer.get(Position(0, 0));
                assertEquals(expected, actual);
            }
            {
                final State expected = Karuna;
                final State actual = newBoardLayer.get(Position(0, 1));
                assertEquals(expected, actual);
            }
            {
                final State expected = Karuna;
                final State actual = newBoardLayer.get(Position(1, 0));
                assertEquals(expected, actual);
            }
            {
                final State expected = Metta;
                final State actual = newBoardLayer.get(Position(1, 1));
                assertEquals(expected, actual);
            }
        }
    }

    //    @Test
    //    public void testGetSelves() {
    //        {
    //            final Map<IPosition, State> expected = Maps.newHashMap();
    //            final Map<IPosition, State> actual = LAYER.getSelves();
    //            assertEquals(expected, actual);
    //        }
    //        {
    //            final Map<IPosition, State> expected = Maps.newHashMap();
    //            expected.put(Position(0, 0), Upekkha);
    //            final Polyomino polyomino = Polyomino._1;
    //            final Map<IPosition, State> mutation = new LayerMutationBuilder()
    //                    .setSelves(polyomino.positions())
    //                    .setShadows(polyomino.shadows())
    //                    .build();
    //            final Layer newBoardLayer = LAYER.apply(mutation);
    //            final Map<IPosition, State> actual = newBoardLayer.getSelves();
    //            assertEquals(expected, actual);
    //        }
    //    }
    //
    //    @Test
    //    public void testGetLights() {
    //        {
    //            final Map<IPosition, State> expected = Maps.newHashMap();
    //            final Map<IPosition, State> actual = LAYER.getLights();
    //            assertEquals(expected, actual);
    //        }
    //        {
    //            final Map<IPosition, State> expected = Maps.newHashMap();
    //            expected.put(Position(1, 1), Metta);
    //            final Polyomino polyomino = Polyomino._1;
    //            final Map<IPosition, State> mutation = new LayerMutationBuilder()
    //                    .setSelves(polyomino.positions())
    //                    .setShadows(polyomino.shadows())
    //                    .setLights(polyomino.lights())
    //                    .build();
    //            final Layer newBoardLayer = LAYER.apply(mutation);
    //            final Map<IPosition, State> actual = newBoardLayer.getLights();
    //            assertEquals(expected, actual);
    //        }
    //    }

    @Test
    public void testIsLight() {
        for (int row = 0; row < ROWS; ++row)
            for (int column = 0; column < COLUMNS; ++column)
                assertFalse(LAYER.isLight(Position(row, column)));
        final IPosition position = Position(0, 0);

        final Map<IPosition, State> mutations = Maps.newHashMap();
        mutations.put(position, Metta);
        final Layer newBoardLayer = LAYER.apply(mutations);
        assertTrue(newBoardLayer.isLight(position));
    }

    @Test
    public void testToString() {
        final String expected = "Layer{rows=6, columns=4, initial=Nirvana, undefined=Mudita, mutations={}}";
        final String actual = LAYER.toString();
        assertEquals(expected, actual);
    }

}