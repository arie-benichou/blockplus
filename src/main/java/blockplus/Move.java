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

package blockplus;

import java.util.List;
import java.util.Map;

import blockplus.board.Board;
import blockplus.matrix.Matrix;
import blockplus.piece.Piece;
import blockplus.piece.PieceInstance;
import blockplus.piece.PieceTemplateInterface;
import blockplus.position.PositionInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

// TODO ? MoveForNull
// TODO extract Comparator(s)
public class Move implements Comparable<Move> {

    private final Color color;
    private final PositionInterface position;
    private final PieceTemplateInterface piece;
    private final Board<Color> board;

    private transient volatile String footPrint;

    // TODO ... inputGameState, outPutGameState
    public Move(
            final Color color,
            final PositionInterface position,
            final PieceTemplateInterface piece,
            final Board<Color> board

    ) {
        this.color = color;
        this.position = position;
        this.piece = piece;
        this.board = board;
    }

    public Color getColor() {
        return this.color;
    }

    public PositionInterface getPosition() {
        return this.position;
    }

    public PieceTemplateInterface getPiece() {
        return this.piece;
    }

    public Board<Color> getOutputBoard() {
        final Map<PositionInterface, Color> cells = Maps.newHashMap();
        final List<PositionInterface> positions = this.getPiece().getPositions(this.getPosition());
        for (final PositionInterface position : positions) {
            cells.put(position, this.getColor());
        }
        return Board.from(this.board, cells);
    }

    private static String computeFootPrint(final Move move) {

        final PieceTemplateInterface piece = move.getPiece();
        if (piece.isNull()) return "";

        final PositionInterface position = move.getPosition();

        // TODO ! PieceInstance as a flatten PieceTemplate (current Piece class)
        final Matrix matrix = Piece.translate(piece.getMatrix(), piece.getReferential(), position);

        // TODO ! pouvoir stocker cette instance
        final PieceInstance pieceInstance = new PieceInstance(piece, matrix, position);

        return pieceInstance.toString();
    }

    public String getFootPrint() {
        String value = this.footPrint;
        if (value == null) {
            synchronized (this) {
                if ((value = this.footPrint) == null) this.footPrint = value = Move.computeFootPrint(this);
            }
        }
        return value;
    }

    @Override
    public int hashCode() {
        return this.getFootPrint().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Move)) return false;
        final Move that = (Move) object;
        final boolean haveSameHashCode = this.hashCode() == that.hashCode();
        final boolean isEqual = this.getFootPrint().equals(that.getFootPrint());
        Preconditions.checkState(haveSameHashCode == isEqual);
        return isEqual;
    }

    @Override
    public String toString() {
        return this.getFootPrint();
    }

    @Override
    public int compareTo(final Move that) {

        final int boxingSquareSide1 = this.getPiece().getBoxingSquareSide();
        final int boxingSquareSide2 = that.getPiece().getBoxingSquareSide();
        final int compare1 = -(boxingSquareSide1 - boxingSquareSide2);
        if (compare1 < 0) return -1;
        if (compare1 > 0) return 1;

        final int id1 = this.getPiece().getId();
        final int id2 = that.getPiece().getId();
        final int compare2 = -(id1 - id2);
        if (compare2 < 0) return -1;
        if (compare2 > 0) return 1;

        final int instanceOrdinal1 = this.getPiece().getInstanceOrdinal();
        final int instanceOrdinal2 = that.getPiece().getInstanceOrdinal();
        final int compare3 = instanceOrdinal1 - instanceOrdinal2;
        if (compare3 < 0) return -1;
        if (compare3 > 0) return 1;

        final int compare4 = this.getPosition().compareTo(that.getPosition());
        //if (compare4 != 0)
        return compare4;

        //return 0;
    }

}