
package blockplus.model;

import static blockplus.model.polyomino.Polyomino._1;
import static blockplus.model.polyomino.Polyomino._2;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import blockplus.model.Side;
import blockplus.model.PolyominoSet;

public class SideTest {

    private final static PolyominoSet PLAYER1_REMAINING_PIECES = new PolyominoSet.Builder().add(_1).build();
    private final static PolyominoSet PLAYER2_REMAINING_PIECES = new PolyominoSet.Builder().add(_2).build();

    private final static Side PLAYER1 = Side.from(PLAYER1_REMAINING_PIECES);
    private final static Side PLAYER2 = Side.from(PLAYER2_REMAINING_PIECES);

    @Test
    public void testGetPieces() {
        Assert.assertNotEquals(PLAYER2.remainingPieces(), PLAYER1.remainingPieces());
        assertEquals(PLAYER1_REMAINING_PIECES, PLAYER1.remainingPieces());
        assertEquals(PLAYER2_REMAINING_PIECES, PLAYER2.remainingPieces());
    }

}