
package blockplus.piece;

import static blockplus.direction.Direction.Direction;
import static blockplus.piece.PieceComponent.PieceComponent;
import static blockplus.piece.PieceComposite.PieceComposite;
import static blockplus.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import blockplus.position.PositionInterface;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class PieceCompositeTest {

    private PieceInterface pieceComposite;

    @Before
    public void setup() {
        /*
        TODO !! pouvoir écrire: 
        PieceComposite = PieceComposite(PieceComponent(0, 0), PieceComponent(0, 1), ..., );
        New PieceComposite = PieceComposite.fromReferential(Position(1,1));
        =>  PieceComposite = new PieceComposite.Builder(referential).add(PieceComponent).build();
        */
        this.pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(0, 1)));
    }

    @After
    public void tearDown() throws Exception {
        this.pieceComposite = null;
    }

    @Test
    public void testGetId() {
        assertEquals(2, this.pieceComposite.getId());
    }

    @Test
    public void testGetCorners() {
        final Set<PieceInterface> components = this.pieceComposite.get();
        final Set<PositionInterface> expected = Sets.newHashSet();
        for (final PieceInterface component : components)
            expected.addAll(component.getCorners());
        assertEquals(expected, this.pieceComposite.getCorners());
    }

    @Test
    public void testGetSides() {
        final Set<PieceInterface> sides = this.pieceComposite.get();
        final Set<PositionInterface> expected = Sets.newHashSet();
        for (final PieceInterface component : sides)
            expected.addAll(component.getSides());
        assertEquals(expected, this.pieceComposite.getSides());
    }

    @Test
    public void testgPotentialPositions() {
        final SetView<PositionInterface> expected = Sets.difference(this.pieceComposite.getCorners(), this.pieceComposite.getSides());
        assertEquals(expected, this.pieceComposite.getPotentialPositions());
    }

    @Test
    public void testGet() {
        final Set<PieceComponent> expected = Sets.newHashSet(PieceComponent(0, 0), PieceComponent(0, 1));
        final Set<PieceInterface> actual = this.pieceComposite.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        final Iterable<PieceInterface> expected = Sets.newHashSet(this.pieceComposite);
        final Set<PieceInterface> actual = Sets.newHashSet();
        for (final PieceInterface pieceInterface : this.pieceComposite) {
            actual.add(pieceInterface);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        final String expected = "" +
                "PieceComposite{" + "\n" +
                "  2, " + "\n" +
                "  PieceComponent{(0, 0)}" + "\n" +
                "  PieceComponent{(0, 1)}" + "\n" +
                "}";
        final String actual = this.pieceComposite.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        final int expected = ("" +
                "PieceComposite{" + "\n" +
                "  2, " + "\n" +
                "  PieceComponent{(0, 0)}" + "\n" +
                "  PieceComponent{(0, 1)}" + "\n" +
                "}").hashCode();
        final int actual = this.pieceComposite.toString().hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsObject() {
        assertFalse(this.pieceComposite.equals(null));
        assertFalse(this.pieceComposite.equals(new Object()));
        assertTrue(this.pieceComposite.equals(this.pieceComposite));
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(0, 1)));
            assertFalse(this.pieceComposite.equals(pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(1, 0)));
            assertFalse(this.pieceComposite.equals(pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(1, 0), Position(0, 0)));
            assertFalse(this.pieceComposite.equals(pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(1, 0)));
            assertFalse(this.pieceComposite.equals(pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(0, 0)));
            assertTrue(this.pieceComposite.equals(pieceComposite));
            assertTrue(pieceComposite.equals(this.pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(0, 1)));
            assertTrue(this.pieceComposite.equals(pieceComposite));
            assertTrue(pieceComposite.equals(this.pieceComposite));
        }
    }

    @Test
    public void testTranslateTo() {
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(1, 0), Position(1, 1)));
            final PieceInterface actual = this.pieceComposite.translateTo(Position(1, 0));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(0, 2)));
            final PieceInterface actual = this.pieceComposite.translateTo(Position(0, 1));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(1, 1), Position(1, 2)));
            final PieceInterface actual = this.pieceComposite.translateTo(Position(1, 1));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testTranslateBy() {
        final PieceInterface pieceComposite = this.pieceComposite;
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(1, 0), Position(1, 1)));
            final PieceInterface actual = pieceComposite.translateBy(Direction(1, 0));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(0, 2)));
            final PieceInterface actual = pieceComposite.translateBy(Direction(0, 1));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(1, 1), Position(1, 2)));
            final PieceInterface actual = pieceComposite.translateBy(Direction(1, 1));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testRotate() {
        PieceInterface actual = this.pieceComposite;
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(-1, 0), Position(0, 0)));
            actual = actual.rotate();
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, -1), Position(0, 0)));
            actual = actual.rotate();
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(1, 0)));
            actual = actual.rotate();
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(0, 1)));
            actual = actual.rotate();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testRotateAroundOrigin() {
        final PositionInterface referential = Position(0, 0);
        PieceInterface actual = this.pieceComposite;
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(-1, 0), Position(0, 0)));
            actual = actual.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, -1), Position(0, 0)));
            actual = actual.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(1, 0)));
            actual = actual.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(0, 1)));
            actual = actual.rotateAround(referential);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testRotateAround() {
        final PositionInterface referential = Position(5, 5);
        {
            final PieceInterface pieceComposite = this.pieceComposite;
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(9, 0), Position(10, 0)));
            final PieceInterface actual = pieceComposite.rotateAround(referential);
            assertEquals(expected, actual);
        }
    }

}