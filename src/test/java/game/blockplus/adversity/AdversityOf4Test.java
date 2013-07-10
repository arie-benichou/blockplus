
package game.blockplus.adversity;

import static game.blockplus.context.Color.Blue;
import static game.blockplus.context.Color.Green;
import static game.blockplus.context.Color.Red;
import static game.blockplus.context.Color.Yellow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import game.blockplus.adversity.AdversityOf4;
import game.blockplus.context.Color;

import org.junit.Ignore;
import org.junit.Test;

// FIXME add tests for hashCode, equals
public class AdversityOf4Test {

    private final static AdversityOf4 ADVERSITY = new AdversityOf4.Builder().add(Blue, Yellow, Red, Green).build();

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithoutAnySide() {
        new AdversityOf4.Builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildOfAdversityWithNullSide() {
        new AdversityOf4.Builder().add((Color) null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyOneSide() {
        new AdversityOf4.Builder().add(Blue).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyTwoSides() {
        new AdversityOf4.Builder().add(Blue, Yellow).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyThreeSides() {
        new AdversityOf4.Builder().add(Blue, Yellow, Red).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithDuplicate() {
        new AdversityOf4.Builder().add(Blue, Blue);
    }

    @Test
    public void testGetOpponent() {
        assertEquals(Yellow, ADVERSITY.getOpponent(Blue));
        assertEquals(Red, ADVERSITY.getOpponent(Yellow));
        assertEquals(Green, ADVERSITY.getOpponent(Red));
        assertEquals(Blue, ADVERSITY.getOpponent(Green));
    }

    @Test
    public void testToString() {
        assertEquals("AdversityOf4{[Blue, Yellow, Red, Green]}", ADVERSITY.toString());
    }

    @Ignore
    public void testHashCode() {
        fail("Not yet implemented");
    }

    @Ignore
    public void testEqualsObject() {
        fail("Not yet implemented");
    }

}
