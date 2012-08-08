
package blockplus.piece;

import static blockplus.direction.Direction.Direction;
import static blockplus.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import blockplus.position.NullPosition;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;

public class NullPieceComponentTest {

    private PieceInterface nullPieceComponent;

    @Before
    public void setUp() throws Exception {

        this.nullPieceComponent = NullPieceComponent.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        this.nullPieceComponent = null;
    }

    @Test
    public void testHashCode() {
        assertEquals(0, this.nullPieceComponent.hashCode());
    }

    @Test
    public void testGetInstance() {
        assertSame(this.nullPieceComponent, NullPieceComponent.getInstance());
    }

    @Test
    public void testGetId() {
        assertEquals(0, this.nullPieceComponent.getId());
    }

    @Test
    public void testGetReferential() {
        assertEquals(NullPosition.getInstance(), this.nullPieceComponent.getReferential());
    }

    @Test
    public void testGetCorners() {
        assertEquals(ImmutableSet.of(), this.nullPieceComponent.getCorners());
    }

    @Test
    public void testGetSides() {
        assertEquals(ImmutableSet.of(), this.nullPieceComponent.getSides());
    }

    @Test
    public void testGetPotentialPositions() {
        assertEquals(ImmutableSet.of(), this.nullPieceComponent.getPotentialPositions());
    }

    @Test
    public void testGet() {
        assertEquals(ImmutableSet.of(), this.nullPieceComponent.get());
    }

    @Test
    public void testGetPositions() {
        assertEquals(ImmutableSet.of(), this.nullPieceComponent.getPositions());
    }

    @Test
    public void testIterator() {
        assertEquals(Iterators.emptyIterator(), this.nullPieceComponent.iterator());
    }

    @Test
    public void testTranslateTo() {
        assertEquals(this.nullPieceComponent, this.nullPieceComponent.translateTo(Position(1, 1)));
    }

    @Test
    public void testTranslateBy() {
        assertEquals(this.nullPieceComponent, this.nullPieceComponent.translateBy(Direction(1, 1)));
    }

    @Test
    public void testRotate() {
        assertEquals(this.nullPieceComponent, this.nullPieceComponent.rotate());
    }

    @Test
    public void testRotateAround() {
        assertEquals(this.nullPieceComponent, this.nullPieceComponent.rotateAround(Position(1, 1)));
    }

    @Test
    public void testToString() {
        assertEquals(this.nullPieceComponent.getClass().getSimpleName(), this.nullPieceComponent.toString());
    }

    @Test
    public void testEqualsObject() {
        Assert.assertFalse(this.nullPieceComponent.equals(null));
        Assert.assertTrue(this.nullPieceComponent.equals(this.nullPieceComponent));
        Assert.assertFalse(this.nullPieceComponent.equals(new Object()));
        Assert.assertFalse(this.nullPieceComponent.equals(Position(1, 1)));
        Assert.assertTrue(this.nullPieceComponent.equals(NullPieceComponent.getInstance()));
        Assert.assertTrue(NullPieceComponent.getInstance().equals(this.nullPieceComponent));
    }
}