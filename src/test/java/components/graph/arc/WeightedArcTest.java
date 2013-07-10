
package components.graph.arc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WeightedArcTest {

    private Arc<String> arc;
    private WeightedArc<String> weightedArc;

    @Before
    public void setUp() throws Exception {
        this.arc = Arc.from("A", "B");
        this.weightedArc = WeightedArc.from(this.arc, 2.0);
    }

    @After
    public void tearDown() throws Exception {
        this.arc = null;
        this.weightedArc = null;
    }

    @Test
    public void testFromArcInterfaceOfTDouble() {
        assertNotNull(this.weightedArc);
    }

    @Test
    public void testFromTTDouble() {
        assertNotNull(WeightedArc.from("A", "B", 2.0));
    }

    @Test
    public void testFromTT() {
        assertNotNull(WeightedArc.from("A", "B"));
    }

    @Test
    public void testGet() {
        assertEquals(WeightedArc.from("A", "B", 2.0), this.weightedArc.get());
    }

    @Test
    public void testGetEndPoint1() {
        assertEquals(this.arc.getEndPoint1(), this.weightedArc.getEndPoint1());
    }

    @Test
    public void testGetEndPoint2() {
        assertEquals(this.arc.getEndPoint2(), this.weightedArc.getEndPoint2());
    }

    @Test
    public void testGetWeight() {
        assertEquals(2.0, this.weightedArc.getWeight(), 0.1);
    }

    @Test
    public void testToString() {
        assertEquals("WeightedArc{Arc{A, B}, 2.0}", this.weightedArc.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(this.weightedArc.toString().hashCode(), this.weightedArc.hashCode());
    }

    @Test
    public void testEqualsObject() {
        assertFalse(this.weightedArc.equals(null));
        assertTrue(this.weightedArc.equals(this.weightedArc));
        assertFalse(this.weightedArc.equals(new Object()));
        assertFalse(this.weightedArc.equals(WeightedArc.from(1, 2, 2.0)));
        assertFalse(this.weightedArc.equals(WeightedArc.from("B", "A", 2.0)));
        assertFalse(this.weightedArc.equals(WeightedArc.from("A", "C", 2.0)));
        assertFalse(this.weightedArc.equals(WeightedArc.from("C", "B", 2.0)));
        assertTrue(this.weightedArc.equals(WeightedArc.from("A", "B", 2.0)));
        assertFalse(this.weightedArc.equals(this.arc));
    }

    @Test
    public void testCompareTo() {
        assertEquals(0, this.weightedArc.compareTo(this.weightedArc));
        assertEquals(-1, WeightedArc.from("A", "B").compareTo(this.weightedArc));
        assertEquals(1, this.weightedArc.compareTo(WeightedArc.from("A", "B")));
        assertEquals(0, this.weightedArc.compareTo(WeightedArc.from("C", "D", 2.0)));
    }

}