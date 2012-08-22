
package blockplus.piece;

import static blockplus.model.piece.PieceComponent.*;
import static blockplus.model.piece.PieceComposite.*;
import static components.direction.Direction.*;
import static components.position.Position.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import blockplus.model.piece.Piece;
import blockplus.model.piece.PieceComponent;
import blockplus.model.piece.PieceInterface;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import components.position.PositionInterface;

@Ignore
public class PieceTest {

    private PieceInterface piece;

    @Before
    public void setup() {
        this.piece = Piece.Piece(2);
    }

    public void tearDown() throws Exception {
        this.piece = null;
    }

    @Test
    public void testGetId() {
        assertEquals(2, this.piece.getId());
    }

    @Test
    public void testGetCorners() {
        fail();
        final Set<PieceInterface> components = this.piece.get();
        final Set<PositionInterface> expected = Sets.newHashSet();
        for (final PieceInterface component : components)
            expected.addAll(component.getCorners());
        assertEquals(expected, this.piece.getCorners());
    }

    @Test
    public void testGetSides() {
        fail();
        final Set<PieceInterface> sides = this.piece.get();
        final Set<PositionInterface> expected = Sets.newHashSet();
        for (final PieceInterface component : sides)
            expected.addAll(component.getSides());
        assertEquals(expected, this.piece.getSides());
    }

    @Test
    public void testgPotentialPositions() {
        fail();
        final SetView<PositionInterface> expected = Sets.difference(this.piece.getCorners(), this.piece.getSides());
        assertEquals(expected, this.piece.getLightPositions());
    }

    @Test
    public void testGet() {
        fail();
        final Set<PieceComponent> expected = Sets.newHashSet(PieceComponent(0, 0), PieceComponent(0, 1));
        final Set<PieceInterface> actual = this.piece.get();
        assertEquals(expected, actual);
    }

    @Test
    public void testIterator() {
        fail();
        final Iterable<PieceInterface> expected = Sets.newHashSet(this.piece);
        final Set<PieceInterface> actual = Sets.newHashSet();
        for (final PieceInterface pieceInterface : this.piece) {
            actual.add(pieceInterface);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        fail();
        final String expected = "" +
                "PieceComposite{" + "\n" +
                "  2, " + "\n" +
                "  PieceComponent{(0, 0)}" + "\n" +
                "  PieceComponent{(0, 1)}" + "\n" +
                "}";
        final String actual = this.piece.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        fail();
        final int expected = ("" +
                "PieceComposite{" + "\n" +
                "  2, " + "\n" +
                "  PieceComponent{(0, 0)}" + "\n" +
                "  PieceComponent{(0, 1)}" + "\n" +
                "}").hashCode();
        final int actual = this.piece.toString().hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsObject() {
        fail();
        assertFalse(this.piece.equals(null));
        assertFalse(this.piece.equals(new Object()));
        assertTrue(this.piece.equals(this.piece));
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(0, 1)));
            assertFalse(this.piece.equals(pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(1, 0)));
            assertFalse(this.piece.equals(pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(1, 0), Position(0, 0)));
            assertFalse(this.piece.equals(pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(1, 0)));
            assertFalse(this.piece.equals(pieceComposite));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(0, 0)));
            assertTrue(this.piece.equals(pieceComposite));
            assertTrue(pieceComposite.equals(this.piece));
        }
        {
            final PieceInterface pieceComposite = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 0), Position(0, 1)));
            assertTrue(this.piece.equals(pieceComposite));
            assertTrue(pieceComposite.equals(this.piece));
        }
    }

    @Test
    public void testTranslateTo() {
        fail();
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(1, 0), Position(1, 1)));
            final PieceInterface actual = this.piece.translateTo(Position(1, 0));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(0, 1), Position(0, 2)));
            final PieceInterface actual = this.piece.translateTo(Position(0, 1));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(1, 1), Position(1, 2)));
            final PieceInterface actual = this.piece.translateTo(Position(1, 1));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testTranslateBy() {
        fail();
        final PieceInterface pieceComposite = this.piece;
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
        fail();
        PieceInterface actual = this.piece;
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
        fail();
        final PositionInterface referential = Position(0, 0);
        PieceInterface actual = this.piece;
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
        fail();
        final PositionInterface referential = Position(5, 5);
        {
            final PieceInterface pieceComposite = this.piece;
            final PieceInterface expected = PieceComposite(2, Position(), Sets.newHashSet(Position(9, 0), Position(10, 0)));
            final PieceInterface actual = pieceComposite.rotateAround(referential);
            assertEquals(expected, actual);
        }
    }

}