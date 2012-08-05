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

package blockplus.move;

import java.util.Map;

import blockplus.board.Board;
import blockplus.color.Color;
import blockplus.piece.PieceInterface;
import blockplus.position.PositionInterface;

import com.google.common.collect.Maps;

// TODO !! à finir
// TODO ? MoveForNull
// TODO extract Comparator(s)
public class Move implements Comparable<Move> {

    private final Color color;
    //private final PositionInterface position;
    private final PieceInterface piece;
    private final Board<Color> board;

    //private transient volatile String footPrint;

    // TODO ... inputGameState, outPutGameState
    public Move(
            final Color color,
            //final PositionInterface position,
            final PieceInterface piece,
            final Board<Color> board

    ) {
        this.color = color;
        //this.position = this.position;
        this.piece = piece;
        this.board = board;
    }

    public Color getColor() {
        return this.color;
    }

    public PositionInterface getPosition() {
        return this.getPiece().getReferential();
    }

    public PieceInterface getPiece() {
        return this.piece;
    }

    public Board<Color> getOutputBoard() {// TODO à revoir
        final Map<PositionInterface, Color> cells = Maps.newHashMap();
        final PieceInterface translated = this.getPiece().translateTo(this.getPosition());

        // TODO à revoir
        for (final PieceInterface component : translated) {
            cells.put(component.getReferential(), this.getColor());
        }
        // TODO gérer les positions potentielles
        for (final PositionInterface potentialPosition : translated.getPotentialPositions()) {
            // TODO !? tester les positions
            // TODO !! potential colors
            cells.put(potentialPosition, this.getColor().potential());
        }

        return Board.from(this.board, cells);
    }

    /*
    private static String computeFootPrint(final Move move) {
        final PieceInterface piece = move.getPiece();
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
    */

    @Override
    public String toString() {
        //return this.getFootPrint();
        return "TODO";
    }

    @Override
    public int compareTo(final Move that) {

        /*
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
        */

        return 0;
    }

}