
package blockplus.model.player;

import static blockplus.model.context.Color.Blue;
import static blockplus.model.context.Color.Green;
import static blockplus.model.context.Color.Red;
import static blockplus.model.context.Color.Yellow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;

import blockplus.model.entity.Polyomino;
import blockplus.model.player.Player;
import blockplus.model.player.Players;
import blockplus.model.player.RemainingPieces;

// FIXME add tests for hashCode, equals
public class PlayersTest {

    private static final RemainingPieces REMAINING_PIECES = new RemainingPieces.Builder().add(Polyomino._1).build();
    private final static Player PLAYER1 = Player.from(REMAINING_PIECES);
    private final static Player PLAYER2 = Player.from(REMAINING_PIECES);
    private final static Player PLAYER3 = Player.from(REMAINING_PIECES);
    private final static Player PLAYER4 = Player.from(REMAINING_PIECES);

    private final static Players PLAYERS = new Players.Builder()
            .add(Blue, PLAYER1)
            .add(Yellow, PLAYER2)
            .add(Red, PLAYER3)
            .add(Green, PLAYER4)
            .build();

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithoutAnyPlayer() {
        new Players.Builder().build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyOnePlayer() {
        new Players.Builder().add(Blue, PLAYER1).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyTwoPlayers() {
        new Players.Builder()
                .add(Blue, PLAYER1)
                .add(Yellow, PLAYER2)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyThreePlayers() {
        new Players.Builder()
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
            final Players newPlayers = PLAYERS.apply(Blue, Polyomino._1);
            assertFalse(newPlayers.getAlivePlayer(Blue).remainingPieces().contains(Polyomino._1));
        }
        {
            assertNotNull(PLAYERS.getAlivePlayer(Blue));
            final Players newPlayers = PLAYERS.apply(Blue, null);
            assertNull(newPlayers.getAlivePlayer(Blue));
        }
    }

    @Test
    public void testToString() {
        final String expected = "Players{" +
                "alive={" +
                "Blue=Player{pieces={_1=1}}" + ", " +
                "Yellow=Player{pieces={_1=1}}" + ", " +
                "Red=Player{pieces={_1=1}}" + ", " +
                "Green=Player{pieces={_1=1}}" + "}" + ", " +
                "dead={}" + "}";
        final String actual = PLAYERS.toString();
        assertEquals(expected, actual);
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