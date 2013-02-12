
package blockplus.player;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.piece.PieceType.PIECE0;
import static blockplus.piece.PieceType.PIECE1;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.junit.Test;

import blockplus.move.Move;
import blockplus.move.Moves;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceType;
import blockplus.piece.PiecesBag;

import com.google.common.collect.Lists;

public class PlayerTest {

    private final static PiecesBag PIECES_BAG = new PiecesBag.Builder().add(PIECE0).add(PIECE1).build();
    private final static Player ALIVE_PLAYER = new Player(Blue, PIECES_BAG);
    private final static Player DEAD_PLAYER = new Player(Green, PiecesBag.EMPTY);

    @Test
    public void testGetColor() {
        assertEquals(Blue, ALIVE_PLAYER.getColor());
        assertEquals(Green, DEAD_PLAYER.getColor());
    }

    @Test
    public void testGetPieces() {
        assertEquals(PIECES_BAG, ALIVE_PLAYER.getPieces());
        assertEquals(PiecesBag.EMPTY, DEAD_PLAYER.getPieces());
    }

    @Test
    public void testIsAlive() {
        assertTrue(ALIVE_PLAYER.isAlive());
        assertFalse(DEAD_PLAYER.isAlive());
    }

    @Test
    public void testApplyMoveInterface() {
        {
            final ArrayList<Entry<PieceType, Integer>> entries = Lists.newArrayList(ALIVE_PLAYER.getPieces());
            final PieceType pieceType = entries.get(1).getKey();
            final PieceInterface pieceInstance = pieceType.iterator().next();
            final Move move = Moves.getMove(ALIVE_PLAYER.getColor(), pieceInstance);
            final Player newPlayer = ALIVE_PLAYER.apply(move);
            assertTrue(newPlayer.isAlive());
            assertFalse(newPlayer.apply(Moves.getNullMove(newPlayer.getColor())).isAlive());
        }
    }

    @Test
    public void testToString() {
        assertEquals("Player{alive=true, color=Blue, pieces={PIECE0=1, PIECE1=1}}", ALIVE_PLAYER.toString());
        assertEquals("Player{alive=false, color=Green, pieces={}}", DEAD_PLAYER.toString());
    }

}