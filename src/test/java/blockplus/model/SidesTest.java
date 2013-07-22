
package blockplus.model;

import static blockplus.model.Colors.Blue;
import static blockplus.model.Colors.Green;
import static blockplus.model.Colors.Red;
import static blockplus.model.Colors.Yellow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import blockplus.model.polyomino.Polyomino;

public class SidesTest {

    private final static SidesOrdering SIDES_ORDERING = new SidesOrdering.Builder().add(Blue).add(Yellow).add(Red).add(Green).build();
    private final static Pieces REMAINING_PIECES = new Pieces.Builder().add(Polyomino._0).add(Polyomino._1).build();
    private final static Side PLAYER1 = Side.with(REMAINING_PIECES);
    private final static Side PLAYER2 = Side.with(REMAINING_PIECES);
    private final static Side PLAYER3 = Side.with(REMAINING_PIECES);
    private final static Side PLAYER4 = Side.with(REMAINING_PIECES);
    private final static Sides PLAYERS = new Sides.Builder(SIDES_ORDERING).add(PLAYER1).add(PLAYER2).add(PLAYER3).add(PLAYER4).build();

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithoutAnyPlayer() {
        new Sides.Builder(SIDES_ORDERING).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyOnePlayer() {
        new Sides.Builder(SIDES_ORDERING).add(PLAYER1).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyTwoPlayers() {
        new Sides.Builder(SIDES_ORDERING)
                .add(PLAYER1)
                .add(PLAYER2)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfPlayersWithOnlyThreePlayers() {
        new Sides.Builder(SIDES_ORDERING)
                .add(PLAYER1)
                .add(PLAYER2)
                .add(PLAYER3)
                .build();
    }

    @Test
    public void testGet() {
        assertEquals(PLAYER1, PLAYERS.getSide(Blue));
        assertEquals(PLAYER2, PLAYERS.getSide(Yellow));
        assertEquals(PLAYER3, PLAYERS.getSide(Red));
        assertEquals(PLAYER4, PLAYERS.getSide(Green));
    }

    @Test
    public void testHasAlivePlayer() {
        assertTrue(PLAYERS.hasSide());
    }

    @Test
    public void testApply() {
        {
            assertTrue(PLAYERS.getSide(Blue).remainingPieces().contains(Polyomino._1));
            final Sides newPlayers = PLAYERS.apply(Blue, Polyomino._1);
            assertFalse(newPlayers.getSide(Blue).remainingPieces().contains(Polyomino._1));
        }
        {
            assertFalse(PLAYERS.getSide(Blue).isNull());
            final Sides newPlayers = PLAYERS.apply(Blue, Polyomino._0);
            assertTrue(newPlayers.getSide(Blue).isNull());
        }
    }

}