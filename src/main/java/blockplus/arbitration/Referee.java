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
import java.util.Map.Entry;
import java.util.Set;

import blockplus.Color;
import blockplus.board.Board;
import blockplus.board.Layer;
import blockplus.context.Context;
import blockplus.move.Move;
import blockplus.move.MoveComparator;
import blockplus.move.Moves;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceType;
import blockplus.piece.Pieces;
import blockplus.player.Player;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import components.neighbourhood.Neighbourhood;
import components.position.PositionInterface;

// TODO ? create class Options in this package
public final class Referee implements RefereeInterface {

    private final static MoveComparator MOVE_COMPARATOR = MoveComparator.getInstance();

    private List<Move> getLegalMoves(final Board board, final Color color, final PieceType piece, final PositionInterface position) {
        final List<Move> legalMoves = Lists.newArrayList();
        for (final PieceInterface pieceInstance : piece) {
            final PieceInterface translatedPieceInstance = pieceInstance.translateTo(position);
            final Move move = Moves.getMove(color, translatedPieceInstance);
            if (board.isLegal(move)) legalMoves.add(move);
        }
        return legalMoves;
    }

    // TODO tester pas à pas => PieceInstanceMatcher
    private Iterable<PositionInterface> getPotentialPositions(final Board board, final Color c, final Iterable<PositionInterface> p, final PieceType pieceType) {
        final int radius = pieceType.radius();
        final Layer layer = board.getLayer(c);
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
        final Iterable<PositionInterface> positionsHavingPotential = board.getLayer(color).getLights().keySet();
        final Set<Move> legalMoves = Sets.newHashSet();
        final Pieces pieces = player.getPieces();
        for (final Entry<PieceType, Integer> entry : pieces) {
            if (entry.getValue() > 0) {
                final PieceType piece = entry.getKey();
                for (final PositionInterface potentialPosition : this.getPotentialPositions(board, color, positionsHavingPotential, piece)) {
                    legalMoves.addAll(this.getLegalMoves(board, color, piece, potentialPosition));
                }
            }
        }
        // TODO null move should be legal if in bag without using special case
        if (legalMoves.isEmpty()) return ImmutableSet.of(Moves.getNullMove(player.getColor()));

        return legalMoves;
    }

    // TODO delegate ordering to the future object Options
    // Options intentions: Referee should return list of option, not move ?
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

    @Override
    public boolean isLegal(final ContextInterface<?> contextInterface, final MoveInterface moveInterface) {
        return contextInterface.getBoard().isLegal(moveInterface);
    }

}