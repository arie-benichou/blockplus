
package components.graph.arc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArcTest {

    private Arc<String> arc;

    @Before
    public void setUp() throws Exception {
        this.arc = Arc.from("A", "B");
    }

    @After
    public void tearDown() throws Exception {
        this.arc = null;
    }

    @Test
    public void testFrom() {
        assertNotNull(this.arc);
    }

    @Test
    public void testGet() {
        assertSame(this.arc, this.arc.get());
    }

    @Test
    public void testGetEndPoint1() {
        assertEquals("A", this.arc.getEndPoint1());
    }

    @Test
    public void testGetEndPoint2() {
        assertEquals("B", this.arc.getEndPoint2());
    }

    @Test
    public void testToString() {
        assertEquals("Arc{A, B}", this.arc.toString());
    }

    @Test
    public void testHashCode() {
        assertEquals(this.arc.toString().hashCode(), this.arc.hashCode());
    }

    @Test
    public void testEqualsObject() {
        assertFalse(this.arc.equals(null));
        assertTrue(this.arc.equals(this.arc));
        assertFalse(this.arc.equals(new Object()));
        assertFalse(this.arc.equals(Arc.from(1, 2)));
        assertFalse(this.arc.equals(Arc.from("B", "A")));
        assertFalse(this.arc.equals(Arc.from("A", "C")));
        assertFalse(this.arc.equals(Arc.from("C", "B")));
        assertTrue(this.arc.equals(Arc.from("A", "B")));
    }

}