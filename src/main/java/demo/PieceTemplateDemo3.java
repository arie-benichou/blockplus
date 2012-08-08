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

import static blockplus.board.BoardRenderer.render;
import static blockplus.position.Position.Position;
import blockplus.board.Board;
import blockplus.color.Color;
import blockplus.move.Move;
import blockplus.move.MoveHandler;
import blockplus.piece.Piece;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.position.PositionInterface;

public class PieceTemplateDemo3 {

    public static void main(final String[] args) {
        for (final Pieces piece : Pieces.values()) {
            final PieceInterface pieceInterface = piece.get();
            final int radius = ((Piece) piece.get()).getPieceData().radius(); // TODO à revoir
            final int n = 1 + 2 * (radius + 1);
            final Board<Color> inputBoard = Board.from(n, n, Color.TRANSPARENT, Color.OPAQUE);
            final MoveHandler moveHandler = new MoveHandler(inputBoard);
            final PositionInterface position = Position(n / 2, n / 2);
            final PieceInterface translatedPiece = pieceInterface.translateTo(position);
            System.out.println();
            System.out.println("=================8<=================");
            final Move move = new Move(Color.WHITE, translatedPiece);
            final Board<Color> ouput = moveHandler.handle(move);
            render(ouput);
            System.out.println("radius : " + radius);
        }
        System.out.println();
        System.out.println("=================8<=================");
    }

}