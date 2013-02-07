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

package blockplus.arbitration;

import interfaces.arbitration.RefereeInterface;
import interfaces.context.ContextInterface;
import interfaces.move.MoveInterface;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import blockplus.board.Board;
import blockplus.board.BoardLayer;
import blockplus.board.State;
import blockplus.context.Color;
import blockplus.context.Context;
import blockplus.move.Move;
import blockplus.move.MoveComparator;
import blockplus.piece.NullPieceComponent;
import blockplus.piece.PieceData;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.neighbourhood.Neighbourhood;
import components.position.PositionInterface;

public final class Referee implements RefereeInterface {

    private static final MoveComparator MOVE_COMPARATOR = MoveComparator.getInstance();

    private List<Move> getLegalMoves(final Board board, final Color color, final Pieces piece, final PositionInterface position) {
        final List<Move> legalMoves = Lists.newArrayList();
        for (final PieceInterface pieceInstance : piece) {
            final PieceInterface translatedPieceInstance = pieceInstance.translateTo(position);
            final Move move = new Move(color, translatedPieceInstance);
            if (board.isLegal(move)) legalMoves.add(move);
        }
        return legalMoves;
    }

    // TODO tester pas à pas => PieceInstanceMatcher
    private Iterable<PositionInterface> getPotentialPositions(final Board board, final Color c, final Iterable<PositionInterface> p,
            final Pieces piece) {

        final int radius = PieceData.PieceData(piece.ordinal()).radius(); // TODO !! Pieces.radius()

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

    private Set<Move> getLegalMoves(final Board board, final Player player) {
        final Color color = player.getColor();
        final Map<PositionInterface, State> stillAlivePositionsByPriority = board.getLayer(color).getLights();
        final Iterable<PositionInterface> positionsHavingPotential = stillAlivePositionsByPriority.keySet();
        final Set<Move> legalMoves = Sets.newHashSet();
        final PiecesBag pieces = player.getPieces();
        for (final Pieces piece : pieces)
            for (final PositionInterface potentialPosition : this.getPotentialPositions(board, color, positionsHavingPotential, piece))
                legalMoves.addAll(this.getLegalMoves(board, color, piece, potentialPosition));
        if (legalMoves.isEmpty()) return ImmutableSet.of(new Move(player.getColor(), NullPieceComponent.getInstance())); // TODO à revoir
        return legalMoves;
    }

    @Override
    public List<MoveInterface> getLegalMoves(final ContextInterface<?> contextInterface, final Comparator<MoveInterface> comparator) {
        final Context context = (Context) contextInterface;
        final List<MoveInterface> moves = Lists.newArrayList();
        moves.addAll(this.getLegalMoves(context.getBoard(), context.getPlayer()));
        Collections.sort(moves, comparator);
        return moves;
    }

    @Override
    public List<MoveInterface> getLegalMoves(final ContextInterface<?> contextInterface) {
        return this.getLegalMoves(contextInterface, MOVE_COMPARATOR);
    }

}