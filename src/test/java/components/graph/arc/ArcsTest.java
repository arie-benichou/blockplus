
package components.graph.arc;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ArcsTest {

    private Arcs<String> arcs;

    @Before
    public void setUp() throws Exception {
        this.arcs = new Arcs<String>();
    }

    @After
    public void tearDown() throws Exception {
        this.arcs = null;
    }

    @Test
    public void testGetArc() {
        final ArcInterface<String> arc1 = this.arcs.get("A", "B");
        final ArcInterface<String> arc2 = this.arcs.get("B", "A");
        final ArcInterface<String> arc3 = this.arcs.get("A", "B");
        assertNotSame(arc1, arc2);
        assertSame(arc1, arc3);
    }

    @Test
    public void testGetWeightedArc() {
        final WeightedArcInterface<String> arc1 = this.arcs.get("A", "B", 2.0);
        final WeightedArcInterface<String> arc2 = this.arcs.get("B", "A", 2.0);
        final WeightedArcInterface<String> arc3 = this.arcs.get("A", "B", 2.0);
        assertNotSame(arc1, arc2);
        assertSame(arc1, arc3);
    }

}