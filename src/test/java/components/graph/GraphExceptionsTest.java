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

import java.util.NoSuchElementException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GraphExceptionsTest {

    private Graph<String> graph;

    @Before
    public void setUp() throws Exception {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        builder.addArc("A", "B", 1.0);
        this.graph = builder.build();
    }

    @After
    public void tearDown() throws Exception {
        this.graph = null;
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalContainsEdge1() {
        this.graph.hasArc("A", "C");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalContainsEdge2() {
        this.graph.hasArc("C", "A");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetConnectedVerticeSet() {
        this.graph.getConnectedEndPoints("C");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetEdge1() {
        this.graph.getArc("C", "A");
    }

    @Test(expected = NoSuchElementException.class)
    public void testIllegalGetEdge2() {
        this.graph.getArc("A", "C");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalContainsT() {
        this.graph.hasEndPoint(null);
    }

}