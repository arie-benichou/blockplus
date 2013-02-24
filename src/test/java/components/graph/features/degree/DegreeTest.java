
package components.graph.features.degree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Maps;

import components.graph.Graph;
import components.graph.GraphBuilder;
import components.graph.features.degree.Degree;

// TODO ! tester sur un graphe orienté
// TODO ! tester sur un graphe mixte
public class DegreeTest {

    @Test
    public void testGetGraph() {
        final Graph<String> graph = new GraphBuilder<String>(2).addEdge("A", "B").build();
        final Degree<String> degree = Degree.from(graph);
        assertEquals(graph, degree.getGraph());
    }

    @Test
    public void testGetDegreeByNode() {
        final Graph<String> graph = new GraphBuilder<String>(2).addEdge("A", "B").build();
        final Degree<String> degree = Degree.from(graph);
        final Map<String, Integer> expected = Maps.newHashMap();
        expected.put("A", 1);
        expected.put("B", 1);
        assertEquals(expected, degree.getDegreeByNode());
    }

    @Test
    public void testGetNodesWithOddDegree() {
        final Graph<String> graph = new GraphBuilder<String>(3)
                .addEdge("A", "B")
                .addEdge("A", "C")
                .build();
        final Degree<String> degree = Degree.from(graph);
        final Map<String, Integer> expected = Maps.newHashMap();
        expected.put("B", 1);
        expected.put("C", 1);
        assertEquals(expected, degree.getNodesWithOddDegree());
    }

    @Test
    public void testGetNodesWithEvenDegree() {
        final Graph<String> graph = new GraphBuilder<String>(3)
                .addEdge("A", "B")
                .addEdge("A", "C")
                .build();
        final Degree<String> degree = Degree.from(graph);
        final Map<String, Integer> expected = Maps.newHashMap();
        expected.put("A", 2);
        assertEquals(expected, degree.getNodesWithEvenDegree());
    }

    @Test
    public void testGetNodesHavingDegree() {
        final Graph<String> graph = new GraphBuilder<String>(3)
                .addEdge("A", "B")
                .addEdge("A", "C")
                .build();
        final Degree<String> degree = Degree.from(graph);
        final Map<String, Integer> expected1 = Maps.newHashMap();
        expected1.put("B", 1);
        expected1.put("C", 1);
        assertEquals(expected1, degree.getNodesHavingDegree(1));
        final Map<String, Integer> expected2 = Maps.newHashMap();
        expected2.put("A", 2);
        assertEquals(expected2, degree.getNodesHavingDegree(2));
    }

    @Test
    public void testIsEulerian1() {
        final GraphBuilder<String> graphBuilder = new GraphBuilder<String>(3)
                .addEdge("A", "B")
                .addEdge("B", "C");
        assertFalse(Degree.from(graphBuilder.build()).isEulerian());
        assertTrue(Degree.from(graphBuilder.addEdge("C", "A").build()).isEulerian());
    }

    @Ignore
    // TODO
    @Test
    public void testIsEulerian2() {
        final Graph<String> graph = new GraphBuilder<String>(2).addEdge("A", "B").build();
        final Degree<String> degree = Degree.from(graph);
        assertTrue(degree.isEulerian());
    }

}