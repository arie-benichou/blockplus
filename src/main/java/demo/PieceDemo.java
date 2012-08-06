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

package demo;

import blockplus.board.Board;
import blockplus.board.BoardBuilder;
import blockplus.board.BoardRenderer;
import blockplus.color.Color;
import blockplus.move.Move;
import blockplus.move.MoveHandler;
import blockplus.piece.PieceComponent;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;
import blockplus.piece.PieceTemplate;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.base.Stopwatch;

public final class PieceDemo {

    private static void TestAllPieces(final Board<Color> board) {

        final MoveHandler moveHandler = new MoveHandler(board);

        for (final PieceTemplate pieceTemplate : PieceTemplate.values()) {
            //pieceTemplate = PieceTemplate.get(7);
            System.out.println("======================8<======================\n");
            System.out.println(pieceTemplate.name());
            //final PositionInterface referential = Position.from(5, 5);
            final PositionInterface position = Position.from(5, 8);
            PieceInterface piece = pieceTemplate.get().translateTo(position);
            for (int i = 0; i < 4; ++i) {
                final Move move = new Move(Color.Blue, piece);
                final Board<Color> ouput = moveHandler.handle(move);
                BoardRenderer.render(ouput);
                System.out.println();
                piece = piece.rotate();
                //piece = piece.rotateAround(referential);
                System.out.println();
            }
            //break;
        }
    }

    public static void main(final String[] args) {
        final String[][] data = {
                { "o.........o" },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { ".....?....." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "o.........o" }
        };
        final Board<Color> board = BoardBuilder.parse(data);
        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();
        for (int i = 0; i < 1; ++i) {
            TestAllPieces(board);
        }
        stopwatch.stop();
        System.out.println(stopwatch.toString());
        System.out.println(PieceComponent.FACTORY);
        System.out.println(PieceComposite.FACTORY);
    }

}