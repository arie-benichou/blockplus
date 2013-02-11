
package blockplus.player;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.piece.PieceType.PIECE0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import blockplus.piece.PiecesBag;

public class PlayerTest {

    private final static PiecesBag PIECES_BAG = new PiecesBag.Builder().add(PIECE0).build();
    private final static Player ALIVE_PLAYER = new Player(Blue, PIECES_BAG);
    private final static Player DEAD_PLAYER = new Player(Green, PiecesBag.EMPTY);

    @Test
    public void testGetColor() {
        assertEquals(Blue, ALIVE_PLAYER.getColor());
        assertEquals(Green, DEAD_PLAYER.getColor());
    }

    //TODO @Test
    public void testGetPieces() {
        assertEquals(PIECES_BAG, ALIVE_PLAYER.getPieces());
        assertEquals(PiecesBag.EMPTY, DEAD_PLAYER.getPieces());
    }

    //TODO @Test
    public void testApplyMoveInterface() {
        fail("Not yet implemented");
    }

    @Test
    public void testIsAlive() {
        assertTrue(ALIVE_PLAYER.isAlive());
        assertFalse(DEAD_PLAYER.isAlive());
    }

    //TODO @Test
    public void testToString() {
        assertEquals("", ALIVE_PLAYER.toString());
    }

}
