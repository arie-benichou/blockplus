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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import components.graph.Graph;
import components.graph.GraphBuilder;
import components.graph.arc.ArcInterface;
import components.graph.arc.Arcs;


public class GraphBuilderTest {

    @Test
    public void testlegalBuilder() {
        new GraphBuilder<String>(2);
    }

    @Test
    public void testGetOrder() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        assertEquals(2, builder.getOrder());
    }

    @Test
    public void testAddEdgeTTDouble4() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(3);
        assertEquals(3, builder.getOrder());
        assertEquals(builder, builder.addEdge("A", "B", 1.0));
        assertEquals(builder, builder.addEdge("A", "C", 1.0));
    }

    @Ignore
    @Test
    public void testContains() {
        final Arcs<String> arcFactory = new Arcs<String>();
        final GraphBuilder<String> builder = new GraphBuilder<String>(2, arcFactory);

        final ArcInterface<String> arc1 = arcFactory.get("A", "B");

        assertFalse(builder.contains(arcFactory.get("C", "D")));

        assertFalse(builder.contains(arc1));
        assertEquals(builder, builder.addArc("A", "B"));
        assertTrue(builder.contains(arc1));

        assertFalse(builder.contains(arcFactory.get("A", "C")));
        assertFalse(builder.contains(arcFactory.get("C", "B")));

        final ArcInterface<String> arc2 = arcFactory.get("B", "A");
        assertFalse(builder.contains(arc2));
        assertEquals(builder, builder.addArc("B", "A"));
        assertTrue(builder.contains(arc2));

        assertTrue(builder.contains(arc1));

        assertFalse(builder.contains(arcFactory.get("C", "A")));
        assertFalse(builder.contains(arcFactory.get("B", "C")));

    }

    @Test
    public void testLegalBuild() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        assertEquals(builder, builder.addEdge("A", "B", 1.0));
        final Graph<String> build = builder.build();
        assertNotNull(build);
        assertTrue(build instanceof Graph);
    }

}