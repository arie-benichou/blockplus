
package components.graph.features.connectivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.graph.Graph;
import components.graph.GraphBuilder;
import components.graph.features.connectivity.Connectivity;

public class UndirectedGraphConnectivityTest {

    @Test
    public void testGetGraph() {
        final Graph<String> graph = new GraphBuilder<String>(2).addEdge("A", "B").build();
        final Connectivity<String> connectivity = Connectivity.from(graph);
        assertEquals(graph, connectivity.getGraph());
    }

    @Test
    public void testIsConnected1() {
        final Graph<String> graph = new GraphBuilder<String>(4)
                .addEdge("A", "B")
                .addEdge("C", "D")
                .build();
        final Connectivity<String> connectivity = Connectivity.from(graph);
        assertFalse(connectivity.isConnected());
    }

    @Test
    public void testIsConnected2() {
        final Graph<String> graph = new GraphBuilder<String>(2).addEdge("A", "B").build();
        final Connectivity<String> connectivity = Connectivity.from(graph);
        assertTrue(connectivity.isConnected());
    }

    @Test
    public void testIsConnectedTT1() {
        final Graph<String> graph = new GraphBuilder<String>(2).addEdge("A", "B").build();
        final Connectivity<String> connectivity = Connectivity.from(graph);
        assertTrue(connectivity.isConnected("A", "B"));
        assertTrue(connectivity.isConnected("B", "A"));
    }

    @Test
    public void testIsConnectedTT2() {
        final Graph<String> graph = new GraphBuilder<String>(3)
                .addEdge("A", "B")
                .addEdge("A", "C")
                .build();
        final Connectivity<String> connectivity = Connectivity.from(graph);
        assertTrue(connectivity.isConnected("B", "C"));
        assertTrue(connectivity.isConnected("C", "B"));
        assertTrue(connectivity.isConnected("A", "B"));
        assertTrue(connectivity.isConnected("B", "A"));
        assertTrue(connectivity.isConnected("A", "C"));
        assertTrue(connectivity.isConnected("C", "A"));
    }

    @Test
    public void testIsConnectedTT3() {
        final Graph<String> graph = new GraphBuilder<String>(4)
                .addEdge("A", "B")
                .addEdge("C", "D")
                .build();
        final Connectivity<String> connectivity = Connectivity.from(graph);
        assertFalse(connectivity.isConnected("A", "C"));
        assertFalse(connectivity.isConnected("C", "A"));
        assertFalse(connectivity.isConnected("A", "D"));
        assertFalse(connectivity.isConnected("D", "A"));
        assertTrue(connectivity.isConnected("A", "B"));
        assertTrue(connectivity.isConnected("B", "A"));
        assertTrue(connectivity.isConnected("C", "D"));
        assertTrue(connectivity.isConnected("D", "C"));
    }

}