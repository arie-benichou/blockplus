
package blockplus.model;

import static blockplus.model.Colors.Blue;
import static blockplus.model.Colors.Green;
import static blockplus.model.Colors.Red;
import static blockplus.model.Colors.Yellow;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

import blockplus.model.Board.Layer;
import blockplus.model.Board.LayerMutationBuilder;
import blockplus.model.Board.State;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import components.cells.Directions;
import components.cells.Positions;
import components.cells.Positions.Position;

// FIXME add tests for toString, hashCode, equals
public class BoardTest {

    private final static int ROWS = 20;
    private final static int COLUMNS = 20;
    private static final Positions POSITIONS = new Positions(ROWS, COLUMNS);
    private final static Set<Colors> COLORS = ImmutableSet.of(Blue, Yellow, Red, Green);
    private final static Board BOARD = new Board.Builder(COLORS, POSITIONS)
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
        final Layer expected = new Layer(POSITIONS);
        assertEquals(expected, BOARD.get(Blue));
        assertEquals(expected, BOARD.get(Yellow));
        assertEquals(expected, BOARD.get(Red));
        assertEquals(expected, BOARD.get(Green));
    }

    @Test
    public void testApply() {

        final Position position = POSITIONS.get(0, 0);

        final Set<Position> positions = Sets.newHashSet(position);
        final Iterable<Position> lights = POSITIONS.neighbours(position, Directions.CORNERS);
        final Iterable<Position> shadows = POSITIONS.neighbours(position, Directions.SIDES);

        final Map<Position, State> selfMutation = new LayerMutationBuilder()
                .setSelfPositions(positions)
                .setShadowPositions(shadows)
                .setLightPositions(lights)
                .build();

        final Map<Position, State> othersMutation = new LayerMutationBuilder()
                .setOtherPositions(positions)
                .build();

        final Layer newBlueLayer = BOARD.get(Blue).apply(selfMutation);
        final Layer newYellowLayer = BOARD.get(Yellow).apply(othersMutation);
        final Layer newRedLayer = BOARD.get(Red).apply(othersMutation);
        final Layer newGreenLayer = BOARD.get(Green).apply(othersMutation);

        final Board expected = Board.builder(BOARD.getColors(), POSITIONS)
                .addLayer(Blue, newBlueLayer)
                .addLayer(Yellow, newYellowLayer)
                .addLayer(Red, newRedLayer)
                .addLayer(Green, newGreenLayer)
                .build();

        final Board actual = BOARD.apply(Blue, positions, shadows, lights);

        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        assertEquals("Board{rows=20, columns=20, data={\"Blue\":[],\"Yellow\":[],\"Red\":[],\"Green\":[]}}", BOARD.toString());
    }

}