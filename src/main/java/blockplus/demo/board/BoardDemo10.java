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

import java.awt.Color;
import java.util.Map;

import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;
import blockplus.model.color.ColorInterface;
import blockplus.model.piece.Pieces;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import components.board.rendering.StringRendering;
import components.board.rendering.SwingRendering;

/**
 * Black color
 */
public final class BoardDemo10 {

    public static void main(final String[] args) throws InterruptedException {

        final Builder<Object, Character> boardSymbolsBuilder = ImmutableMap.builder();
        boardSymbolsBuilder.put(Blue, 'B');
        boardSymbolsBuilder.put(Yellow, 'Y');
        boardSymbolsBuilder.put(Red, 'R');
        boardSymbolsBuilder.put(Green, 'G');
        boardSymbolsBuilder.put(White, ' ');
        boardSymbolsBuilder.put(Black, 'Ø');
        final StringRendering boardConsoleView = new StringRendering(boardSymbolsBuilder.build());

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

        Board newBoard = board;
        newBoard = newBoard.apply(Blue, Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(4, 3)));
        newBoard = newBoard.apply(Yellow, Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(3, 4)));
        newBoard = newBoard.apply(Red, Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(4, 5)));
        newBoard = newBoard.apply(Green, Pieces.get(1).getInstances().getDistinctInstance(0).translateTo(Position(5, 4)));
        boardConsoleView.apply(newBoard);

        boardConsoleView.apply(newBoard);

        // TODO ! extract ColorView
        final Map<ColorInterface, Color> colorByColor = new ImmutableMap.Builder<ColorInterface, Color>()
                .put(Blue, Color.decode("#3971c4"))
                .put(Red, Color.decode("#cc2b2b"))
                .put(Yellow, Color.decode("#eea435"))
                .put(Green, Color.decode("#04a44b"))
                .put(White, Color.decode("#2a2d30"))
                .build();

        final SwingRendering colorView = new SwingRendering(colorByColor);
        colorView.apply(newBoard.colorize());

    }
}