
package blockplus.piece;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.Sets;

// TODO à compléter
public class PieceComponentTest {

    private PieceInterface pieceComponent;

    @Before
    public void setup() {
        this.pieceComponent = PieceComponent.from();
    }

    @Test
    public void testGet() {
        assertEquals(1, this.pieceComponent.get().size());
        assertSame(this.pieceComponent, this.pieceComponent.get().iterator().next());
    }

    @Test
    public void testGetId() {
        assertEquals(1, this.pieceComponent.getId());
    }

    @Test
    public void testGetCorners() {
        final Set<PositionInterface> expected = Sets.newHashSet(
                Position.from(-1, -1),
                Position.from(-1, 1),
                Position.from(1, -1),
                Position.from(1, 1));
        assertEquals(expected, this.pieceComponent.getCorners());
    }

    @Test
    public void testGetSides() {
        final Set<PositionInterface> expected = Sets.newHashSet(
                Position.from(0, -1),
                Position.from(0, 1),
                Position.from(1, 0),
                Position.from(-1, 0));
        assertEquals(expected, this.pieceComponent.getSides());
    }

    @Test
    public void testGetExtensions() {

        final Set<PositionInterface> expected = Sets.newHashSet(
                Position.from(-1, -1),
                Position.from(-1, 1),
                Position.from(1, -1),
                Position.from(1, 1));
        assertEquals(expected, this.pieceComponent.getPotentialPositions());
    }

}