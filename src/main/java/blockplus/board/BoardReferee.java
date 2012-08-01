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
import blockplus.piece.PieceTemplateInterface;
import blockplus.position.PositionInterface;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

// TODO caching
public class BoardReferee {

    // TODO ? predicate
    private boolean hasRoomForPiece(final Board<Color> board, final PieceTemplateInterface piece, final PositionInterface position) {
        for (final PositionInterface coordinates : piece.getPositions(position))
            if (!board.get(coordinates).equals(Color.Transparent)) return false;
        return true;
    }

    // TODO ? predicate
    private boolean hasAtLeastOneCellWithSideNeighbourOfSameColor(final Board<Color> board, final Color color, final PieceTemplateInterface piece,
            final PositionInterface position) {

        // TODO ! BoardReferal
        final BoardManager boardManager = new BoardManager(board);

        for (final PositionInterface coordinates : piece.getPositions(position))
            for (final Color value : boardManager.getNeighbours(coordinates, BoardManager.SIDES_DIRECTIONS).values())
                if (value.equals(color)) return true;
        return false;
    }

    // TODO ? predicate
    private boolean hasAtLeastOneCellWithCornerNeighbourOfSameColor(final Board<Color> board, final Color color, final PieceTemplateInterface piece,
            final PositionInterface position) {

        // TODO ! BoardReferal
        final BoardManager boardManager = new BoardManager(board);

        for (final PositionInterface coordinates : piece.getPositions(position))
            for (final Color value : boardManager.getNeighbours(coordinates, BoardManager.CORNERS_DIRECTIONS).values())
                // TODO ? helper dans Color
                if (Math.abs(value.value()) % color.value() == 0) return true;
        return false;
    }

    // TODO ? predicate
    private boolean isLegal(final Board<Color> board, final Color color, final PieceTemplateInterface piece, final PositionInterface position) {
        return this.hasRoomForPiece(board, piece, position)
                & !this.hasAtLeastOneCellWithSideNeighbourOfSameColor(board, color, piece, position)
                & this.hasAtLeastOneCellWithCornerNeighbourOfSameColor(board, color, piece, position);
    }

    private List<Move> getLegalMovesByPiece(final Board<Color> board, final Color color, final PieceTemplateInterface piece, final PositionInterface position) {
        final List<Move> legalMovesByPiece = Lists.newArrayList();
        for (final PieceTemplateInterface rotation : piece.getRotations()) {
            if (this.isLegal(board, color, rotation, position)) {
                final Move move = new Move(color, position, rotation, board);
                legalMovesByPiece.add(move);
            }
        }
        return legalMovesByPiece;
    }

    private List<DirectionInterface> getPotentialDirections(final Map<DirectionInterface, Color> neighbours) {
        final List<DirectionInterface> potentialDirections = Lists.newArrayList();
        for (final Entry<DirectionInterface, Color> entry : neighbours.entrySet()) {
            final DirectionInterface direction = entry.getKey();
            final Color value = entry.getValue();
            if (value.equals(Color.Transparent)) potentialDirections.add(direction);
        }
        return potentialDirections;
    }

    private Set<Move> getDistinctLegalMovesByPiece(final Board<Color> board, final Color color, final PieceTemplateInterface piece,
            final PositionInterface position) {

        final int distance = piece.getBoxingSquareSide() - 1;

        // TODO ! BoardReferal
        final BoardManager boardManager = new BoardManager(board);

        final Map<DirectionInterface, Color> neighbours = boardManager.getAllNeighbours(position, distance);

        final List<DirectionInterface> potentialDirections = this.getPotentialDirections(neighbours);
        final Set<Move> distinctLegalMovesByPiece = Sets.newHashSet();
        for (final DirectionInterface direction : potentialDirections)
            distinctLegalMovesByPiece.addAll(this.getLegalMovesByPiece(board, color, piece, position.apply(direction)));
        return distinctLegalMovesByPiece;
    }

    private List<Move> getLegalMovesByPosition(final Player player, final Board<Color> board, final PositionInterface position) {
        final List<Move> legalMovesByCell = Lists.newArrayList();
        for (final PieceTemplateInterface piece : player.getBagOfPieces())
            legalMovesByCell.addAll(this.getDistinctLegalMovesByPiece(board, player.getColor(), piece, position));
        return legalMovesByCell;
    }

    // TODO ! réfléchir à des heuristiques permettant d'augmenter le nombre de cut-off 
    private List<PositionInterface> getPotentialPositions(final Board<Color> board, final Color color, final List<PositionInterface> emptyCells) {
        final List<PositionInterface> potentialPositions = Lists.newArrayList();

        // TODO ! BoardReferal
        final BoardManager boardManager = new BoardManager(board);

        for (final PositionInterface position : emptyCells) {
            final Map<DirectionInterface, Color> corners = boardManager.getCorners(position);
            int cornersProduct = 1;
            for (final Color n : corners.values()) {
                cornersProduct *= n.value();
            }
            if (Math.abs(cornersProduct) % color.value() != 0) continue; // TODO ! à revoir
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

    public Set<Move> getLegalMoves(final Board<Color> board, final Player player) {
        final Set<Move> legalMoves = Sets.newHashSet();

        // TODO ! BoardReferal
        final BoardManager boardManager = new BoardManager(board);

        final List<PositionInterface> potentialPositions = this.getPotentialPositions(board, player.getColor(), boardManager.getEmptyCellPositions());
        for (final PositionInterface potentialPosition : potentialPositions)
            legalMoves.addAll(this.getLegalMovesByPosition(player, board, potentialPosition));
        return legalMoves;
    }

    // TODO pouvoir passer un Ordering/Comparator de Move
    public List<Move> getOrderedLegalMoves(final Board<Color> board, final Player player) {
        final List<Move> moves = Lists.newArrayList(this.getLegalMoves(board, player));
        Collections.sort(moves);
        return moves;
    }

}