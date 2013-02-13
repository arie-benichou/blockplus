
package blockplus.context;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import interfaces.move.MoveInterface;

import java.util.List;

import org.junit.Test;

import blockplus.Color;
import blockplus.adversity.AdversityOf4;
import blockplus.board.Board;
import blockplus.board.LayerMutationBuilder;
import blockplus.move.Move;
import blockplus.move.Moves;
import blockplus.piece.PieceType;
import blockplus.piece.Pieces;
import blockplus.player.Player;
import blockplus.player.Players;

import com.google.common.collect.Sets;

// FIXME test hashCode, equals
public class ContextTest {

    private final static Pieces PIECES = new Pieces.Builder().addAll(PieceType.asSet()).build();
    private final static Player PLAYER4 = new Player(Green, PIECES);
    private final static Player PLAYER3 = new Player(Red, PIECES);
    private final static Player PLAYER2 = new Player(Yellow, PIECES);
    private final static Player PLAYER1 = new Player(Blue, PIECES);
    private final static int ROWS = 2;
    private final static int COLUMNS = 2;
    private final static Board BOARD = new Board.Builder(Sets.newHashSet(Color.values()), ROWS, COLUMNS)
            .addLayer(Blue, new LayerMutationBuilder().setLightPositions(Position(0, 0)).build())
            .addLayer(Yellow, new LayerMutationBuilder().setLightPositions(Position(0, COLUMNS - 1)).build())
            .addLayer(Red, new LayerMutationBuilder().setLightPositions(Position(ROWS - 1, COLUMNS - 1)).build())
            .addLayer(Green, new LayerMutationBuilder().setLightPositions(Position(ROWS - 1, 0)).build())
            .build();
    private final static Context CONTEXT = new ContextBuilder().setBoard(BOARD).build();

    @Test
    public void testGetSide() {
        assertEquals(Blue, CONTEXT.getSide());
    }

    @Test
    public void testGetNextSide() {
        assertEquals(Yellow, CONTEXT.getNextSide());
    }

    @Test
    public void testGetAdversity() {
        final AdversityOf4 expected = new AdversityOf4.Builder()
                .add(Blue)
                .add(Yellow)
                .add(Red)
                .add(Green)
                .build();
        assertEquals(expected, CONTEXT.getAdversity());
    }

    @Test
    public void testGetPlayers() {
        final Players expected = new Players.Builder()
                .add(PLAYER1)
                .add(PLAYER2)
                .add(PLAYER3)
                .add(PLAYER4)
                .build();
        assertEquals(expected, CONTEXT.getPlayers());
    }

    @Test
    public void testGetPlayer() {
        final Player expected = new Player(Blue, PIECES);
        assertEquals(expected, CONTEXT.getPlayer());
    }

    @Test
    public void testGetBoard() {
        final Board expected = new Board.Builder(Sets.newHashSet(Color.values()), ROWS, COLUMNS)
                .addLayer(Blue, new LayerMutationBuilder().setLightPositions(Position(0, 0)).build())
                .addLayer(Yellow, new LayerMutationBuilder().setLightPositions(Position(0, COLUMNS - 1)).build())
                .addLayer(Red, new LayerMutationBuilder().setLightPositions(Position(ROWS - 1, COLUMNS - 1)).build())
                .addLayer(Green, new LayerMutationBuilder().setLightPositions(Position(ROWS - 1, 0)).build())
                .build();
        assertEquals(expected, CONTEXT.getBoard());
    }

    // FIXME à compléter
    @Test
    public void testIsTerminal() {
        assertFalse(CONTEXT.isTerminal());
    }

    // FIXME à compléter
    @Test
    public void testOptions() {
        final List<MoveInterface> actual = CONTEXT.options();
        assertEquals(7, actual.size()); // TODO extract Options class
    }

    @Test
    public void testForwardBoolean() {
        // FIXME fail("Not yet implemented");
    }

    @Test
    public void testForward() {
        // FIXME fail("Not yet implemented");
    }

    @Test
    public void testApply() {
        // FIXME fail("Not yet implemented");
        final Move move = Moves.getNullMove(Blue);
        CONTEXT.apply(move);
    }

}