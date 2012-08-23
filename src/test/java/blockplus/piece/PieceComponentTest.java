/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package blockplus.piece;

import static blockplus.model.piece.PieceComponent.*;
import static components.direction.Direction.*;
import static components.position.Position.*;
import static org.junit.Assert.*;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import blockplus.model.piece.PieceInterface;

import com.google.common.collect.Sets;
import components.position.PositionInterface;

public class PieceComponentTest {

    private PieceInterface pieceComponent;

    @Before
    public void setup() {
        this.pieceComponent = PieceComponent(0, 0);
    }

    @After
    public void tearDown() throws Exception {
        this.pieceComponent = null;
    }

    @Test
    public void testGetId() {
        assertEquals(1, this.pieceComponent.getId());
    }

    @Test
    public void testGetCorners() {
        final Set<PositionInterface> expected = Sets.newHashSet(
                Position(-1, -1),
                Position(-1, 1),
                Position(1, -1),
                Position(1, 1));
        assertEquals(expected, this.pieceComponent.getCorners());
    }

    @Test
    public void testGetSides() {
        final Set<PositionInterface> expected = Sets.newHashSet(
                Position(0, -1),
                Position(0, 1),
                Position(1, 0),
                Position(-1, 0));
        assertEquals(expected, this.pieceComponent.getSides());
    }

    @Test
    public void testGetLightPositions() {

        final Set<PositionInterface> expected = Sets.newHashSet(
                Position(-1, -1),
                Position(-1, 1),
                Position(1, -1),
                Position(1, 1));
        assertEquals(expected, this.pieceComponent.getLightPositions());
    }

    @Test
    public void testGetShadowPositions() {

        final Set<PositionInterface> expected = Sets.newHashSet(
                Position(0, -1),
                Position(0, 1),
                Position(1, 0),
                Position(-1, 0));
        assertEquals(expected, this.pieceComponent.getShadowPositions());
    }

    @Test
    public void testGet() {
        assertEquals(1, this.pieceComponent.get().size());
        assertSame(this.pieceComponent, this.pieceComponent.get().iterator().next());
    }

    @Test
    public void testIterator() {
        final Iterable<PieceInterface> expected = Sets.newHashSet(this.pieceComponent);
        final Set<PieceInterface> actual = Sets.newHashSet();
        for (final PieceInterface pieceInterface : this.pieceComponent) {
            actual.add(pieceInterface);
        }
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        final String expected = "PieceComponent{(0, 0)}";
        final String actual = this.pieceComponent.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        final int expected = "PieceComponent{(0, 0)}".hashCode();
        final int actual = this.pieceComponent.toString().hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void testEqualsObject() {
        assertFalse(this.pieceComponent.equals(null));
        assertFalse(this.pieceComponent.equals(new Object()));
        assertTrue(this.pieceComponent.equals(this.pieceComponent));
        assertFalse(this.pieceComponent.equals(PieceComponent(1, 0)));
        assertFalse(this.pieceComponent.equals(PieceComponent(0, 1)));
        assertFalse(this.pieceComponent.equals(PieceComponent(1, 1)));
        assertTrue(this.pieceComponent.equals(PieceComponent(0, 0)));
        assertTrue(PieceComponent(0, 0).equals(this.pieceComponent));
    }

    @Test
    public void testTranslateTo() {
        {
            final PieceInterface expected = PieceComponent(1, 0);
            final PieceInterface actual = this.pieceComponent.translateTo(Position(1, 0));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComponent(0, 1);
            final PieceInterface actual = this.pieceComponent.translateTo(Position(0, 1));
            assertEquals(expected, actual);
        }
        {
            final PieceInterface expected = PieceComponent(1, 1);
            final PieceInterface actual = this.pieceComponent.translateTo(Position(1, 1));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testTranslateBy() {
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
        final PositionInterface referential = Position(5, 5);
        {
            final PieceInterface pieceComponent = PieceComponent(5, 6);
            final PieceInterface expected = PieceComponent(4, 5);
            final PieceInterface actual = pieceComponent.rotateAround(referential);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testReflectAlongVerticalAxisWithReferential() {
        final PositionInterface referential = Position(0, 4);
        {
            final PieceInterface pieceComponent = PieceComponent(2, 5);
            final PieceInterface expected = PieceComponent(2, 3);
            final PieceInterface actual = pieceComponent.reflectAlongVerticalAxis(referential);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testReflectAlongVerticalAxis() {
        {
            final PieceInterface pieceComponent = PieceComponent(2, 5);
            final PieceInterface expected = pieceComponent;
            final PieceInterface actual = pieceComponent.reflectAlongVerticalAxis();
            assertEquals(expected, actual);
        }
    }

}