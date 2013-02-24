/*
 * Copyright 2012 Arie Benichou
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */

package components.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Sets;
import components.graph.edge.WeightedEdge;

// TODO !!!
public class UndirectedGraphTest {

    private Graph<String> graph;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {}

    @AfterClass
    public static void tearDownAfterClass() throws Exception {}

    @Before
    public void setUp() throws Exception {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        builder.addEdge("A", "B", 1.0);
        this.graph = builder.build();
    }

    @After
    public void tearDown() throws Exception {
        this.graph = null;
    }

    @Test
    public void testGetOrder() {
        final int order = this.graph.getOrder();
        assertEquals(2, order);
    }

    @Ignore
    public void testLegalContainsEdge() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("A", "C", 1.0);

        //final Graph<String> graph = builder.build();

        /*
        assertFalse(graph.hasEdge("C", "B"));
        assertTrue(graph.hasEdge("A", "B"));

        assertFalse(graph.hasEdge("B", "C"));
        assertTrue(graph.hasEdge("B", "A"));
        */
        fail();
    }

    @Ignore
    public void testLegalGetConnectedVerticeSet() {
        assertFalse(this.graph.getConnectedEndPoints("A").equals(Sets.newHashSet("C")));
        assertTrue(this.graph.getConnectedEndPoints("A").equals(Sets.newHashSet("B")));
        assertFalse(this.graph.getConnectedEndPoints("B").equals(Sets.newHashSet("C")));
        assertTrue(this.graph.getConnectedEndPoints("B").equals(Sets.newHashSet("A")));
    }

    @Test
    public void testIterator() {
        final Set<String> expectedSet = Sets.newHashSet("A", "B");
        final Set<String> actualSet = Sets.newHashSet();
        int i = 0;
        for (final String MazeNode : this.graph) {
            ++i;
            actualSet.add(MazeNode);
        }
        assertEquals(expectedSet.size(), i);
        assertTrue(actualSet.equals(expectedSet));
    }

    @Ignore
    public void testLegalGetEdge1() {
        //final WeightedEdge<String> expectedEdge = WeightedEdge.from("A", "B", 1.0);
        fail();
        //final WeightedEdge<String> actualEdge = this.graph.getEdge("A", "B");
        //assertTrue(actualEdge.equals(expectedEdge));
    }

    @Ignore
    public void testLegalGetEdge2() {
        //final WeightedEdge<String> expectedEdge = WeightedEdge.from("B", "A", 1.0);
        fail();
        //final WeightedEdge<String> actualEdge = this.graph.getEdge("A", "B");
        //assertTrue(actualEdge.equals(expectedEdge));
    }

    @Ignore
    public void testLegalGetEdges1() {
        final Set<WeightedEdge<String>> expectedEdges = Sets.newHashSet();
        expectedEdges.add(WeightedEdge.from("A", "B", 1.0));
        fail();
        //final Set<WeightedEdge<String>> actualEdges = this.graph.getEdgesFrom("A");
        //assertTrue(actualEdges.equals(expectedEdges));
    }

    @Ignore
    public void testLegalGetEdges2() {
        final Set<WeightedEdge<String>> expectedEdges = Sets.newHashSet();
        expectedEdges.add(WeightedEdge.from("B", "A", 1.0));
        fail();
        //final Set<WeightedEdge<String>> actualEdges = this.graph.getEdgesFrom("A");
        //assertTrue(actualEdges.equals(expectedEdges));
    }

    @Test
    public void testContainsT() {
        assertFalse(this.graph.hasEndPoint("C"));
        assertTrue(this.graph.hasEndPoint("A"));
        assertTrue(this.graph.hasEndPoint("B"));
        assertFalse(this.graph.hasEndPoint(""));
    }

    /*
    @Test(expected = IllegalStateException.class)
    public void testGetFeature() {
        this.graph.getFeature(Feature.NONE);
    }

    @Test
    public void testGetDegreeFeature() {
        final Object feature = this.graph.fetch(Feature.DEGREE);
        assertTrue(feature.getClass().equals(Feature.DEGREE.getFeatureClass()));
    }
    */

    /*
    @Test
    public void testIsConnected1() {
        final Builder<String> builder = new UndirectedGraphBuilder<String>(4);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("C", "D", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertFalse(graph.isConnected());
        assertFalse(graph.isConnected());
    }

    @Test
    public void testIsConnected2() {
        final Builder<String> builder = new UndirectedGraphBuilder<String>(2);
        builder.addEdge("A", "B", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isConnected());
        assertTrue(graph.isConnected());
    }
    
    @Test
    public void testIsEulerian1() {
        final Builder<String> builder = new UndirectedGraphBuilder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("C", "A", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isEulerian());
        assertTrue(graph.isEulerian());
    }

    @Test
    public void testIsNotEulerian2() {
        final Builder<String> builder = new UndirectedGraphBuilder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertFalse(graph.isEulerian());
        assertFalse(graph.isEulerian());
    }

    @Test
    public void testGetShortestPathBetween1() {
        final Path<String> expectedPath = Path.from(this.graph.getEdge("A", "B"));
        assertTrue(this.graph.isConnected());
        final Path<String> actualPath = this.graph.getShortestPathBetween("A", "B");
        assertTrue(actualPath.equals(expectedPath));
    }

    @Test
    public void testGetShortestPathBetween2() {
        final Builder<String> builder = new UndirectedGraphBuilder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("A", "C", 1.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isConnected());
        final Path<String> expectedPath = Path.from(graph.getEdge("A", "C"));
        final Path<String> actualPath = graph.getShortestPathBetween("A", "C");
        assertTrue(actualPath.equals(expectedPath));
    }

    @Test
    public void testGetShortestPathBetween3() {
        final Builder<String> builder = new UndirectedGraphBuilder<String>(3);
        builder.addEdge("A", "B", 1.0);
        builder.addEdge("B", "C", 1.0);
        builder.addEdge("A", "C", 3.0);
        final UndirectedGraph<String> graph = builder.build();
        assertTrue(graph.isConnected());
        final Path<String> expectedPath = Path.from(graph.getEdge("A", "B")).add(Path.from(graph.getEdge("B", "C")));
        final Path<String> actualPath = graph.getShortestPathBetween("A", "C");
        assertTrue(actualPath.equals(expectedPath));
    }
    */

    /*
    @Test
    public void testGetShortestPathBetween4() {
        final Path<String> expectedPath = new Path.Factory<String>().newPath("A", "B", 999.999);
        assertTrue(this.graph.isConnected());
        final Path<String> actualPath = this.graph.getShortestPathBetween("A", "B");
        assertTrue(actualPath.equals(expectedPath) == false);// TODO SHOULD be false : revoir Equals de Path
    }
    */

}