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
import java.util.Map;

import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.color.Color;
import blockplus.move.Move;
import blockplus.piece.PieceComponent;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceTemplate;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;

public class RefereeDemo {

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
                { "Ø.........." }
        };

        final Board<Color> board = parse(data);
        render(board);

        final Map<PieceInterface, Integer> instanceOfPieces = Maps.newHashMap();
        for (final PieceTemplate piece : PieceTemplate.values())
            instanceOfPieces.put(piece.get(), 1); // tester avec 1000 ;)
        //instanceOfPieces.put(PieceTemplate.get(0).get(), 0);
        //instanceOfPieces.put(PieceTemplate.get(1).get(), 1);
        //instanceOfPieces.put(PieceTemplate.get(7).get(), 1);

        final PiecesBag bagOfPieces = new PiecesBag(instanceOfPieces);
        final Player player = new Player(Color.White, bagOfPieces);
        final Referee boardReferee = new Referee();

        //System.out.println("-----------------------------8<-----------------------------");

        if (!player.getBagOfPieces().isEmpty()) {

            final Stopwatch stopwatch = new Stopwatch();

            stopwatch.start();
            final List<Move> legalMoves = boardReferee.getOrderedLegalMoves(board, player);
            stopwatch.stop();

            System.out.println("-----------------------------8<-----------------------------");

            for (final Move legalMove : legalMoves) {
                render(legalMove.getOutputBoard());
            }

            System.out.println("-----------------------------8<-----------------------------");
            System.out.println("number of pieces      : " + player.getBagOfPieces().getList().size());
            System.out.println("number of legal moves : " + legalMoves.size());
            System.out.println("-----------------------------8<-----------------------------");
            System.out.println(stopwatch.toString());
            System.out.println(PieceComponent.FACTORY);
            System.out.println(PieceComposite.FACTORY);
        }

    }
}