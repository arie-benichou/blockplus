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

import static blockplus.model.board.State.*;
import static blockplus.model.piece.Piece.*;
import static components.position.Position.*;

import java.util.Map;



import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;

import components.board.rendering.StringRendering;

public final class BoardLayerDemo06 {

    public static void main(final String[] args) {
        final Map<?, Character> stateBoardSymbols = ImmutableMap.of(Self, 'O', Other, 'X', None, ' ', Light, '.', Shadow, '#');
        final BoardLayer boardLayer = new BoardLayer(20, 20);
        final StringRendering boardLayerConsoleView = new StringRendering(stateBoardSymbols);
        boardLayerConsoleView.apply(boardLayer.get());

        {
            BoardLayer newBoardLayer = null;
            final Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();
            for (int i = -1; i < 540000; ++i) { // ~ 10 s
                newBoardLayer = boardLayer
                        .apply(Piece(1).translateTo(Position(4, 4)))
                        .apply(Position(1, 1), State.Other);
                newBoardLayer.getShadows();
                newBoardLayer.getLights();
                newBoardLayer.getSelves();
                newBoardLayer.getOthers();
            }
            stopwatch.stop();
            boardLayerConsoleView.apply(newBoardLayer.get());
            System.out.println(stopwatch);
        }

    }

}