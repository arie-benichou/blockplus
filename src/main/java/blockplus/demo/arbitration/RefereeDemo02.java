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

package blockplus.demo.arbitration;

import static blockplus.model.board.State.*;
import static blockplus.model.color.Colors.*;
import static components.position.Position.*;

import java.util.Map;

import blockplus.model.arbitration.Referee;
import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.Player;
import blockplus.model.player.PlayerInterface;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import components.board.rendering.StringRendering;

public final class RefereeDemo02 {

    public static void main(final String[] args) {
        final Map<?, Character> layerSymbols = ImmutableMap.of(Self, 'O', Other, 'X', None, ' ', Light, '.', Shadow, '#');
        final StringRendering view = new StringRendering(layerSymbols);
        final int rows = 9, columns = 9;
        final BoardLayer blueLayer = new BoardLayer(rows, columns).apply(Position(0, 0), State.Light);
        final BoardLayer yellowLayer = new BoardLayer(rows, columns).apply(Position(0, columns - 1), State.Light);
        final BoardLayer redLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, columns - 1), State.Light);
        final BoardLayer greenLayer = new BoardLayer(rows, columns).apply(Position(rows - 1, 0), State.Light);
        final Board board = Board.builder(Sets.newHashSet(Blue, Yellow, Red, Green), rows, columns)
                .set(Blue, blueLayer).set(Yellow, yellowLayer).set(Red, redLayer).set(Green, greenLayer).build();
        {
            view.apply(board);
            final Referee blokusReferee = new Referee();
            final PiecesBag bagOfPieces = PiecesBag.from(Pieces.set());
            final PlayerInterface player = new Player(Red, bagOfPieces, Green);
            {
                final Stopwatch stopwatch = new Stopwatch();
                stopwatch.start();
                for (int i = -1; i < 8000; ++i) { // ~ 10 s -> 6.85 s
                    blokusReferee.getOrderedLegalMoves(board, player);
                }
                stopwatch.stop();
                System.out.println(stopwatch);
            }
        }

    }
}