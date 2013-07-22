
package blockplus.model;

import static blockplus.model.Colors.Blue;
import static blockplus.model.Colors.Green;
import static blockplus.model.Colors.Red;
import static blockplus.model.Colors.Yellow;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AdversityTest {

    private final static SidesOrdering ADVERSITY = new SidesOrdering.Builder().add(Blue).add(Yellow).add(Red).add(Green).build();

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithoutAnySide() {
        new SidesOrdering.Builder().build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildOfAdversityWithNullSide() {
        new SidesOrdering.Builder().add((Colors) null);
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyOneSide() {
        new SidesOrdering.Builder().add(Blue).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyTwoSides() {
        new SidesOrdering.Builder().add(Blue).add(Yellow).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithOnlyThreeSides() {
        new SidesOrdering.Builder().add(Blue).add(Yellow).add(Red).build();
    }

    @Test(expected = IllegalStateException.class)
    public void testBuildOfAdversityWithDuplicate() {
        new SidesOrdering.Builder().add(Blue).add(Blue);
    }

    @Test
    public void testGetOpponent() {
        assertEquals(Yellow, ADVERSITY.next(Blue));
        assertEquals(Red, ADVERSITY.next(Yellow));
        assertEquals(Green, ADVERSITY.next(Red));
        assertEquals(Blue, ADVERSITY.next(Green));
    }

}