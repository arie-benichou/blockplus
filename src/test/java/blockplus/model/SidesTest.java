
package blockplus.model;

import static blockplus.model.Colors.Blue;
import static blockplus.model.Colors.Green;
import static blockplus.model.Colors.Red;
import static blockplus.model.Colors.Yellow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import blockplus.model.polyomino.Polyomino;

// FIXME add tests for hashCode, equals
public class SidesTest {

    private static final ColoredPolyominoSet REMAINING_PIECES = new ColoredPolyominoSet.Builder().add(Polyomino._1).build();
    private final static Side PLAYER1 = Side.from(REMAINING_PIECES);
    private final static Side PLAYER2 = Side.from(REMAINING_PIECES);
    private final static Side PLAYER3 = Side.from(REMAINING_PIECES);
    private final static Side PLAYER4 = Side.from(REMAINING_PIECES);

    private final static Sides PLAYERS = new Sides.Builder()
            .add(Blue, PLAYER1)
            .add(Yellow, PLAYER2)
            .add(Red, PLAYER3)
            .add(Green, PLAYER4)
            .build();

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithoutAnyPlayer() {
        new Sides.Builder().build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyOnePlayer() {
        new Sides.Builder().add(Blue, PLAYER1).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyTwoPlayers() {
        new Sides.Builder()
                .add(Blue, PLAYER1)
                .add(Yellow, PLAYER2)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyThreePlayers() {
        new Sides.Builder()
                .add(Blue, PLAYER1)
                .add(Yellow, PLAYER2)
                .add(Red, PLAYER3)
                .build();
    }

    @Test
    public void testGet() {
        assertEquals(PLAYER1, PLAYERS.getAlivePlayer(Blue));
        assertEquals(PLAYER2, PLAYERS.getAlivePlayer(Yellow));
        assertEquals(PLAYER3, PLAYERS.getAlivePlayer(Red));
        assertEquals(PLAYER4, PLAYERS.getAlivePlayer(Green));
    }

    @Test
    public void testHasAlivePlayer() {
        assertTrue(PLAYERS.hasAlivePlayer());
    }

    @Test
    public void testApply() {
        {
            assertTrue(PLAYERS.getAlivePlayer(Blue).remainingPieces().contains(Polyomino._1));
            final Sides newPlayers = PLAYERS.apply(Blue, Polyomino._1);
            assertFalse(newPlayers.getAlivePlayer(Blue).remainingPieces().contains(Polyomino._1));
        }
        {
            assertNotNull(PLAYERS.getAlivePlayer(Blue));
            final Sides newPlayers = PLAYERS.apply(Blue, Polyomino._0);
            assertNull(newPlayers.getAlivePlayer(Blue));
        }
    }

}