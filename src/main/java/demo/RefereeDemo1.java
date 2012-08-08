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

package demo;

import static blockplus.board.BoardBuilder.parse;
import static blockplus.board.BoardRenderer.render;

import java.util.List;

import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.color.Color;
import blockplus.move.Move;
import blockplus.move.MoveHandler;
import blockplus.piece.Piece;
import blockplus.piece.PieceComponent;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

public class RefereeDemo1 {

    public static void main(final String[] args) {
        final String[][] data = {
                { "Ø.........Ø" },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "Ø.........o" }
        };
        final Board<Color> board = parse(data);
        final MoveHandler moveHandler = new MoveHandler(board);

        // TODO à revoir
        final List<PieceInterface> list = Lists.newArrayList();
        for (final Pieces piece : Pieces.values())
            list.add(piece.get());

        final Player player = new Player(Color.WHITE, PiecesBag.from(list));
        final Referee boardReferee = new Referee();
        render(board);
        if (!player.getAvailablePieces().isEmpty()) {
            List<Move> legalMoves = null;
            final Stopwatch stopwatch = new Stopwatch();
            stopwatch.start();
            {
                for (int i = 0; i < 8000; ++i)
                    // 10 s
                    legalMoves = boardReferee.getOrderedLegalMoves(board, player);
            }
            stopwatch.stop();
            System.out.println("-----------------------------8<-----------------------------");
            for (final Move legalMove : legalMoves)
                render(moveHandler.handle(legalMove)); // TODO ? MoveRenderer
            System.out.println("-----------------------------8<-----------------------------");
            System.out.println("number of pieces      : " + player.getAvailablePieces().size());
            System.out.println("number of legal moves : " + legalMoves.size());
            System.out.println("-----------------------------8<-----------------------------");
            System.out.println(PieceComponent.FACTORY);
            System.out.println(PieceComposite.FACTORY);
            System.out.println(Piece.FACTORY);
            System.out.println(Piece.counter);
            System.out.println(stopwatch.toString());
        }
    }
}