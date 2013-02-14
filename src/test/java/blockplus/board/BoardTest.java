
package blockplus.board;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import blockplus.Color;
import blockplus.board.Layer.State;
import blockplus.move.Move;
import blockplus.move.Moves;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import components.position.PositionInterface;

// FIXME test hashCode, equals
public class BoardTest {

    private final static int ROWS = 20;
    private final static int COLUMNS = 20;
    private final static Set<Color> COLORS = ImmutableSet.of(Blue, Yellow, Red, Green);
    private final static Board BOARD = new Board.Builder(COLORS, ROWS, COLUMNS)
            .addLayer(Blue)
            .addLayer(Yellow)
            .addLayer(Red)
            .addLayer(Green)
            .build();

    @Test
    public void testGetColors() {
        assertEquals(COLORS, BOARD.getColors());
    }

    @Test
    public void testRows() {
        assertEquals(ROWS, BOARD.rows());
    }

    @Test
    public void testColumns() {
        assertEquals(COLUMNS, BOARD.columns());
    }

    @Test
    public void testGetLayer() {
        final Layer expected = new Layer(ROWS, COLUMNS);
        assertEquals(expected, BOARD.getLayer(Blue));
        assertEquals(expected, BOARD.getLayer(Yellow));
        assertEquals(expected, BOARD.getLayer(Red));
        assertEquals(expected, BOARD.getLayer(Green));
    }

    @Test
    public void testIsLegal() {
        {
            final Move move = Moves.getNullMove(Blue);
            assertTrue(BOARD.isLegal(move));
        }

        {
            final PositionInterface position = Position(0, 0);
            final PieceInterface p1 = PieceComposite.from(1, position, Sets.newHashSet(position)); // TODO à revoir
            final Move move = Moves.getMove(Blue, p1);
            assertFalse(BOARD.isLegal(move));

            final Map<PositionInterface, State> layerMutation = new LayerMutationBuilder().setLightPositions(position).build();
            final Board board = Board.builder(BOARD.getColors(), BOARD.rows(), BOARD.columns())
                    .addLayer(Blue, BOARD.getLayer(Blue).apply(layerMutation))
                    .addLayer(Yellow, BOARD.getLayer(Yellow))
                    .addLayer(Red, BOARD.getLayer(Red))
                    .addLayer(Green, BOARD.getLayer(Green))
                    .build(); // TODO à revoir
            assertTrue(board.isLegal(move));
        }
    }

    @Test
    public void testApply() {
        {
            final Move move = Moves.getNullMove(Blue);
            final Board expected = BOARD;
            final Board actual = BOARD.apply(move);
            assertEquals(expected, actual);
        }
        {
            final PositionInterface position = Position(0, 0);
            final PieceInterface p1 = PieceComposite.from(1, position, Sets.newHashSet(position)); // TODO à revoir
            final Move move = Moves.getMove(Blue, p1);

            final Map<PositionInterface, State> selfMutation = new LayerMutationBuilder()
                    .setSelfPositions(p1.getSelfPositions())
                    .setShadowPositions(p1.getShadowPositions())
                    .setLightPositions(p1.getLightPositions())
                    .build();

            final Map<PositionInterface, State> othersMutation = new LayerMutationBuilder()
                    .setOtherPositions(p1.getSelfPositions())
                    .build();

            final Board expected = Board.builder(BOARD.getColors(), BOARD.rows(), BOARD.columns())
                    .addLayer(Blue, BOARD.getLayer(Blue).apply(selfMutation))
                    .addLayer(Yellow, BOARD.getLayer(Yellow).apply(othersMutation))
                    .addLayer(Red, BOARD.getLayer(Red).apply(othersMutation))
                    .addLayer(Green, BOARD.getLayer(Green).apply(othersMutation))
                    .build(); // TODO à revoir

            final Board actual = BOARD.apply(move);

            assertEquals(expected.getLayer(Blue), actual.getLayer(Blue));
            assertEquals(expected.getLayer(Yellow), actual.getLayer(Yellow));
            assertEquals(expected.getLayer(Red), actual.getLayer(Red));
            assertEquals(expected.getLayer(Green), actual.getLayer(Green));
            assertEquals(expected, actual);
        }
    }

    // FIXME @Test
    public void testToString() {
        fail("Not yet implemented");
    }

    // FIXME @Test
    public void testHashCode() {
        fail("Not yet implemented");
    }

    // FIXME @Test
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

}