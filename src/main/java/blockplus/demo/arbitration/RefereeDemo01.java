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

import static blockplus.model.color.Colors.*;
import static components.position.Position.*;

import java.util.List;



import blockplus.model.arbitration.Referee;
import blockplus.model.board.Board;
import blockplus.model.board.BoardLayer;
import blockplus.model.board.State;
import blockplus.model.move.Move;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.Player;
import blockplus.model.player.PlayerInterface;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;

import components.board.rendering.StringRendering;

public final class RefereeDemo01 {

    public static void main(final String[] args) {

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
                .set(Blue, blueLayer).set(Yellow, yellowLayer).set(Red, redLayer).set(Green, greenLayer).build();

        {
            final BoardLayer layer = board.getLayer(Red);
            boardConsoleView.apply(board);
            boardConsoleView.apply(layer.get());
            final PiecesBag bagOfPieces = PiecesBag.from(Pieces.set());
            final PlayerInterface player = new Player(Red, bagOfPieces, Green);
            final Referee blokusReferee = new Referee();
            final Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();
            final List<Move> legalMoves = blokusReferee.getOrderedLegalMoves(board, player);
            stopwatch.stop();
            System.out.println("==================8<==================\n");
            System.out.println("pieces      : " + bagOfPieces.size());
            System.out.println("legal moves : " + legalMoves.size());
            System.out.println(stopwatch);
        }
    }
}