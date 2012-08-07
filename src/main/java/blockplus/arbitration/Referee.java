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
import java.util.Set;

import blockplus.board.Board;
import blockplus.color.Color;
import blockplus.move.Move;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceTemplate;
import blockplus.player.Player;
import static blockplus.position.Position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Referee {

    // TODO ! extract predicate
    private boolean hasRoom(final Board<Color> board, final PieceInterface piece) {
        for (final PieceInterface component : piece)
            if (board.get(component.getReferential()).hasOpacity()) return false;
        return true;
    }

    // TODO ! extract predicate    
    private boolean hasSideOfSameColor(final Board<Color> board, final Color color, final PieceInterface piece) {
        for (final PositionInterface position : piece.getSides())
            if (board.get(position).is(color)) return true;
        return false;
    }

    // TODO ! extract predicate    
    private boolean hasCornerOfSameColor(final Board<Color> board, final Color color, final PieceInterface piece) {
        for (final PositionInterface position : piece.getCorners()) {
            final Color c = board.get(position);
            if (c.is(Color.White)) return true; // TODO à revoir...
            if (c.is(color)) return true;
        }
        return false;
    }

    // TODO ! extract predicate    
    private boolean isLegal(final Board<Color> board, final Color color, final PieceInterface piece) {
        return this.hasRoom(board, piece)
                && !this.hasSideOfSameColor(board, color, piece)
                && this.hasCornerOfSameColor(board, color, piece);

    }

    private List<Move> getLegalMoves(
            final Board<Color> board,
            final Color color,
            final PieceTemplate pieceTemplate,
            final PositionInterface potentialPosition
            ) {
        final PieceInterface piece = pieceTemplate.get();
        PieceInterface rotatedPiece = piece.translateTo(potentialPosition);
        final List<Move> legalMoves = Lists.newArrayList();
        if (this.isLegal(board, color, rotatedPiece)) legalMoves.add(new Move(color, rotatedPiece));
        for (int i = 1; i < pieceTemplate.getNumberOfRotations(); ++i) {
            rotatedPiece = rotatedPiece.rotate();
            if (this.isLegal(board, color, rotatedPiece)) legalMoves.add(new Move(color, rotatedPiece));
        }
        return legalMoves;
    }

    private List<PositionInterface> getPositionsHavingPotential(final Board<Color> board, final Color potential) {
        final List<PositionInterface> positionsHavingPotential = Lists.newArrayList();
        for (int i = 0; i < board.rows(); ++i) {
            for (int j = 0; j < board.columns(); ++j) {
                final PositionInterface position = Position(i, j);
                final Color c = board.get(position);
                if (c.hasTransparency() && c.contains(potential)) positionsHavingPotential.add(position);
            }
        }
        return positionsHavingPotential;
    }

    private Set<PositionInterface> getDistinctPotentialPositions(
            final Board<Color> board,
            final Color color,
            final List<PositionInterface> positionsHavingPotential,
            final PieceTemplate pieceTemplate) {
        final int radius = pieceTemplate.getRadius();
        final Set<PositionInterface> potentialPositions = Sets.newHashSet();
        for (final PositionInterface positionHavingPotential : positionsHavingPotential) {
            for (final PositionInterface neighbour : board.getAllNeighboursPositions(positionHavingPotential, radius)) {
                if (board.get(neighbour).hasTransparency()) potentialPositions.add(neighbour);
            }
        }
        return potentialPositions;
    }

    // TODO pouvoir passer un Ordering/Comparator de Move
    public Set<Move> getLegalMoves(final Board<Color> board, final Player player) {
        final Color color = player.getColor();
        final List<PositionInterface> positionsHavingPotential = this.getPositionsHavingPotential(board, color.potential());
        final Set<Move> legalMoves = Sets.newHashSet();
        for (final PieceTemplate pieceTemplate : player.getAvailablePieces()) {
            final Set<PositionInterface> distinctPotentialPositions = this.getDistinctPotentialPositions(board, color, positionsHavingPotential, pieceTemplate);
            for (final PositionInterface uniquePotentialPosition : distinctPotentialPositions) {
                legalMoves.addAll(this.getLegalMoves(board, color, pieceTemplate, uniquePotentialPosition));
            }
        }
        return legalMoves;
    }

    public List<Move> getOrderedLegalMoves(final Board<Color> board, final Player player) {
        final List<Move> moves = Lists.newArrayList(this.getLegalMoves(board, player));
        Collections.sort(moves);
        return moves;
    }

}