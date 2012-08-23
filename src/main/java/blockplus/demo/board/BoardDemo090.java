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

import java.awt.Color;
import java.util.Map;



import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.Concurrency;
import blockplus.model.board.State;
import blockplus.model.color.ColorInterface;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;

import components.board.rendering.SwingRendering;
import components.board.rendering.StringRendering;

/**
 * White color
 */
public final class BoardDemo090 {

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
        newBoard = newBoard.apply(Blue, Piece(1).translateTo(Position(3, 3)));
        newBoard = newBoard.apply(Yellow, Piece(1).translateTo(Position(3, 5)));
        newBoard = newBoard.apply(Red, Piece(1).translateTo(Position(5, 5)));
        newBoard = newBoard.apply(Green, Piece(1).translateTo(Position(5, 3)));
        boardConsoleView.apply(newBoard);

        ///////////////////////////////////////////////////////////////////////

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

        ///////////////////////////////////////////////////////////////////////

        // TODO ! extract LightConcurrencyView
        final Map<Concurrency, Color> colorByConcurrency = new ImmutableMap.Builder<Concurrency, Color>()
                .put(Concurrency.NONE, new Color(255, 255, 255, 0))
                .put(Concurrency.ONE, new Color(255, 255, 255, 60))
                .put(Concurrency.TWO, new Color(255, 255, 255, 120))
                .put(Concurrency.THREE, new Color(255, 255, 255, 180))
                .put(Concurrency.FOUR, new Color(255, 255, 255, 255))
                .build();

        final SwingRendering lightConcurrencyView = new SwingRendering(colorByConcurrency);

        lightConcurrencyView.apply(newBoard.getSelfLightConcurrency(Blue));

        /*
        Thread.sleep(1000);
        lightConcurrencyView.render(newBoard.getLightConcurrency(Yellow));
        Thread.sleep(1000);
        lightConcurrencyView.render(newBoard.getLightConcurrency(Red));
        Thread.sleep(1000);
        lightConcurrencyView.render(newBoard.getLightConcurrency(Green));
        */

    }

}