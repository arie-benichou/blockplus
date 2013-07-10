
package blockplus.model.player;

import static blockplus.model.entity.Polyomino._1;
import static blockplus.model.entity.Polyomino._2;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import blockplus.model.player.Player;
import blockplus.model.player.RemainingPieces;

public class PlayerTest {

    private final static RemainingPieces PLAYER1_REMAINING_PIECES = new RemainingPieces.Builder().add(_1).build();
    private final static RemainingPieces PLAYER2_REMAINING_PIECES = new RemainingPieces.Builder().add(_2).build();

    private final static Player PLAYER1 = Player.from(PLAYER1_REMAINING_PIECES);
    private final static Player PLAYER2 = Player.from(PLAYER2_REMAINING_PIECES);

    @Test
    public void testGetPieces() {
        Assert.assertNotEquals(PLAYER2.remainingPieces(), PLAYER1.remainingPieces());
        assertEquals(PLAYER1_REMAINING_PIECES, PLAYER1.remainingPieces());
        assertEquals(PLAYER2_REMAINING_PIECES, PLAYER2.remainingPieces());
    }

}