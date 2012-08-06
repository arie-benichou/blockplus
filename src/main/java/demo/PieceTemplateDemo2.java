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

import java.util.Set;

import blockplus.board.Board;
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

import com.google.common.collect.Sets;

public class PieceTemplateDemo2 {

    public static void main(final String[] args) {

        for (final PieceTemplate pieceTemplate : PieceTemplate.values()) {
            final PieceInterface pieceInterface = pieceTemplate.get();
            final int radius = pieceTemplate.getRadius();
            final int n = 1 + 2 * (radius + 1);
            final Board<Color> inputBoard = Board.from(n, n, Color.TRANSPARENT, Color.OPAQUE);
            final PositionInterface position = Position.from(n / 2, n / 2);
            final PieceInterface translatedPiece = pieceInterface.translateTo(position);
            final MoveHandler moveHandler = new MoveHandler(inputBoard);
            final Set<PieceInterface> rotations = Sets.newHashSet();
            PieceInterface rotatedPiece = translatedPiece;
            {
                rotations.add(rotatedPiece);
                System.out.println();
                System.out.println("=================8<=================");
                final Move move = new Move(Color.White, rotatedPiece);
                final Board<Color> ouput = moveHandler.handle(move);
                BoardRenderer.render(ouput);
            }
            for (int i = 1; i < 4; ++i)
            {
                rotatedPiece = rotatedPiece.rotate();
                rotations.add(rotatedPiece);
                System.out.println();
                final Move move = new Move(Color.White, rotatedPiece);
                final Board<Color> ouput = moveHandler.handle(move);
                BoardRenderer.render(ouput);
            }
            System.out.println("=================8<=================");
            System.out.println("Nombre de rotations distinctes: " + rotations.size());
            System.out.println("=================8<=================");

        }
        System.out.println(PieceComponent.FACTORY);
        System.out.println(PieceComposite.FACTORY);
    }

}