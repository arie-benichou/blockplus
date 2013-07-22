
package blockplus.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoardTest {

    private final static int ROWS = 20;
    private final static int COLUMNS = 20;
    private final static Board BOARD = Board.of(ROWS, COLUMNS);

    @Test
    public void testGetColors() {
        assertEquals(Colors.set(), BOARD.getColors());
    }

    @Test
    public void testRows() {
        assertEquals(ROWS, BOARD.rows());
    }

    @Test
    public void testColumns() {
        assertEquals(COLUMNS, BOARD.columns());
    }

    /*
    @Test
    public void testApply() {

        final IPosition position = Position(0, 0);

        final Set<IPosition> positions = Sets.newHashSet(position);
        final Iterable<IPosition> lights = Positions.neighbours(position, Directions.CORNERS);
        final Iterable<IPosition> shadows = Positions.neighbours(position, Directions.SIDES);

        final Map<IPosition, State> selfMutation = new LayerMutationBuilder()
                .setSelfPositions(positions)
                .setShadowPositions(shadows)
                .setLightPositions(lights)
                .build();

        final Map<IPosition, State> othersMutation = new LayerMutationBuilder()
                .setOtherPositions(positions)
                .build();

        final Layer newBlueLayer = BOARD.get(Blue).apply(selfMutation);
        final Layer newYellowLayer = BOARD.get(Yellow).apply(othersMutation);
        final Layer newRedLayer = BOARD.get(Red).apply(othersMutation);
        final Layer newGreenLayer = BOARD.get(Green).apply(othersMutation);

        final Board expected = new Board.Builder(ROWS, COLUMNS, BOARD.getColors())
                .addLayer(Blue, newBlueLayer)
                .addLayer(Yellow, newYellowLayer)
                .addLayer(Red, newRedLayer)
                .addLayer(Green, newGreenLayer)
                .build();

        final Board actual = BOARD.apply(Blue, positions, shadows, lights);

        assertEquals(expected, actual);
    }

    @Test
    public void testApply2() {
        final Object expected = null;
        final PolyominoTranslatedInstance instance = Polyominos.getInstance().get((SortedSet<IPosition>) Polyomino._1.positions());
        final Board actual = BOARD.apply(Blue, instance);
        //System.out.println(actual.get(Blue));
        assertEquals(expected, actual);
    }
    */

}