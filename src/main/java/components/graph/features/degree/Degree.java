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

package components.graph.features.degree;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Maps;
import components.graph.Graph;
import components.graph.GraphBuilder;
import components.graph.GraphInterface;

// TODO caching
// TODO incoming degree
// TODO outcoming degree
final class Degree<T> implements DegreeInterface<T> {

    private final GraphInterface<T> graph;

    public GraphInterface<T> getGraph() {
        return this.graph;
    }

    public static <T> Degree<T> from(final GraphInterface<T> graph) {
        return new Degree<T>(graph);
    }

    private Degree(final GraphInterface<T> graph) {
        this.graph = graph;
    }

    private volatile Map<T, Integer> data = null;

    private static <T> Map<T, Integer> computeData(final GraphInterface<T> graph) {
        final Builder<T, Integer> builder = new ImmutableMap.Builder<T, Integer>();
        for (final T node : graph)
            builder.put(node, graph.getConnectedEndPoints(node).size());
        return builder.build();
    }

    private Map<T, Integer> getData() {
        Map<T, Integer> value = this.data;
        if (value == null) {
            synchronized (this) {
                if ((value = this.data) == null) this.data = value = Degree.computeData(this.graph);
            }
        }
        return value;
    }

    @Override
    public Map<T, Integer> getDegreeByNode() {
        return this.getData();
    }

    @Override
    public Map<T, Integer> getNodesWithOddDegree() {
        return Maps.filterValues(this.getData(), Integers.Predicates.isOdd());
    }

    @Override
    public Map<T, Integer> getNodesWithEvenDegree() {
        return Maps.filterValues(this.getData(), Integers.Predicates.isEven());
    }

    @Override
    public Map<T, Integer> getNodesHavingDegree(final int degree) {
        Preconditions.checkArgument(degree > 0);
        return Maps.filterValues(this.getData(), Integers.Predicates.is(degree));
    }

    @Override
    public Map<T, Integer> getNodesNotHavingDegree(final int degree) {
        return Maps.filterValues(this.getData(), Integers.Predicates.isNot(degree));
    }

    // TODO : hasClosedEulerianPath / hasOpenEulerianPath
    @Override
    public boolean isEulerian() {
        final Map<T, Integer> nodesWithOddDegree = this.getNodesWithOddDegree();
        return nodesWithOddDegree.isEmpty(); // || nodesWithOddDegree.size() == 2;
    }

    public static void main(final String[] args) {

        final Graph<String> graph = new GraphBuilder<String>(4)
                .addEdge("A", "B", 1.0)
                .addEdge("B", "C", 1.0)
                .addEdge("C", "A", 1.0)
                .addEdge("A", "D", 1.0)
                .build();

        final DegreeInterface<String> degreeInterface = Degree.from(graph);

        final Map<String, Integer> data = degreeInterface.getDegreeByNode();
        for (final Entry<String, Integer> entry : data.entrySet())
            System.out.println(entry);

        System.out.println();

        final Map<String, Integer> nodesWithEvenDegree = degreeInterface.getNodesWithEvenDegree();
        for (final Entry<String, Integer> entry : nodesWithEvenDegree.entrySet())
            System.out.println(entry);

        System.out.println();

        final Map<String, Integer> nodesWithOddDegree = degreeInterface.getNodesWithOddDegree();
        for (final Entry<String, Integer> entry : nodesWithOddDegree.entrySet())
            System.out.println(entry);

        System.out.println();

        final Map<String, Integer> nodesOfDegree1 = degreeInterface.getNodesHavingDegree(1);
        for (final Entry<String, Integer> entry : nodesOfDegree1.entrySet())
            System.out.println(entry);

    }
}