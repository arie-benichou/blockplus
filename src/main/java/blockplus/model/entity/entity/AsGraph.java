/*
 * Copyright 2012-2013 Arie Benichou
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

package blockplus.model.entity.entity;


import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import blockplus.model.entity.entity.Node.Type;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;
import components.cells.Directions.Direction;
import components.cells.Positions.Position;
import components.graph.EmptyGraph;
import components.graph.GraphBuilder;
import components.graph.GraphInterface;
import components.graph.SingletonGraph;
import components.graph.arc.Arcs;
import components.graph.arc.WeightedArcInterface;

public final class AsGraph implements Supplier<GraphInterface<Node>> {

    public static AsGraph from(final AsCells board) {
        return new AsGraph(board);
    }

    private final static Arcs<Node> arcs = new Arcs<Node>();

    private final GraphInterface<Node> graph;
    private final AsCells cells;

    @Override
    public GraphInterface<Node> get() {
        return this.graph;
    }

    private Map<Position, Node> buildNodes(final AsCells cells) {
        final Builder<Position, Node> builder = new ImmutableSortedMap.Builder<Position, Node>(Ordering.natural());
        for (final Position position : cells.getCells().get().keySet())
            builder.put(position, Node.from(position, ImmutableSet.copyOf(cells.getDirections(position))));
        return builder.build();
    }

    private Map<Position, List<WeightedArcInterface<Node>>> buildEdges(final Map<Position, Node> nodes) {
        final Builder<Position, List<WeightedArcInterface<Node>>> builder;
        builder = new ImmutableSortedMap.Builder<Position, List<WeightedArcInterface<Node>>>(Ordering.natural());
        for (final Node node : nodes.values()) {
            if (!node.is(Type.STREET)) {
                final ImmutableList.Builder<WeightedArcInterface<Node>> edgesBuilder = ImmutableList.builder();
                for (final Direction direction : node.getData()) {
                    double weight = 0;
                    Node currentNode = node;
                    do {
                        ++weight;
                        final Position id = currentNode.id();
                        final Position position = this.cells.getCells().position(id.row() + direction.rowDelta(), id.column() + direction.columnDelta()); // TODO à revoir
                        currentNode = nodes.get(position);
                    } while (currentNode.is(Type.STREET));
                    edgesBuilder.add(arcs.get(node, currentNode, weight));
                    edgesBuilder.add(arcs.get(currentNode, node, weight));
                }
                builder.put(node.id(), edgesBuilder.build());
            }
        }
        return builder.build();
    }

    private GraphInterface<Node> buildGraph(final Map<Position, List<WeightedArcInterface<Node>>> edgesByNode) {
        final GraphBuilder<Node> graphBuilder = new GraphBuilder<Node>(edgesByNode.size(), arcs);
        for (final Entry<Position, List<WeightedArcInterface<Node>>> entry : edgesByNode.entrySet())
            for (final WeightedArcInterface<Node> arc : entry.getValue())
                if (!graphBuilder.contains(arc.get())) graphBuilder.addArc(arc.get());
        return graphBuilder.build();
    }

    private AsGraph(final AsCells cells) {
        this.cells = cells;
        final Map<Position, Node> nodes = this.buildNodes(this.cells);
        final int order = nodes.size();
        if (order == 0) {
            this.graph = new EmptyGraph<Node>();
        }
        else if (order == 1) {
            this.graph = new SingletonGraph<Node>(nodes.values().iterator().next());
        }
        else {
            this.graph = this.buildGraph(this.buildEdges(nodes));
        }
    }

}