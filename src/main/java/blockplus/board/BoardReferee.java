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

package blockplus.board;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.Color;
import blockplus.Move;
import blockplus.Player;
import blockplus.direction.DirectionInterface;
import blockplus.piece.PieceInterface;
import blockplus.position.PositionInterface;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

// TODO caching
public class BoardReferee {

    // TODO ? predicate
    private boolean hasEnoughSpaceForPiece(final Board board, final PieceInterface piece, final PositionInterface position) {
        for (final PositionInterface coordinates : piece.getPositions(position))
            if (!board.isEmpty(coordinates)) return false;
        return true;
    }

    // TODO ? predicate
    private boolean hasAtLeastOneCellWithSideNeighbourOfSameColor(final Board board, final Color color, final PieceInterface piece,
            final PositionInterface position) {
        for (final PositionInterface coordinates : piece.getPositions(position))
            for (final Integer value : board.getSides(coordinates).values())
                if (value == color.getValue()) return true;
        return false;
    }

    // TODO ? predicate
    private boolean hasAtLeastOneCellWithCornerNeighbourOfSameColor(final Board board, final Color color, final PieceInterface piece,
            final PositionInterface position) {
        for (final PositionInterface coordinates : piece.getPositions(position))
            for (final Integer value : board.getCornerNeighbours(coordinates).values())
                if (Math.abs(value) % color.getValue() == 0) return true;
        return false;
    }

    // TODO ? predicate
    private boolean isLegal(final Board board, final Color color, final PieceInterface piece, final PositionInterface position) {
        final boolean c1 = this.hasEnoughSpaceForPiece(board, piece, position);
        final boolean c2 = !this.hasAtLeastOneCellWithSideNeighbourOfSameColor(board, color, piece, position);
        final boolean c3 = this.hasAtLeastOneCellWithCornerNeighbourOfSameColor(board, color, piece, position);
        return c1 && c2 && c3;
    }

    private List<Move> getLegalMovesByPiece(final Board board, final Color color, final PieceInterface piece, final PositionInterface position,
            final DirectionInterface direction) {
        final List<Move> legalMovesByPiece = Lists.newArrayList();
        final PositionInterface newPosition = position.apply(direction);
        for (final PieceInterface rotation : piece.getRotations()) {
            if (this.isLegal(board, color, rotation, newPosition)) {
                final Board newBoard = board.put(color, rotation, newPosition);
                final Move move = new Move(color, position, direction, rotation, newBoard);
                legalMovesByPiece.add(move);
            }
        }
        return legalMovesByPiece;
    }

    private List<DirectionInterface> getPotentialDirections(final Map<DirectionInterface, Integer> neighbours) {
        final List<DirectionInterface> potentialDirections = Lists.newArrayList();
        for (final Entry<DirectionInterface, Integer> entry : neighbours.entrySet()) {
            final DirectionInterface direction = entry.getKey();
            final Integer value = entry.getValue();
            if (value == Board.EMPTY) potentialDirections.add(direction); // TODO ? Cell Object
        }
        return potentialDirections;
    }

    private Set<Move> getDistinctLegalMovesByPiece(final Board board, final Color color, final PieceInterface piece, final PositionInterface position) {
        final int distance = piece.getBoxingSquareSide() - 1;
        final Map<DirectionInterface, Integer> neighbours = board.getAllNeighbours(position, distance);
        final List<DirectionInterface> potentialDirections = this.getPotentialDirections(neighbours);
        final Set<Move> distinctLegalMovesByPiece = Sets.newHashSet();
        for (final DirectionInterface direction : potentialDirections)
            distinctLegalMovesByPiece.addAll(this.getLegalMovesByPiece(board, color, piece, position, direction));
        return distinctLegalMovesByPiece;
    }

    private List<Move> getLegalMovesByPosition(final Player player, final Board board, final PositionInterface position) {
        final List<Move> legalMovesByCell = Lists.newArrayList();
        for (final PieceInterface piece : player.getBagOfPieces())
            legalMovesByCell.addAll(this.getDistinctLegalMovesByPiece(board, player.getColor(), piece, position));
        return legalMovesByCell;
    }

    // TODO ! réfléchir à des heuristiques permettant d'augmenter le nombre de cut-off 
    // TODO ? utiliser un board de Color
    // TODO ? avoir un objet Neighbourhood (Side et Corners)
    private List<PositionInterface> getPotentialPositions(final Board board, final Color color, final List<PositionInterface> emptyCells) {
        final List<PositionInterface> potentialPositions = Lists.newArrayList();
        for (final PositionInterface position : emptyCells) {
            final Map<DirectionInterface, Integer> corners = board.getCorners(position);
            int cornersProduct = 1;
            for (final Integer n : corners.values()) {
                cornersProduct *= n;
            }
            if (Math.abs(cornersProduct) % color.getValue() != 0) continue; // Board.UNDEFINED = -1
            /*
            final Map<DirectionInterface, Integer> sides = board.getSides(position);
            int sidesProduct = 1;
            for (final Integer n : sides.values())
                sidesProduct *= n;
            if (Math.abs(sidesProduct) % color.getValue() == 0) continue;
            */
            potentialPositions.add(position);
        }
        return potentialPositions;
    }

    public Set<Move> getLegalMoves(final Board board, final Player player) {
        final Set<Move> legalMoves = Sets.newHashSet();
        final List<PositionInterface> potentialPositions = this.getPotentialPositions(board, player.getColor(), board.getEmptyCellPositions());
        for (final PositionInterface potentialPosition : potentialPositions)
            legalMoves.addAll(this.getLegalMovesByPosition(player, board, potentialPosition));
        return legalMoves;
    }

    // TODO pouvoir passer un Ordering/Comparator de Move
    public List<Move> getOrderedLegalMoves(final Board board, final Player player) {
        final List<Move> moves = Lists.newArrayList(this.getLegalMoves(board, player));
        Collections.sort(moves);
        return moves;
    }

    // TODO à revoir
    public Board submit(final Board board, final Color color, final PieceInterface piece, final PositionInterface position) {
        if (!this.isLegal(board, color, piece, position)) return board;
        return board.put(color, piece, position);
    }

}