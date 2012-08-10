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
import blockplus.color.ColorInterface;
import blockplus.piece.PieceComponent;
import blockplus.position.PositionInterface;

import com.google.common.collect.Maps;

// TODO ? générer un objet BoardMutation (puis par la suite, un GameMutation...)
public class MoveHandler {

    public Board<ColorInterface> handle(final Board<ColorInterface> board, final Move move) {
        final Map<PositionInterface, ColorInterface> cells = Maps.newHashMap();
        for (final PositionInterface position : move.getPiece().getPositions()) {
            cells.put(position, move.getColor());
        }
        for (final PositionInterface potentialPosition : move.getPiece().getPotentialPositions()) {
            final ColorInterface color1 = board.get(potentialPosition);
            if (color1.hasTransparency()) {
                final PieceComponent pieceComponent = PieceComponent.PieceComponent(potentialPosition);
                boolean isConcretisable = true;
                for (final PositionInterface position : pieceComponent.getSides()) {
                    if (board.get(position).is(move.getColor())) {
                        isConcretisable = false;
                        break;
                    }
                }
                if (isConcretisable) cells.put(potentialPosition, new Color(color1, move.getColor().potential())); // TODO factory
            }
        }
        return Board.from(board, cells);
    }

}