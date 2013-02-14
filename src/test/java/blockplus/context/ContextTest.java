
package blockplus.context;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import interfaces.move.MoveInterface;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import blockplus.Color;
import blockplus.adversity.AdversityOf4;
import blockplus.board.Board;
import blockplus.board.LayerMutationBuilder;
import blockplus.move.Move;
import blockplus.move.Moves;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceType;
import blockplus.piece.Pieces;
import blockplus.player.Player;
import blockplus.player.Players;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

// FIXME add tests for toString, hashCode, equals
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

    // TODO à compléter
    @Test
    public void testIsTerminal() {
        assertFalse(CONTEXT.isTerminal());
    }

    // TODO à compléter
    @Test
    public void testOptions() {

        // TODO à revoir...
        final PieceInterface p1 = PieceComposite.from(8, Position(0, 0), Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 0), Position(1, 1)));
        final PieceInterface p2 = PieceComposite.from(4, Position(0, 0), Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 0)));
        final PieceInterface p3 = PieceComposite.from(4, Position(0, 0), Sets.newHashSet(Position(0, 0), Position(0, 1), Position(1, 1)));
        final PieceInterface p4 = PieceComposite.from(4, Position(0, 0), Sets.newHashSet(Position(0, 0), Position(1, 0), Position(1, 1)));
        final PieceInterface p5 = PieceComposite.from(2, Position(0, 0), Sets.newHashSet(Position(0, 0), Position(0, 1)));
        final PieceInterface p6 = PieceComposite.from(2, Position(0, 0), Sets.newHashSet(Position(0, 0), Position(1, 0)));
        final PieceInterface p7 = PieceComposite.from(1, Position(0, 0), Sets.newHashSet(Position(0, 0)));

        final List<Move> expected = Lists.newArrayList(
                Moves.getMove(Blue, p1),
                Moves.getMove(Blue, p2),
                Moves.getMove(Blue, p3),
                Moves.getMove(Blue, p4),
                Moves.getMove(Blue, p5),
                Moves.getMove(Blue, p6),
                Moves.getMove(Blue, p7)
                );

        final List<MoveInterface> actual = CONTEXT.options(); // TODO extract Options class
        assertEquals(expected, actual);
    }

    // TODO à compléter
    @Test
    public void testForward() {
        final Context expected = new ContextBuilder()
                .setBoard(BOARD)
                .setSide(Yellow)
                .build();
        final Context actual = CONTEXT.forward();
        assertEquals(expected, actual);
    }

    // TODO à compléter
    @Test
    public void testApply() {
        final Move move = Moves.getNullMove(Blue);
        final Players players = CONTEXT.getPlayers().apply(PLAYER1.apply(move));
        final Context expected = new ContextBuilder()
                .setBoard(BOARD)
                .setPlayers(players)
                .build();
        final Context actual = CONTEXT.apply(move);
        assertEquals(expected, actual);
    }

    @Ignore
    public void testToString() {
        fail("Not yet implemented");
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