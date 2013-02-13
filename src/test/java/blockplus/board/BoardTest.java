
package blockplus.board;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Test;

import blockplus.Color;

import com.google.common.collect.ImmutableSet;

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

    // FIXME @Test
    public void testIsLegal() {
        fail("Not yet implemented");
    }

    // FIXME @Test
    public void testApply() {
        fail("Not yet implemented");
    }

}
