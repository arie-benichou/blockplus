
package blockplus.player;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static blockplus.piece.PieceType.PIECE0;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import blockplus.piece.PiecesBag;

import com.google.common.collect.Lists;

public class PlayersTest {

    private final static Player PLAYER1 = new Player(Blue, PiecesBag.EMPTY);
    private final static Player PLAYER2 = new Player(Yellow, PiecesBag.EMPTY);
    private final static Player PLAYER3 = new Player(Red, PiecesBag.EMPTY);
    private final static Player PLAYER4 = new Player(Green, PiecesBag.EMPTY);

    private final static Players PLAYERS = new Players.Builder().add(PLAYER1, PLAYER2, PLAYER3, PLAYER4).build();

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithoutAnyPlayer() {
        new Players.Builder().build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyOnePlayer() {
        new Players.Builder().add(PLAYER1).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyTwoPlayers() {
        new Players.Builder().add(PLAYER1, PLAYER2).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyThreePlayers() {
        new Players.Builder().add(PLAYER1, PLAYER2, PLAYER3).build();
    }

    @Test
    public void testGet() {
        assertEquals(Blue, PLAYERS.get(Blue).getColor());
        assertEquals(Yellow, PLAYERS.get(Yellow).getColor());
        assertEquals(Red, PLAYERS.get(Red).getColor());
        assertEquals(Green, PLAYERS.get(Green).getColor());
    }

    @Test
    public void testHasAlivePlayer() {
        assertFalse(PLAYERS.hasAlivePlayer());
    }

    @Test
    public void testApply() {
        final Player newPlayer = new Player(Blue, new PiecesBag.Builder().add(PIECE0).build());
        final Players newPlayers = PLAYERS.apply(newPlayer);
        assertTrue(newPlayers.hasAlivePlayer());
    }

    @Test
    public void testIterator() {
        final ArrayList<Player> expected = Lists.newArrayList(PLAYER1, PLAYER2, PLAYER3, PLAYER4);
        final ArrayList<Player> actual = Lists.newArrayList(PLAYERS.iterator());
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        final String expected = "Players{[" +
                "Player{alive=false, color=Blue, pieces={}}, " +
                "Player{alive=false, color=Yellow, pieces={}}, " +
                "Player{alive=false, color=Red, pieces={}}, " +
                "Player{alive=false, color=Green, pieces={}}" +
                "]}";
        final String actual = PLAYERS.toString();
        assertEquals(expected, actual);
    }

}