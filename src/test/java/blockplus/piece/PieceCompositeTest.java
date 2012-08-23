
package blockplus.piece;

import static blockplus.model.piece.PieceComponent.*;
import static blockplus.model.piece.PieceComposite.*;
import static components.direction.Direction.*;
import static components.position.Position.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import blockplus.model.piece.PieceComponent;
import blockplus.model.piece.PieceInterface;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import components.position.PositionInterface;

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
        final Set<PositionInterface> actual = this.pieceComposite.getCorners();
        assertEquals(8, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSides() {
        final Set<PieceInterface> sides = this.pieceComposite.get();
        final Set<PositionInterface> expected = Sets.newHashSet();
        for (final PieceInterface component : sides)
            expected.addAll(component.getSides());
        final Set<PositionInterface> actual = this.pieceComposite.getSides();
        assertEquals(8, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetLightPositions() {
        final SetView<PositionInterface> expected = Sets.difference(this.pieceComposite.getCorners(), this.pieceComposite.getSides());
        final Set<PositionInterface> actual = this.pieceComposite.getLightPositions();
        assertEquals(4, actual.size());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetShadowPositions() {
        final SetView<PositionInterface> expected = Sets.difference(this.pieceComposite.getSides(), this.pieceComposite.getSelfPositions());
        final Set<PositionInterface> actual = this.pieceComposite.getShadowPositions();
        assertEquals(6, actual.size());
        assertEquals(expected, actual);
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

    @Test
    public void testReflectAlongVerticalAxisWithReferential() {
        final PositionInterface referential = Position(Integer.MAX_VALUE, 2);
        {
            final PieceInterface pieceComposite = this.pieceComposite;
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 3), Position(0, 4)));
            final PieceInterface actual = pieceComposite.reflectAlongVerticalAxis(referential);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testReflectAlongVerticalAxis() {
        {
            final PieceInterface pieceComposite = PieceComposite(3, Position(2, 5), Sets.newHashSet(Position(3, 5), Position(2, 5), Position(2, 6)));
            final PieceInterface expected = PieceComposite(2, Position(2, 5), Sets.newHashSet(Position(3, 5), Position(2, 5), Position(2, 4)));
            final PieceInterface actual = pieceComposite.reflectAlongVerticalAxis();
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testReflectAlongHorizontalAxisWithReferential() {
        final PositionInterface referential = Position(1, Integer.MAX_VALUE);
        {
            final PieceInterface pieceComposite = this.pieceComposite;
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(2, 0), Position(2, 1)));
            final PieceInterface actual = pieceComposite.reflectAlongHorizontalAxis(referential);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testReflectAlongHorizontalAxis() {
        {
            final PieceInterface pieceComposite = PieceComposite(3, Position(2, 5), Sets.newHashSet(Position(3, 5), Position(2, 5), Position(2, 6)));
            final PieceInterface expected = PieceComposite(2, Position(2, 5), Sets.newHashSet(Position(1, 5), Position(2, 5), Position(2, 6)));
            final PieceInterface actual = pieceComposite.reflectAlongHorizontalAxis();
            assertEquals(expected, actual);
        }
    }

}