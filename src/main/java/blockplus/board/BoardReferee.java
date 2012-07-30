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

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.Color;
import blockplus.Move;
import blockplus.Player;
import blockplus.board.direction.DirectionInterface;
import blockplus.board.position.PositionInterface;
import blockplus.piece.PieceInterface;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
            for (final Integer value : board.getSideNeighbours(coordinates).values())
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

    private List<Move> getLegalMovesByCell(final Player player, final Board board, final PositionInterface position) {
        final List<Move> legalMovesByCell = Lists.newArrayList();
        for (final PieceInterface piece : player.getBagOfPieces()) {
            legalMovesByCell.addAll(this.getDistinctLegalMovesByPiece(board, player.getColor(), piece, position));
        }
        return legalMovesByCell;
    }

    private Map<Integer, Integer> getPotentialCells(final Board board, final Color color) {
        final List<Integer> emptyCells = board.getEmptyCells();
        final Map<Integer, Integer> cornersProductByEmptyCellHavingAtLeastOneCornerOfSameColor = Maps.newTreeMap();
        for (final Integer cellIndex : emptyCells) {
            final Map<DirectionInterface, Integer> neighbours = board.getCorners(cellIndex);
            int cornersProduct = 1;
            for (final Integer n : neighbours.values())
                cornersProduct *= n;
            if (Math.abs(cornersProduct) % color.getValue() == 0)
                cornersProductByEmptyCellHavingAtLeastOneCornerOfSameColor.put(cellIndex, cornersProduct);
        }
        return cornersProductByEmptyCellHavingAtLeastOneCornerOfSameColor;
    }

    public List<Move> getLegalMoves(final Board board, final Player player) {
        final Map<Integer, Integer> potentialCells = this.getPotentialCells(board, player.getColor());
        final List<Move> legalMoves = Lists.newArrayList();
        for (final Integer cellIndex : potentialCells.keySet()) {
            final PositionInterface position = board.getPositionFromIndex(cellIndex);
            final List<Move> legalMovesByCell = this.getLegalMovesByCell(player, board, position);
            legalMoves.addAll(legalMovesByCell);
        }
        return legalMoves;
    }

    // TODO à revoir
    public Board submit(final Board board, final Color color, final PieceInterface piece, final PositionInterface position) {
        if (!this.isLegal(board, color, piece, position)) return board;
        return board.put(color, piece, position);
    }

}