
package blockplus.adversity;

import static blockplus.Color.Blue;
import static blockplus.Color.Green;
import static blockplus.Color.Red;
import static blockplus.Color.Yellow;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import blockplus.adversity.AdversityOf4.Builder;

public class AdversityOf4Test {

    private AdversityOf4 adversity;

    @Before
    public void setup() {
        final Builder builder = new AdversityOf4.Builder();
        builder.add(Blue);
        builder.add(Yellow);
        builder.add(Red);
        builder.add(Green);
        this.adversity = builder.build();
    }

    @Test
    public void testGetOpponent() {
        assertEquals(Yellow, this.adversity.getOpponent(Blue));
        assertEquals(Red, this.adversity.getOpponent(Yellow));
        assertEquals(Green, this.adversity.getOpponent(Red));
        assertEquals(Blue, this.adversity.getOpponent(Green));
    }

    @Test
    public void testToString() {
        assertEquals("AdversityOf4{[Blue, Yellow, Red, Green]}", this.adversity.toString());
    }

}
