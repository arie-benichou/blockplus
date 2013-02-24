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

package components.graph.edge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

public class WeightedEdgeTest {

    @Test
    public void testGetEndPoint1() {
        final WeightedEdge<String> edge = WeightedEdge.from("A", "B", 1.0);
        assertTrue(edge.getEndPoint1().equals("A"));
    }

    @Test
    public void testGetEndPoint2() {
        final WeightedEdge<String> edge = WeightedEdge.from("A", "B", 1.0);
        assertTrue(edge.getEndPoint2().equals("B"));
    }

    @Test
    public void testGetWeight() {
        final WeightedEdge<String> edge = WeightedEdge.from("A", "B", 1.0);
        assertEquals(1.0, edge.getWeight(), 0.1);
    }

    @Test
    public void testEqualsObject() {
        final WeightedEdge<String> edge1 = WeightedEdge.from("A", "C", 2.0);
        final WeightedEdge<String> edge2 = WeightedEdge.from("A", "D", 2.0);
        final WeightedEdge<String> edge3 = WeightedEdge.from("C", "A", 2.0);

        assertFalse(edge1.equals(null));
        assertTrue(edge1.equals(edge1) == true);
        assertFalse(edge1.equals(new Object()));

        assertFalse(edge1.equals(edge2));
        assertFalse(edge2.equals(edge1));

        assertTrue(edge1.equals(edge3));
        assertTrue(edge3.equals(edge1));

        assertTrue(edge1.equals(WeightedEdge.from("A", "C", 2.0)) == true);
        assertTrue(edge1.equals(WeightedEdge.from("C", "A", 2.0)) == true);

        assertFalse(edge1.equals(WeightedEdge.from("B", "C", 2.0)));
        assertFalse(edge1.equals(WeightedEdge.from("A", "B", 2.0)));

        assertFalse(edge1.equals(WeightedEdge.from("A", "C", 1.0)));

        final WeightedEdge<Integer> newEdge1 = WeightedEdge.from(1, 2, 1.0);
        final WeightedEdge<Double> newEdge2 = WeightedEdge.from(1.0, 2.0, 1.0);
        assertFalse(newEdge1.equals(newEdge2));
    }

    @Test
    public void testNewEdgeFromOneEndpointToSameEndpoint() {
        WeightedEdge.from("A", "A", 1.0);
    }

    //@Test
    public void testHashCode() {
        /*
        final WeightedEdge<String> edge1 = WeightedEdge.from("A", "C", 2.0);
        final WeightedEdge<String> edge2 = WeightedEdge.from("A", "D", 2.0);
        final WeightedEdge<String> edge3 = edge1.reverse();
        assertNotSame(edge1.hashCode(), edge2.hashCode());
        assertEquals(edge1.hashCode(), WeightedEdge.from("A", "C", 2.0).hashCode());
        assertEquals(edge1.hashCode(), WeightedEdge.from("C", "A", 2.0).hashCode());
        assertEquals(edge1.hashCode(), edge3.hashCode());
        */
        fail();
    }

}