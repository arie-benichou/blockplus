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

import static components.direction.Direction.Direction;
import static components.position.Position.Position;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import junit.framework.Assert;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import blockplus.model.piece.NullPieceComponent;
import blockplus.model.piece.PieceInterface;

import com.google.common.collect.ImmutableSet;
import components.position.NullPosition;

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
        assertEquals(ImmutableSet.of(), this.nullPieceComponent.getLightPositions());
    }

    @Test
    public void testGet() {
        assertEquals(ImmutableSet.of(), this.nullPieceComponent.get());
    }

    @Test
    public void testGetPositions() {
        assertEquals(ImmutableSet.of(), this.nullPieceComponent.getSelfPositions());
    }

    @Test
    public void testIterator() {
        assertFalse(this.nullPieceComponent.iterator().hasNext());
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