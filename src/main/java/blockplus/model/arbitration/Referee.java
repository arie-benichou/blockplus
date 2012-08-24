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

package blockplus.model.arbitration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;
import blockplus.model.color.ColorInterface;
import blockplus.model.move.Move;
import blockplus.model.piece.Piece;
import blockplus.model.piece.PieceInstances;
import blockplus.model.piece.PieceInstancesFactory;
import blockplus.model.piece.PieceInterface;
import blockplus.model.piece.Pieces;
import blockplus.model.player.PlayerInterface;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.neighbourhood.Neighbourhood;
import components.position.PositionInterface;

public class Referee {

    private List<Move> getLegalMoves(final Board board, final ColorInterface color, final PieceInterface piece, final PositionInterface position) {
        final List<Move> legalMoves = Lists.newArrayList();
        final PieceInstances pieceInstances = PieceInstancesFactory.get(piece.getId());
        for (final PieceInterface pieceInstance : pieceInstances) {
            final PieceInterface translatedPieceInstance = pieceInstance.translateTo(position);
            if (board.isLegal(color, translatedPieceInstance)) legalMoves.add(new Move(color, translatedPieceInstance));
        }
        return legalMoves;
    }

    private Iterable<PositionInterface> getPotentialPositions(final Board board, final ColorInterface c, final Iterable<PositionInterface> p,
            final PieceInterface pc) {

        final int radius = ((Piece) pc).getPieceData().radius(); // TODO !! à revoir

        final BoardLayer layer = board.getLayer(c);
        final Set<PositionInterface> extendedLegalPositions = Sets.newLinkedHashSet();
        final Map<PositionInterface, Set<PositionInterface>> map = Maps.newLinkedHashMap();
        for (final PositionInterface positionHavingPotential : p) {
            final Set<PositionInterface> extendedPositions = Sets.newLinkedHashSet();
            for (int k = 0; k <= radius; ++k) {
                for (final PositionInterface neighbour : Neighbourhood.getNeighboursPositions(positionHavingPotential, k)) {
                    if (!extendedLegalPositions.contains(neighbour) && layer.isMutable(neighbour)) {
                        extendedLegalPositions.add(neighbour);
                        extendedPositions.add(neighbour);
                    }
                }
            }
            map.put(positionHavingPotential, extendedPositions);
        }
        return Iterables.concat(map.values());
    }

    // TODO !! passer le contexte à l'arbitre
    // TODO ! pouvoir passer un Ordering/Comparator de Move
    public Set<Move> getLegalMoves(final Board board, final PlayerInterface player) {
        final ColorInterface color = player.getColor();
        final Map<PositionInterface, State> stillAlivePositionsByPriority = board.getLayer(color).getLights();
        final Iterable<PositionInterface> positionsHavingPotential = stillAlivePositionsByPriority.keySet();
        final Set<Move> legalMoves = Sets.newHashSet();
        for (final PieceInterface piece : player.getPieces())
            for (final PositionInterface potentialPosition : this.getPotentialPositions(board, color, positionsHavingPotential, piece))
                legalMoves.addAll(this.getLegalMoves(board, color, piece, potentialPosition));

        // TODO ! enlever cette responsabilité de l'arbitre:
        // autoriser le coup nul pour un jeu en ajoutant explicitement la piece nulle au set de pièces légales du jeu 
        if (legalMoves.isEmpty()) return ImmutableSet.of(new Move(player.getColor(), Pieces.get(0))); // TODO à revoir

        return legalMoves;
    }

    public List<Move> getOrderedLegalMoves(final Board board, final PlayerInterface player) {
        final List<Move> moves = Lists.newArrayList(this.getLegalMoves(board, player));
        Collections.sort(moves);
        return moves;
    }
}