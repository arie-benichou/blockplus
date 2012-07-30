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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import blockplus.board.Board;
import blockplus.board.direction.DirectionInterface;
import blockplus.board.position.PositionInterface;
import blockplus.piece.PieceInterface;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class Move {

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
        this.position = position;
        this.direction = direction;
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

    private static List<PositionInterface> getOrderedPositions(final PositionInterface position, final Map<DirectionInterface, Integer> neighbours) {
        final List<PositionInterface> positions = Lists.newArrayList();
        for (final DirectionInterface directionInterface : neighbours.keySet()) {
            if (neighbours.get(directionInterface) != 210) // TODO à revoir
                positions.add(position.apply(directionInterface));
        }
        Collections.sort(positions);
        return positions;
    }

    private static String getBoardFragmentFootprint(final int[][] data) {
        final StringBuilder stringBuilder = new StringBuilder("\n ");
        for (final int[] js : data) {
            for (final int k : js)
                stringBuilder.append(k).append(" ");
            stringBuilder.append("\n ");
        }
        return stringBuilder.toString();
    }

    private static String computeFootPrint(final Move move) {
        final int distance = move.getPiece().getBoxingSquareSide() - 1;
        final Map<DirectionInterface, Integer> neighbours = move.getNewBoard().getAllNeighbours(move.getReferentialPosition(), distance);
        final List<PositionInterface> orderedPositions = getOrderedPositions(move.getReferentialPosition(), neighbours);
        final int[][] boardFragment = move.getNewBoard().getFragment(move.getPiece().getBoxingSquareSide(), orderedPositions);
        return getBoardFragmentFootprint(boardFragment);
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

}