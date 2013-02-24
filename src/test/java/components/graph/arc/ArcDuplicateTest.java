
package components.graph.arc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import components.graph.arc.Arc;
import components.graph.arc.ArcDuplicate;

// TODO tester la factory
public class ArcDuplicateTest {

    private Arc<String> arc;
    private ArcDuplicate<String> arcDuplicate;

    @Before
    public void setUp() throws Exception {
        this.arc = Arc.from("A", "B");
        this.arcDuplicate = ArcDuplicate.from(this.arc);
    }

    @After
    public void tearDown() throws Exception {
        this.arc = null;
        this.arcDuplicate = null;
    }

    @Test
    public void testFrom() {
        assertNotNull(this.arcDuplicate);
    }

    @Test
    public void testGet() {
        assertEquals(Arc.from("A", "B"), this.arcDuplicate.get());
    }

    @Test
    public void testGetEndPoint1() {
        assertEquals(this.arc.getEndPoint1(), this.arcDuplicate.getEndPoint1());
    }

    @Test
    public void testGetEndPoint2() {
        assertEquals(this.arc.getEndPoint2(), this.arcDuplicate.getEndPoint2());
    }

    @Test
    public void testToString() {
        assertEquals("ArcDuplicate{Arc{A, B}}", this.arcDuplicate.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(this.arcDuplicate.toString().hashCode(), this.arcDuplicate.hashCode());
    }

    @Test
    public void testEqualsObject() {
        assertFalse(this.arcDuplicate.equals(null));
        assertTrue(this.arcDuplicate.equals(this.arcDuplicate));
        assertFalse(this.arcDuplicate.equals(new Object()));
        assertFalse(this.arcDuplicate.equals(ArcDuplicate.from(Arc.from(1, 2))));
        assertFalse(this.arcDuplicate.equals(ArcDuplicate.from(Arc.from("B", "A"))));
        assertFalse(this.arcDuplicate.equals(ArcDuplicate.from(Arc.from("A", "C"))));
        assertFalse(this.arcDuplicate.equals(ArcDuplicate.from(Arc.from("C", "B"))));
        assertTrue(this.arcDuplicate.equals(ArcDuplicate.from(Arc.from("A", "B"))));
        assertFalse(this.arcDuplicate.equals(Arc.from("A", "B")));
    }

}