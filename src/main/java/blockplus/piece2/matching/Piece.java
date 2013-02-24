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
import java.util.Set;

import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;
import blockplus.piece2.matching.PieceTiles.PieceTile;

import com.google.common.collect.Lists;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import components.cells.CellsInterface;
import components.graph.GraphInterface;
import components.graph.arc.ArcInterface;
import components.graph.arc.WeightedArc;
import components.neighbourhood.Neighbourhood;
import components.position.Position;
import components.position.PositionInterface;

public final class Piece {

    private final PieceAsGraph graph;
    private final PieceAsCells cells;

    public static Piece from(final String[] data) {
        return new Piece(data);
    }

    private Piece(final String[] data) {
        this.cells = PieceAsCells.from(data);
        this.graph = PieceAsGraph.from(this.cells);
    }

    private PieceAsGraph getGraph() {
        return this.graph;
    }

    private PieceAsCells getCells() {
        return this.cells;
    }

    // TODO compute from agregated data in PieceNode (data: sides, corners)
    private Set<PositionInterface> shadows() {
        final PieceInterface truc = PieceComposite.from(1, Position.Position(), this.getCells().getCells().mutations().keySet());
        return truc.getShadowPositions();
    }

    // TODO compute from agregated data in PieceNode (data: sides, corners)
    private Set<PositionInterface> lights() {
        final PieceInterface truc = PieceComposite.from(1, Position.Position(), this.getCells().getCells().mutations().keySet());
        return truc.getLightPositions();
    }

    // TODO compute from agregated data in PieceNode (data: sides, corners)
    private int checkSum(final PieceNode node) {
        final CellsInterface<PieceTile> cells = this.getCells().getCells();
        int checkSum = 0;
        for (final PositionInterface neighbour : Neighbourhood.getNeighboursPositions(node.getId(), 1))
            if (cells.get(neighbour).is(PieceTile.SOME)) ++checkSum;
        return checkSum;
    }

    public Multiset<String> footPrint() {
        final TreeMultiset<String> footPrint = TreeMultiset.create();
        final GraphInterface<PieceNode> graph = this.getGraph().get();
        for (final PieceNode endpoint : graph) {
            final List<ArcInterface<PieceNode>> arcs = graph.getArcsFrom(endpoint);
            for (final ArcInterface<PieceNode> arc : arcs) {
                final WeightedArc<PieceNode> weightedArc = (WeightedArc<PieceNode>) arc;
                final int weight = weightedArc.getWeight().intValue();
                final PieceNode endPoint1 = weightedArc.getEndPoint1();
                final int checkSum1 = this.checkSum(endPoint1);
                final PieceNode endPoint2 = weightedArc.getEndPoint2();
                final int checkSum2 = this.checkSum(endPoint2);
                final String element = "$" + weight + ":"
                        + endPoint1.getType()
                        + "(" + checkSum1 + ")"
                        + "->" + endPoint2.getType()
                        + "(" + checkSum2 + ")";
                footPrint.add(element);
            }
        }
        footPrint.add(Lists.newArrayList(this.shadows().size(), this.lights().size()).toString());
        return footPrint;
    }
}