
package blockplus.board;

import static blockplus.model.board.State.*;
import static components.position.Position.*;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;
import blockplus.model.piece.Pieces;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.board.BoardInterface;
import components.position.PositionInterface;

public class BoardLayerTest {

    private BoardLayer boardLayer;

    @Before
    public void setUp() throws Exception {
        this.boardLayer = new BoardLayer(6, 4);
    }

    @After
    public void tearDown() throws Exception {
        this.boardLayer = null;
    }

    @Test
    public void testRows() {
        final int expected = 6;
        final int actual = this.boardLayer.rows();
        assertEquals(expected, actual);
    }

    @Test
    public void testColumns() {
        final int expected = 4;
        final int actual = this.boardLayer.columns();
        assertEquals(expected, actual);
    }

    @Test
    public void testGet() {
        final BoardInterface<State> expected = components.board.Board.from(this.boardLayer.rows(), this.boardLayer.columns(), None, Other);
        final BoardInterface<State> actual = this.boardLayer.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsMutable() {
        assertFalse(this.boardLayer.isMutable(Position(-1, 0)));
        assertFalse(this.boardLayer.isMutable(Position(0, -1)));
        assertFalse(this.boardLayer.isMutable(Position(this.boardLayer.rows(), 0)));
        assertFalse(this.boardLayer.isMutable(Position(0, this.boardLayer.columns())));
        for (int row = 0; row < this.boardLayer.rows(); ++row)
            for (int column = 0; column < this.boardLayer.columns(); ++column)
                assertTrue(this.boardLayer.isMutable(Position(row, column)));
    }

    @Test
    public void testApplyPositionInterfaceState() {
        {
            final BoardLayer newBoardLayer = this.boardLayer.apply(Position(0, 0), Self);
            final State expected = Self;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final BoardLayer newBoardLayer = this.boardLayer.apply(Position(0, 0), Other);
            final State expected = Other;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final BoardLayer newBoardLayer = this.boardLayer.apply(Position(0, 0), Shadow);
            final State expected = Shadow;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final BoardLayer newBoardLayer = this.boardLayer.apply(Position(0, 0), Light);
            final State expected = Light;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final BoardLayer newBoardLayer = this.boardLayer.apply(Position(0, 0), None);
            final State expected = None;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsLegal() {
        assertFalse(this.boardLayer.isLegal(Sets.newHashSet(Position(-1, 0))));
        assertFalse(this.boardLayer.isLegal(Sets.newHashSet(Position(0, -1))));
        assertFalse(this.boardLayer.isLegal(Sets.newHashSet(Position(this.boardLayer.rows(), 0))));
        assertFalse(this.boardLayer.isLegal(Sets.newHashSet(Position(0, this.boardLayer.columns()))));
        for (int row = 0; row < this.boardLayer.rows(); ++row)
            for (int column = 0; column < this.boardLayer.columns(); ++column)
                assertFalse(this.boardLayer.isLegal(Sets.newHashSet(Position(row, column))));
        final BoardLayer newBoardLayer = this.boardLayer.apply(Position(0, 0), Light);
        assertTrue(newBoardLayer.isLegal(Sets.newHashSet(Position(0, 0))));
    }

    @Test
    public void testApplyMapOfPositionInterfaceState() {
        {
            final HashMap<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(Position(0, 0), Self);
            mutations.put(Position(0, 1), Shadow);
            mutations.put(Position(1, 0), Shadow);
            mutations.put(Position(1, 1), Light);
            final BoardLayer newBoardLayer = this.boardLayer.apply(mutations);
            {
                final State expected = Self;
                final State actual = newBoardLayer.get().get(Position(0, 0));
                assertEquals(expected, actual);
            }
            {
                final State expected = Shadow;
                final State actual = newBoardLayer.get().get(Position(0, 1));
                assertEquals(expected, actual);
            }
            {
                final State expected = Shadow;
                final State actual = newBoardLayer.get().get(Position(1, 0));
                assertEquals(expected, actual);
            }
            {
                final State expected = Light;
                final State actual = newBoardLayer.get().get(Position(1, 1));
                assertEquals(expected, actual);
            }
        }
    }

    @Test
    public void testApplyPieceInterface() {
        final BoardLayer newBoardLayer = this.boardLayer.apply(Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(1, 1)));
        {
            final State expected = Shadow;
            final State actual = newBoardLayer.get().get(Position(0, 1));
            assertEquals(expected, actual);
        }
        {
            final State expected = Shadow;
            final State actual = newBoardLayer.get().get(Position(1, 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = Shadow;
            final State actual = newBoardLayer.get().get(Position(1, 2));
            assertEquals(expected, actual);
        }
        {
            final State expected = Shadow;
            final State actual = newBoardLayer.get().get(Position(2, 1));
            assertEquals(expected, actual);
        }
        {
            final State expected = Self;
            final State actual = newBoardLayer.get().get(Position(1, 1));
            assertEquals(expected, actual);
        }
        {
            final State expected = Light;
            final State actual = newBoardLayer.get().get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = Light;
            final State actual = newBoardLayer.get().get(Position(0, 2));
            assertEquals(expected, actual);
        }
        {
            final State expected = Light;
            final State actual = newBoardLayer.get().get(Position(2, 0));
            assertEquals(expected, actual);
        }

        {
            final State expected = Light;
            final State actual = newBoardLayer.get().get(Position(2, 2));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetSelves() {
        {
            final Map<PositionInterface, State> actual = this.boardLayer.getSelves();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            assertEquals(expected, actual);
        }
        {
            // TODO ! à revoir
            final BoardLayer newBoardLayer = this.boardLayer.apply(Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(1, 1)));
            final Map<PositionInterface, State> actual = newBoardLayer.getSelves();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            expected.put(Position(1, 1), Self);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetOthers() {
        {
            final Map<PositionInterface, State> actual = this.boardLayer.getOthers();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            assertEquals(expected, actual);
        }
        {
            final BoardLayer newBoardLayer = this.boardLayer.apply(Position(0, 0), Other);
            final Map<PositionInterface, State> actual = newBoardLayer.getOthers();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            expected.put(Position(0, 0), Other);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetShadows() {
        {
            final Map<PositionInterface, State> actual = this.boardLayer.getShadows();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            assertEquals(expected, actual);
        }
        {
            final BoardLayer newBoardLayer = this.boardLayer.apply(Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(1, 1)));
            final Map<PositionInterface, State> actual = newBoardLayer.getShadows();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            expected.put(Position(0, 1), Shadow);
            expected.put(Position(1, 0), Shadow);
            expected.put(Position(1, 2), Shadow);
            expected.put(Position(2, 1), Shadow);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetLights() {
        {
            final Map<PositionInterface, State> actual = this.boardLayer.getLights();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            assertEquals(expected, actual);
        }
        {
            final BoardLayer newBoardLayer = this.boardLayer.apply(Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(1, 1)));
            final Map<PositionInterface, State> actual = newBoardLayer.getLights();
            final Map<PositionInterface, State> expected = Maps.newHashMap();
            expected.put(Position(0, 0), Light);
            expected.put(Position(0, 2), Light);
            expected.put(Position(2, 0), Light);
            expected.put(Position(2, 2), Light);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testIsNone() {
        assertFalse(this.boardLayer.isNone(Position(-1, 0)));
        assertFalse(this.boardLayer.isNone(Position(0, -1)));
        assertFalse(this.boardLayer.isNone(Position(this.boardLayer.rows(), 0)));
        assertFalse(this.boardLayer.isNone(Position(0, this.boardLayer.columns())));
        for (int row = 0; row < this.boardLayer.rows(); ++row)
            for (int column = 0; column < this.boardLayer.columns(); ++column)
                assertTrue(this.boardLayer.isNone(Position(row, column)));
    }

    @Test
    public void testIsSelf() {
        for (int row = 0; row < this.boardLayer.rows(); ++row)
            for (int column = 0; column < this.boardLayer.columns(); ++column)
                assertFalse(this.boardLayer.isSelf(Position(row, column)));
        final PositionInterface position = Position(0, 0);
        final BoardLayer newBoardLayer = this.boardLayer.apply(position, Self);
        assertTrue(newBoardLayer.isSelf(position));
    }

    @Test
    public void testIsOther() {
        for (int row = 0; row < this.boardLayer.rows(); ++row)
            for (int column = 0; column < this.boardLayer.columns(); ++column)
                assertFalse(this.boardLayer.isOther(Position(row, column)));
        final PositionInterface position = Position(0, 0);
        final BoardLayer newBoardLayer = this.boardLayer.apply(position, Other);
        assertTrue(newBoardLayer.isOther(position));
    }

    @Test
    public void testIsShadow() {
        for (int row = 0; row < this.boardLayer.rows(); ++row)
            for (int column = 0; column < this.boardLayer.columns(); ++column)
                assertFalse(this.boardLayer.isShadow(Position(row, column)));
        final PositionInterface position = Position(0, 0);
        final BoardLayer newBoardLayer = this.boardLayer.apply(position, Shadow);
        assertTrue(newBoardLayer.isShadow(position));
    }

    @Test
    public void testIsLight() {
        for (int row = 0; row < this.boardLayer.rows(); ++row)
            for (int column = 0; column < this.boardLayer.columns(); ++column)
                assertFalse(this.boardLayer.isLight(Position(row, column)));
        final PositionInterface position = Position(0, 0);
        final BoardLayer newBoardLayer = this.boardLayer.apply(position, Light);
        assertTrue(newBoardLayer.isLight(position));
    }

    @Test
    public void testIsSelfOrOther() {
        for (int row = 0; row < this.boardLayer.rows(); ++row)
            for (int column = 0; column < this.boardLayer.columns(); ++column)
                assertFalse(this.boardLayer.isSelfOrOther(Position(row, column)));
        final PositionInterface position1 = Position(0, 0);
        final PositionInterface position2 = Position(0, 1);
        BoardLayer newBoardLayer = this.boardLayer.apply(position1, Self);
        newBoardLayer = newBoardLayer.apply(position2, Self);
        assertTrue(newBoardLayer.isSelfOrOther(position1));
        assertTrue(newBoardLayer.isSelfOrOther(position2));
    }

}