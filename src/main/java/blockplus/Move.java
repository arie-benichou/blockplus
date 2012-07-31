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

import blockplus.board.Board;
import blockplus.direction.DirectionInterface;
import blockplus.matrix.Matrix;
import blockplus.piece.Piece;
import blockplus.piece.PieceInterface;
import blockplus.position.PositionInterface;

import com.google.common.base.Preconditions;

public class Move implements Comparable<Move> {// TODO extract Comparator(s)

    private final Color color;
    private final PositionInterface position;
    private final DirectionInterface direction;
    private final PieceInterface piece;
    private final Board newBoard;

    private transient volatile String footPrint;

    // TODO ? inputGameState, outPutGameState
    public Move(
            final Color color,
            final PositionInterface position,
            final DirectionInterface direction,
            final PieceInterface piece,
            final Board newBoard) {
        this.color = color;

        this.position = position; // TODO à revoir
        this.direction = direction; // TODO inutile

        this.piece = piece;
        this.newBoard = newBoard;
    }

    public Color getColor() {
        return this.color;
    }

    public PositionInterface getReferentialPosition() {
        return this.position;
    }

    public DirectionInterface getDirection() {
        return this.direction;
    }

    public PieceInterface getPiece() {
        return this.piece;
    }

    public Board getNewBoard() {
        return this.newBoard;
    }

    private static String computeFootPrint(final Move move) {
        final PieceInterface piece = move.getPiece();
        final PositionInterface position = move.getReferentialPosition().apply(move.getDirection());
        // TODO !! piece.translateTo(position): PieceInterface
        final Matrix matrix = Piece.translate(piece.getMatrix(), piece.getReferential(), position);
        // TODO !! stocker cette instance de piece
        final Piece pieceInstance = new Piece(piece.getId(), matrix, position, piece.getBoxingSquareSide(), piece.getInstanceOrdinal());
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

        final PositionInterface position1 = this.getReferentialPosition().apply(this.getDirection()); // TODO !
        final PositionInterface position2 = that.getReferentialPosition().apply(that.getDirection()); // TODO !
        final int compare4 = position1.compareTo(position2);
        if (compare4 != 0) return compare4;

        return 0;
    }

}