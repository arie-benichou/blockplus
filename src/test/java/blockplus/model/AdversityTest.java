
package blockplus.model;

import static blockplus.model.Colors.Blue;
import static blockplus.model.Colors.Green;
import static blockplus.model.Colors.Red;
import static blockplus.model.Colors.Yellow;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AdversityTest {

    private final static Adversity ADVERSITY = new Adversity.Builder().add(Blue, Yellow, Red, Green).build();

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithoutAnySide() {
        new Adversity.Builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildOfAdversityWithNullSide() {
        new Adversity.Builder().add((Colors) null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyOneSide() {
        new Adversity.Builder().add(Blue).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyTwoSides() {
        new Adversity.Builder().add(Blue, Yellow).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyThreeSides() {
        new Adversity.Builder().add(Blue, Yellow, Red).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithDuplicate() {
        new Adversity.Builder().add(Blue, Blue);
    }

    @Test
    public void testGetOpponent() {
        assertEquals(Yellow, ADVERSITY.getOpponent(Blue));
        assertEquals(Red, ADVERSITY.getOpponent(Yellow));
        assertEquals(Green, ADVERSITY.getOpponent(Red));
        assertEquals(Blue, ADVERSITY.getOpponent(Green));
    }

}