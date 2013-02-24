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

package blockplus.piece2.matching;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import blockplus.piece2.matching.PieceNode.Type;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.ImmutableSortedMap.Builder;
import com.google.common.collect.Ordering;
import components.direction.DirectionInterface;
import components.graph.EmptyGraph;
import components.graph.Graph;
import components.graph.GraphBuilder;
import components.graph.GraphInterface;
import components.graph.SingletonGraph;
import components.graph.arc.Arcs;
import components.graph.arc.WeightedArcInterface;
import components.position.PositionInterface;

public final class PieceAsGraph implements Supplier<GraphInterface<PieceNode>> {

    private final static Arcs<PieceNode> ARC_FACTORY = new Arcs<PieceNode>();

    private static Map<PositionInterface, PieceNode> buildNodes(final PieceAsCells board) {
        final Builder<PositionInterface, PieceNode> builder = new ImmutableSortedMap.Builder<PositionInterface, PieceNode>(Ordering.natural());
        for (final PositionInterface position : board.getCells().mutations().keySet())
            builder.put(position, PieceNode.from(position, ImmutableSet.copyOf(board.getDirections(position))));
        return builder.build();
    }

    private static Map<PositionInterface, List<WeightedArcInterface<PieceNode>>> buildEdges(final Map<PositionInterface, PieceNode> nodes) {
        final Builder<PositionInterface, List<WeightedArcInterface<PieceNode>>> builder;
        builder = new ImmutableSortedMap.Builder<PositionInterface, List<WeightedArcInterface<PieceNode>>>(Ordering.natural());
        for (final PieceNode node : nodes.values()) {
            if (!node.is(Type.STREET)) {
                final ImmutableList.Builder<WeightedArcInterface<PieceNode>> edgesBuilder = ImmutableList.builder();
                for (final DirectionInterface direction : node.getData()) {
                    double weight = 0;
                    PieceNode currentNode = node;
                    do {
                        ++weight;
                        currentNode = nodes.get(currentNode.getId().apply(direction));
                    } while (currentNode.is(Type.STREET));
                    edgesBuilder.add(ARC_FACTORY.get(node, currentNode, weight));
                    edgesBuilder.add(ARC_FACTORY.get(currentNode, node, weight));
                }
                builder.put(node.getId(), edgesBuilder.build());
            }
        }
        return builder.build();
    }

    private static Graph<PieceNode> buildGraph(final Map<PositionInterface, List<WeightedArcInterface<PieceNode>>> edgesByNode) {
        final GraphBuilder<PieceNode> graphBuilder = new GraphBuilder<PieceNode>(edgesByNode.size(), ARC_FACTORY);
        for (final Entry<PositionInterface, List<WeightedArcInterface<PieceNode>>> entry : edgesByNode.entrySet())
            for (final WeightedArcInterface<PieceNode> arc : entry.getValue())
                if (!graphBuilder.contains(arc.get())) graphBuilder.addArc(arc.get());
        return graphBuilder.build();
    }

    public static PieceAsGraph from(final PieceAsCells board) {
        return new PieceAsGraph(board);
    }

    private final GraphInterface<PieceNode> graph;

    @Override
    public GraphInterface<PieceNode> get() {
        return this.graph;
    }

    private PieceAsGraph(final PieceAsCells board) {
        final Map<PositionInterface, PieceNode> nodes = PieceAsGraph.buildNodes(board);
        final int order = nodes.size();
        if (order == 0) {
            this.graph = new EmptyGraph<PieceNode>();
        }
        else if (order == 1) {
            this.graph = new SingletonGraph<PieceNode>(nodes.values().iterator().next());
        }
        else {
            this.graph = PieceAsGraph.buildGraph(PieceAsGraph.buildEdges(nodes));
        }
    }

    /*
    public boolean hasIsland() {
        final ConnectivityInterface<PieceNode> connectivityFeature = this.graph.fetch(ConnectivityFeature.class).up();
        return !connectivityFeature.isConnected();
    }
    */

}