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
import blockplus.position.PositionInterface;

import com.google.common.collect.Maps;

public class MoveHandler {

    private final Board<Color> board;

    // TODO !? use guava bus event    
    public MoveHandler(final Board<Color> board) {
        this.board = board;
    }

    // TODO générer un objet BoardMutation (puis par la suite, un GameMutation...)
    public Board<Color> handle(final Move move) {
        final Map<PositionInterface, Color> cells = Maps.newHashMap();
        for (final PositionInterface component : move.getPiece().getPositions()) {
            cells.put(component, move.getColor());
        }

        // TODO !!! gérer les positions potentielles: tester les positions et prendre en compte les potential colors dejà présente sur le board
        for (final PositionInterface potentialPosition : move.getPiece().getPotentialPositions()) {
            cells.put(potentialPosition, move.getColor().potential());
        }
        return Board.from(this.board, cells);
    }

}