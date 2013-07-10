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

import java.util.Collections;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.collect.Sets;

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

    @Test
    public void testLegalGetConnectedVerticeSet() {
        assertFalse(this.graph.getConnectedEndPoints("A").equals(Collections.singletonList("C")));
        assertTrue(this.graph.getConnectedEndPoints("A").equals(Collections.singletonList("B")));
        assertFalse(this.graph.getConnectedEndPoints("B").equals(Collections.singletonList("C")));
        assertTrue(this.graph.getConnectedEndPoints("B").equals(Collections.singletonList("A")));
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

    @Test
    public void testContainsT() {
        assertFalse(this.graph.hasEndPoint("C"));
        assertTrue(this.graph.hasEndPoint("A"));
        assertTrue(this.graph.hasEndPoint("B"));
        assertFalse(this.graph.hasEndPoint(""));
    }
}