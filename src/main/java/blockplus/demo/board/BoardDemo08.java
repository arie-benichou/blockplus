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
import static blockplus.model.piece.Piece.*;
import static components.position.Position.*;
import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;
import blockplus.view.View;

import com.google.common.collect.Sets;

/**
 * "Colored" view of board.
 */
public final class BoardDemo08 {

    public static void main(final String[] args) {

        final int rows = 9, columns = 9;
        final BoardLayer blueLayer = new BoardLayer(rows, columns).apply(Position(0, 0), State.Light);
        final BoardLayer yellowLayer = new BoardLayer(rows, columns).apply(Position(0, columns - 1), State.Light);
        final BoardLayer redLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, columns - 1), State.Light);
        final BoardLayer greenLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, 0), State.Light);
        final Board board = Board.builder(Sets.newHashSet(Blue, Yellow, Red, Green), rows, columns)
                .set(Blue, blueLayer).set(Yellow, yellowLayer).set(Red, redLayer).set(Green, greenLayer).build();

        Board newBoard = board;
        newBoard = newBoard.apply(Blue, Piece(1).translateTo(Position(0, 0)));
        newBoard = newBoard.apply(Yellow, Piece(1).translateTo(Position(0, 8)));
        newBoard = newBoard.apply(Red, Piece(1).translateTo(Position(8, 8)));
        newBoard = newBoard.apply(Green, Piece(1).translateTo(Position(8, 0)));

        View.as(blockplus.view.window.BoardView.class).show(newBoard).up();
        View.as(blockplus.view.console.BoardView.class).show(newBoard).up();

    }

}