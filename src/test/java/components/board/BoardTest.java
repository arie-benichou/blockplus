
package components.board;

import static components.board.BoardTest.State.*;
import static components.position.Position.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import components.position.PositionInterface;

public class BoardTest {

    enum State implements Symbol {
        Initial, Undefined, Other;
    }

    private BoardInterface<State> board;

    @Before
    public void setUp() throws Exception {
        this.board = Board.from(6, 4, Initial, Undefined);
    }

    @After
    public void tearDown() throws Exception {
        this.board = null;
    }

    @Test
    public void testRows() {
        final int expected = 6;
        final int actual = this.board.rows();
        assertEquals(expected, actual);
    }

    @Test
    public void testColumns() {
        final int expected = 4;
        final int actual = this.board.columns();
        assertEquals(expected, actual);
    }

    @Test
    public void testInitialSymbol() {
        final State expected = Initial;
        final State actual = this.board.initialSymbol();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUndefinedSymbol() {
        final State expected = Undefined;
        final State actual = this.board.undefinedSymbol();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetRowColumn() {
        {
            final State expected = this.board.undefinedSymbol();
            final State actual = this.board.get(-1, 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = this.board.undefinedSymbol();
            final State actual = this.board.get(0, -1);
            assertEquals(expected, actual);
        }
        {
            final State expected = this.board.initialSymbol();
            final State actual = this.board.get(0, 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = this.board.undefinedSymbol();
            final State actual = this.board.get(this.board.rows(), 0);
            assertEquals(expected, actual);
        }
        {
            final State expected = this.board.undefinedSymbol();
            final State actual = this.board.get(0, this.board.columns());
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testGetPosition() {
        {
            final State expected = this.board.undefinedSymbol();
            final State actual = this.board.get(Position(-1, 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = this.board.undefinedSymbol();
            final State actual = this.board.get(Position(0, -1));
            assertEquals(expected, actual);
        }
        {
            final State expected = this.board.initialSymbol();
            final State actual = this.board.get(Position(0, 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = this.board.undefinedSymbol();
            final State actual = this.board.get(Position(this.board.rows(), 0));
            assertEquals(expected, actual);
        }
        {
            final State expected = this.board.undefinedSymbol();
            final State actual = this.board.get(Position(0, this.board.columns()));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testApply() {
        {
            final PositionInterface position = Position(0, 0);
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(position, Other);
            BoardInterface<State> newBoard;
            newBoard = this.board.apply(mutations);
            final State expected = Other;
            final State actual = newBoard.get(position);
            assertSame(expected, actual);
            assertFalse(expected.equals(this.board.initialSymbol()));
            assertEquals(this.board.initialSymbol(), this.board.get(position));
        }
    }

    @Test
    public void testCopy() {
        {
            final BoardInterface<State> copy = this.board.copy();
            final PositionInterface position = Position(0, 0);
            final Map<PositionInterface, State> mutations = Maps.newHashMap();
            mutations.put(position, Other);
            BoardInterface<State> newBoard;
            newBoard = copy.apply(mutations);
            final State expected = Other;
            final State actual = newBoard.get(position);
            assertSame(expected, actual);
            assertFalse(expected.equals(this.board.initialSymbol()));
            assertEquals(this.board.initialSymbol(), this.board.get(position));
        }
    }

    @Test
    public void testToString() {
        final PositionInterface position = Position(0, 0);
        final Map<PositionInterface, State> mutations = Maps.newHashMap();
        mutations.put(position, Other);
        final BoardInterface<State> newBoard = this.board.apply(mutations);
        final StringBuilder builder = new StringBuilder();
        builder.append("Board{");
        builder.append("\n");
        builder.append("  rows=6, ");
        builder.append("\n");
        builder.append("  columns=4, ");
        builder.append("\n");
        builder.append("  initial=Initial, ");
        builder.append("\n");
        builder.append("  undefined=Undefined, ");
        builder.append("\n");
        builder.append("  mutation={Position(0, 0)=Other}");
        builder.append("\n");
        builder.append("}");
        final String expected = builder.toString();
        final String actual = newBoard.toString();
        assertEquals(expected, actual);
    }

}