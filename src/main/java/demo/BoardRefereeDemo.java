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

import java.util.List;
import java.util.Map;

import blockplus.Color;
import blockplus.Move;
import blockplus.Player;
import blockplus.board.Board;
import blockplus.board.BoardReferee;
import blockplus.board.BoardRenderingManager;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;

public class BoardRefereeDemo {

    public static void main(final String[] args) {

        final String data = "" +
                "3111113" +
                "1111111" +
                "1111111" +
                "1111111" +
                "1111111" +
                "1111111" +
                "3111111";

        final Board board = Board.from(data);

        final BoardRenderingManager boardRenderingManager = new BoardRenderingManager();

        final Map<PieceInterface, Integer> instanceOfPieces = Maps.newHashMap();
        for (final Pieces piece : Pieces.values()) {
            //instanceOfPieces.put(piece.get(), 1000);
            instanceOfPieces.put(piece.get(), 1);
        }

        instanceOfPieces.put(Pieces.get(0).get(), 0); // TODO tester les opérations avec la pièce null

        final PiecesBag bagOfPieces = new PiecesBag(instanceOfPieces);
        final Player player = new Player(Color.Blue, bagOfPieces);

        final BoardReferee boardReferee = new BoardReferee();

        final Stopwatch stopwatch = new Stopwatch();

        if (!player.getBagOfPieces().isEmpty()) {

            stopwatch.start();
            final List<Move> legalMoves = boardReferee.getOrderedLegalMoves(board, player);
            stopwatch.stop();

            for (final Move legalMove : legalMoves)
                System.out.println(boardRenderingManager.render(legalMove.getNewBoard()));

            System.out.println("number of pieces      : " + player.getBagOfPieces().getList().size());
            System.out.println("number of legal moves : " + legalMoves.size());
            System.out.println("computation time      : " + stopwatch.elapsedMillis());
        }

    }

}