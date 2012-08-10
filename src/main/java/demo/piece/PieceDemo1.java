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

package demo.piece;

import static blockplus.piece.Piece.Piece;
import static blockplus.position.Position.Position;

import java.util.List;

import blockplus.board.Board;
import blockplus.board.BoardBuilder;
import blockplus.board.BoardRenderer;
import blockplus.color.ColorInterface;
import blockplus.move.Move;
import blockplus.move.MoveHandler;
import blockplus.piece.PieceComponent;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;

public final class PieceDemo1 {

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

        final Board<ColorInterface> board = BoardBuilder.parse(data);
        final PieceInterface piece = Piece(20);
        PieceInterface movedPiece = piece.translateTo(Position(5, 5));
        final MoveHandler moveHandler = new MoveHandler();
        final Stopwatch stopwatch = new Stopwatch();
        stopwatch.start();

        final List<Move> rotations = Lists.newArrayListWithCapacity(4);
        for (int i = -1; i < 1850000 * 1; ++i) { // ~ 10 s
            rotations.clear();
            for (int n = 0; n < 4; ++n) {
                rotations.add(new Move(ColorInterface.WHITE, movedPiece));
                movedPiece = movedPiece.rotate();
            }
        }

        stopwatch.stop();
        System.out.println("-----------------------------8<-----------------------------");
        for (final Move move : rotations) {
            BoardRenderer.render(moveHandler.handle(board, move));
        }
        System.out.println("-----------------------------8<-----------------------------");
        System.out.println(PieceComponent.FACTORY);
        System.out.println(PieceComposite.FACTORY);
        System.out.println("-----------------------------8<-----------------------------");
        System.out.println(stopwatch.toString());
    }

}