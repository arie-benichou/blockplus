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
 * this program. If not, see <http:www.gnu.org/licenses/>.
 */

package blockplus.demo.board;

import static blockplus.model.color.Colors.*;
import static components.position.Position.*;
import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;
import blockplus.model.piece.Pieces;
import blockplus.view.View;
import blockplus.view.console.BoardView;

import com.google.common.collect.Sets;

public final class BoardDemo11 {

    public static void main(final String[] args) {

        final int rows = 9, columns = 9;
        final BoardLayer blueLayer = new BoardLayer(rows, columns).apply(Position(0, 0), State.Light);
        final BoardLayer yellowLayer = new BoardLayer(rows, columns).apply(Position(0, columns - 1), State.Light);
        final BoardLayer redLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, columns - 1), State.Light);
        final BoardLayer greenLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, 0), State.Light);

        final Board board = Board.builder(Sets.newHashSet(Blue, Yellow, Red, Green), rows, columns)
                .set(Blue, blueLayer)
                .set(Yellow, yellowLayer)
                .set(Red, redLayer)
                .set(Green, greenLayer)
                .build();

        final BoardView consoleBoardView = View.as(blockplus.view.console.BoardView.class).show(board).up();

        Board newBoard = board;
        newBoard = newBoard.apply(Blue, Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(4, 3)));
        consoleBoardView.apply(newBoard);

        newBoard = newBoard.apply(Yellow, Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(3, 4)));
        consoleBoardView.apply(newBoard);

        newBoard = newBoard.apply(Red, Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(4, 5)));
        consoleBoardView.apply(newBoard);

        newBoard = newBoard.apply(Green, Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(5, 4)));
        consoleBoardView.apply(newBoard);

        View.as(blockplus.view.window.BoardView.class).show(newBoard).up();

    }

}