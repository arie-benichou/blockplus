
package blockplus.move;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import blockplus.piece.NullPieceComponent;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceType;

// FIXME add tests for hashCode, equals
public class MoveTest {

    private final static PieceInterface NULL_PIECE_INSTANCE = NullPieceComponent.getInstance();

    private final static PieceInterface PIECE_INSTANCE = PieceType.PIECE1.iterator().next();

    private final static Move BLUE_MOVE = Moves.getMove(Blue, PIECE_INSTANCE);
    private final static Move YELLOW_MOVE = Moves.getMove(Yellow, PIECE_INSTANCE);
    private final static Move RED_MOVE = Moves.getMove(Red, PIECE_INSTANCE);
    private final static Move GREEN_MOVE = Moves.getMove(Green, PIECE_INSTANCE);

    private final static Move BLUE_NULL_MOVE = Moves.getNullMove(Blue);
    private final static Move YELLOW_NULL_MOVE = Moves.getNullMove(Yellow);
    private final static Move RED_NULL_MOVE = Moves.getNullMove(Red);
    private final static Move GREEN_NULL_MOVE = Moves.getNullMove(Green);

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMove1() {
        Moves.getNullMove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMove2() {
        Moves.getMove(null, PIECE_INSTANCE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMove3() {
        Moves.getMove(Blue, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalMove4() {
        Moves.getMove(Blue, NULL_PIECE_INSTANCE);
    }

    @Test
    public void testIsNull() {

        assertFalse(BLUE_MOVE.isNull());
        assertTrue(BLUE_NULL_MOVE.isNull());

        assertFalse(YELLOW_MOVE.isNull());
        assertTrue(YELLOW_NULL_MOVE.isNull());

        assertFalse(RED_MOVE.isNull());
        assertTrue(RED_NULL_MOVE.isNull());

        assertFalse(GREEN_MOVE.isNull());
        assertTrue(GREEN_NULL_MOVE.isNull());

    }

    @Test
    public void testGetColor() {

        assertEquals(Blue, BLUE_MOVE.getColor());
        assertEquals(Yellow, YELLOW_MOVE.getColor());
        assertEquals(Red, RED_MOVE.getColor());
        assertEquals(Green, GREEN_MOVE.getColor());

        assertEquals(Blue, BLUE_NULL_MOVE.getColor());
        assertEquals(Yellow, YELLOW_NULL_MOVE.getColor());
        assertEquals(Red, RED_NULL_MOVE.getColor());
        assertEquals(Green, GREEN_NULL_MOVE.getColor());

    }

    @Test
    public void testGetPiece() {
        assertEquals(PIECE_INSTANCE, BLUE_MOVE.getPiece());
        assertEquals(PIECE_INSTANCE, YELLOW_MOVE.getPiece());
        assertEquals(PIECE_INSTANCE, RED_MOVE.getPiece());
        assertEquals(PIECE_INSTANCE, GREEN_MOVE.getPiece());

        assertEquals(NULL_PIECE_INSTANCE, BLUE_NULL_MOVE.getPiece());
        assertEquals(NULL_PIECE_INSTANCE, YELLOW_NULL_MOVE.getPiece());
        assertEquals(NULL_PIECE_INSTANCE, RED_NULL_MOVE.getPiece());
        assertEquals(NULL_PIECE_INSTANCE, GREEN_NULL_MOVE.getPiece());
    }

    @Test
    public void testToString() {
        assertEquals("Move{color=Blue, piece=PieceComposite{id=1, positions=(0, 0)}}", BLUE_MOVE.toString());
        assertEquals("Move{color=Blue, piece=NULL}", BLUE_NULL_MOVE.toString());
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