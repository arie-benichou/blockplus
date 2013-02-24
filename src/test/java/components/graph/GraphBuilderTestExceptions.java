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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import components.graph.GraphBuilder;

public class GraphBuilderTestExceptions {

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalBuilder1() {
        new GraphBuilder<String>(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalBuilder2() {
        new GraphBuilder<String>(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalBuilder3() {
        new GraphBuilder<String>(1);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddArcTT1() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        builder.addArc("1", "2");
        builder.addArc("3", "2");
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddArcTT2() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        builder.addArc("1", "2");
        builder.addArc("1", "3");
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddArcTT3() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        builder.addArc("A", "B");
        builder.addArc("A", "B");
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalAddEdgeTT4() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        builder.addEdge("A", "B");
        builder.addEdge("B", "A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddArcWithNullReferenceOfArcOfT() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        builder.addArc(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalBuild1() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(2);
        builder.build();
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalBuild2() {
        final GraphBuilder<String> builder = new GraphBuilder<String>(3);
        assertTrue(builder.addArc("A", "B") == builder);
        builder.build();
    }

}