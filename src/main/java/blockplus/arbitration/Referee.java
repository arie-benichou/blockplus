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

package blockplus.arbitration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import blockplus.board.Board;
import blockplus.color.Color;
import blockplus.direction.DirectionInterface;
import blockplus.move.Move;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceTemplate;
import blockplus.player.Player;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Referee {

    private boolean hasRoom(final Board<Color> board, final PieceInterface piece) {
        for (final PieceInterface component : piece)
            if (board.get(component.getReferential()).hasOpacity()) return false;
        return true;
    }

    private boolean hasSideOfSameColor(final Board<Color> board, final Color color, final PieceInterface piece) {
        for (final PositionInterface position : piece.getSides())
            if (board.get(position).is(color)) return true;
        return false;
    }

    private boolean hasCornerOfSameColor(final Board<Color> board, final Color color, final PieceInterface piece) {
        // TODO à revoir...
        for (final PositionInterface position : piece.getCorners()) {
            if (board.get(position).is(color)) return true;
            if (board.get(position).is(Color.White)) return true;
        }
        return false;
    }

    private boolean isLegal(final Board<Color> board, final Color color, final PieceInterface piece) {
        return this.hasRoom(board, piece)
                && !this.hasSideOfSameColor(board, color, piece)
                && this.hasCornerOfSameColor(board, color, piece);

    }

    private List<Move> getLegalMovesByPiece(
            final Board<Color> board,
            final Color color,
            final PieceTemplate pieceTemplate,
            final PositionInterface position,
            final DirectionInterface direction
            ) {
        final List<Move> legalMovesByPiece = Lists.newArrayList();
        final int numberOfRotations = pieceTemplate.getNumberOfRotations();
        final PieceInterface translated = pieceTemplate.get().translateTo(position.apply(direction)); // TODO !? façade
        PieceInterface rotated = translated;
        // TODO à revoir...
        if (this.isLegal(board, color, rotated)) legalMovesByPiece.add(new Move(color, rotated));
        for (int i = 1; i < numberOfRotations; ++i) {
            rotated = rotated.rotate();
            if (this.isLegal(board, color, rotated)) legalMovesByPiece.add(new Move(color, rotated));
        }
        return legalMovesByPiece;
    }

    private List<DirectionInterface> getPotentialDirections(final Map<DirectionInterface, Color> neighbours) {
        final List<DirectionInterface> potentialDirections = Lists.newArrayList();
        for (final Entry<DirectionInterface, Color> entry : neighbours.entrySet())
            if (entry.getValue().hasTransparency()) potentialDirections.add(entry.getKey());
        return potentialDirections;
    }

    private Set<Move> getDistinctLegalMovesByPiece(
            final Board<Color> board,
            final Color color,
            final PieceTemplate pieceTemplate,
            final PositionInterface position
            ) {
        // TODO !! computer et utiliser plutot un radius
        final int distance = pieceTemplate.getBoxingSquareSide() - 1;
        final Map<DirectionInterface, Color> neighbours = board.getAllNeighbours(position, distance);
        final List<DirectionInterface> potentialDirections = this.getPotentialDirections(neighbours);
        final Set<Move> distinctLegalMovesByPiece = Sets.newHashSet();
        for (final DirectionInterface direction : potentialDirections)
            distinctLegalMovesByPiece.addAll(this.getLegalMovesByPiece(board, color, pieceTemplate, position, direction));
        return distinctLegalMovesByPiece;

    }

    private List<Move> getLegalMovesByPosition(final Player player, final Board<Color> board, final PositionInterface position) {
        final List<Move> legalMovesByCell = Lists.newArrayList();
        for (final PieceTemplate pieceTemplate : player.getAvailablePieces())
            legalMovesByCell.addAll(this.getDistinctLegalMovesByPiece(board, player.getColor(), pieceTemplate, position));
        return legalMovesByCell;
    }

    public List<PositionInterface> getPotentialPositions(final Board<Color> board, final Color color) {
        final Color potential = color.potential();
        final List<PositionInterface> potentialPositions = Lists.newArrayList();
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.columns(); ++j) {
                final PositionInterface position = Position.from(i, j);
                final Color c = board.get(position);
                if (c.hasTransparency() && c.contains(potential)) potentialPositions.add(position);
            }
        }
        return potentialPositions;
    }

    public Set<Move> getLegalMoves(final Board<Color> board, final Player player) {
        final Set<Move> legalMoves = Sets.newHashSet();
        final List<PositionInterface> potentialPositions = this.getPotentialPositions(board, player.getColor());
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