
package blockplus.model.adversity;

import static blockplus.model.context.Color.Blue;
import static blockplus.model.context.Color.Green;
import static blockplus.model.context.Color.Red;
import static blockplus.model.context.Color.Yellow;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import blockplus.model.context.Color;

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

}