
package blockplus.piece;

import static blockplus.direction.Direction.Direction;
import static blockplus.piece.PieceComponent.PieceComponent;
import static blockplus.piece.PieceComposite.PieceComposite;
import static blockplus.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import blockplus.position.PositionInterface;

import com.google.common.collect.Sets;

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

    @Test
    public void testGetId() {
        assertEquals(2, this.pieceComposite.getId());
    }

    @Test
    public void testGetCorners() {
        final Set<PositionInterface> expected = Sets.newHashSet(
                Position(-1, -1),
                Position(-1, 1),
                Position(1, -1),
                Position(1, 1));
        assertEquals(expected, this.pieceComposite.getCorners());
        fail();
    }

    @Test
    public void testGetSides() {
        final Set<PositionInterface> expected = Sets.newHashSet(
                Position(0, -1),
                Position(0, 1),
                Position(1, 0),
                Position(-1, 0));
        assertEquals(expected, this.pieceComposite.getSides());
        fail();
    }

    @Test
    public void testGetExtensions() {
        final Set<PositionInterface> expected = Sets.newHashSet(
                Position(-1, -1),
                Position(-1, 1),
                Position(1, -1),
                Position(1, 1));
        assertEquals(expected, this.pieceComposite.getPotentialPositions());
        fail();
    }

    @Test
    public void testGet() {
        assertEquals(1, this.pieceComposite.get().size());
        assertSame(this.pieceComposite, this.pieceComposite.get().iterator().next());
        fail();
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
        final String expected = "PieceComponent{(0, 0)}";
        final String actual = this.pieceComposite.toString();
        assertEquals(expected, actual);
        fail();
    }

    @Test
    public void testHashCode() {
        final int expected = "PieceComponent{(0, 0)}".hashCode();
        final int actual = this.pieceComposite.toString().hashCode();
        assertEquals(expected, actual);
        fail();
    }

    @Test
    public void testEqualsObject() {
        assertFalse(this.pieceComposite.equals(null));
        assertFalse(this.pieceComposite.equals(new Object()));
        assertTrue(this.pieceComposite.equals(this.pieceComposite));
        assertFalse(this.pieceComposite.equals(PieceComponent(1, 0)));
        assertFalse(this.pieceComposite.equals(PieceComponent(0, 1)));
        assertFalse(this.pieceComposite.equals(PieceComponent(1, 1)));
        assertTrue(this.pieceComposite.equals(PieceComponent(0, 0)));
        assertTrue(PieceComponent(0, 0).equals(this.pieceComposite));
        fail();
    }

    @Test
    public void testTranslateTo() {
        {
            final PieceInterface expected = PieceComponent(1, 0);
            final PieceInterface actual = this.pieceComposite.translateTo(Position(1, 0));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComponent(0, 1);
            final PieceInterface actual = this.pieceComposite.translateTo(Position(0, 1));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComponent(1, 1);
            final PieceInterface actual = this.pieceComposite.translateTo(Position(1, 1));
            assertEquals(expected, actual);
        }
        fail();
    }

    @Test
    public void testTranslateBy() {
        fail();
        final PieceInterface pieceComponent = PieceComponent(0, 0);
        {
            final PieceInterface expected = PieceComponent(1, 0);
            final PieceInterface actual = pieceComponent.translateBy(Direction(1, 0));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComponent(0, 1);
            final PieceInterface actual = pieceComponent.translateBy(Direction(0, 1));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComponent(1, 1);
            final PieceInterface actual = pieceComponent.translateBy(Direction(1, 1));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testRotate() {
        fail();
        {
            final PieceInterface pieceComponent = PieceComponent(0, 0);
            final PieceInterface expected = pieceComponent;
            final PieceInterface actual = pieceComponent.rotate();
            assertEquals(expected, actual);
            assertSame(expected, actual);
        }
        {
            final PieceInterface pieceComponent = PieceComponent(0, 1);
            final PieceInterface expected = pieceComponent;
            final PieceInterface actual = pieceComponent.rotate();
            assertEquals(expected, actual);
            assertSame(expected, actual);
        }
        {
            final PieceInterface pieceComponent = PieceComponent(1, 0);
            final PieceInterface expected = pieceComponent;
            final PieceInterface actual = pieceComponent.rotate();
            assertEquals(expected, actual);
            assertSame(expected, actual);
        }
        {
            final PieceInterface pieceComponent = PieceComponent(1, 1);
            final PieceInterface expected = pieceComponent;
            final PieceInterface actual = pieceComponent.rotate();
            assertEquals(expected, actual);
            assertSame(expected, actual);
        }
    }

    @Test
    public void testRotateAroundOrigin() {
        fail();
        final PositionInterface referential = Position(0, 0);
        {
            final PieceInterface pieceComponent = PieceComponent(0, 0);
            final PieceInterface expected = PieceComponent(0, 0);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {
            final PieceInterface pieceComponent = PieceComponent(1, 0);
            final PieceInterface expected = PieceComponent(0, 1);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {
            final PieceInterface pieceComponent = PieceComponent(0, 1);
            final PieceInterface expected = PieceComponent(-1, 0);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {
            final PieceInterface pieceComponent = PieceComponent(-1, 0);
            final PieceInterface expected = PieceComponent(0, -1);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {
            final PieceInterface pieceComponent = PieceComponent(0, -1);
            final PieceInterface expected = PieceComponent(1, 0);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testRotateAroundNotOrigin() {
        fail();
        final PieceInterface pieceComponent = PieceComponent(0, 0);
        { //0
            final PositionInterface referential = Position(-1, -1);
            final PieceInterface expected = PieceComponent(-2, 0);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {//1
            final PositionInterface referential = Position(-1, 0);
            final PieceInterface expected = PieceComponent(-1, 1);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {//2
            final PositionInterface referential = Position(-1, 1);
            final PieceInterface expected = PieceComponent(0, 2);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {//3
            final PositionInterface referential = Position(0, -1);
            final PieceInterface expected = PieceComponent(-1, -1);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {//4
            final PositionInterface referential = Position(0, 0);
            final PieceInterface expected = PieceComponent(0, 0);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {//5
            final PositionInterface referential = Position(0, 1);
            final PieceInterface expected = PieceComponent(1, 1);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {//6
            final PositionInterface referential = Position(1, -1);
            final PieceInterface expected = PieceComponent(0, -2);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {//7
            final PositionInterface referential = Position(1, 0);
            final PieceInterface expected = PieceComponent(1, -1);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
        {//8
            final PositionInterface referential = Position(1, 1);
            final PieceInterface expected = PieceComponent(2, 0);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }

    }

    @Test
    public void testRotateAround() {
        fail();
        final PositionInterface referential = Position(5, 5);
        {
            final PieceInterface pieceComponent = PieceComponent(5, 6);
            final PieceInterface expected = PieceComponent(4, 5);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
    }

}