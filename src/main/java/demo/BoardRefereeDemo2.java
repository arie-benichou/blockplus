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
import java.util.Set;

import blockplus.Color;
import blockplus.Move;
import blockplus.Player;
import blockplus.board.Board;
import blockplus.board.BoardReferee;
import blockplus.board.BoardRepresentation;
import blockplus.piece.PieceTemplateInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.position.Position;
import blockplus.position.PositionInterface;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

// TODO MasterPiece (a template of a piece)
// TODO RotatedPiece (a rotated template of a piece)
// TODO TranslatedPiece (a rotated and translated template of a piece)
// TODO Piece (a rotated, translated and colored template of piece)
public class BoardRefereeDemo2 {

    public static void main(final String[] args) {

        final String[][] data = {
                { "Ø.........Ø" },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { ".....?....." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "..........." },
                { "Ø.........Ø" }
        };

        final Board<Color> board = BoardRepresentation.parse(data);
        System.out.println(board);

        final ImmutableMap<PieceTemplateInterface, Integer> instanceOfPieces = ImmutableMap.of(Pieces.get(1).get(), 1);
        final PiecesBag bagOfPieces = new PiecesBag(instanceOfPieces);
        final Player player = new Player(Color.White, bagOfPieces);
        final BoardReferee boardReferee = new BoardReferee();
        final PositionInterface position = Position.from(5, 5);

        for (final Pieces piece : Pieces.values()) {

            System.out.println("================================8<================================");
            System.out.println();
            System.out.println(piece.name());
            System.out.println(piece);

            int sumOfLegalMoves = 0;
            final Set<Move> distinctLegalMoves = Sets.newHashSet();

            System.out.println(new Move(player.getColor(), position, piece.get(), board).getOutputBoard());
            System.out.println("--------------------------------8<--------------------------------");

            for (final PieceTemplateInterface rotation : piece.get().getRotations()) {

                final Move move = new Move(player.getColor(), position, rotation, board);
                final Board<Color> outputBoard = move.getOutputBoard();

                final List<Move> legalMoves = boardReferee.getOrderedLegalMoves(outputBoard, player);
                distinctLegalMoves.addAll(legalMoves);

                for (final Move legalMove : legalMoves) {
                    System.out.println(legalMove.getOutputBoard());
                    System.out.println(legalMove);
                }

                final int numberOfLegalMoves = legalMoves.size();
                sumOfLegalMoves += numberOfLegalMoves;

                System.out.println("--------------------------------8<--------------------------------");
                System.out.println();
                System.out.println("number of legal moves : " + numberOfLegalMoves);
                System.out.println();
                System.out.println("--------------------------------8<--------------------------------");
            }

            System.out.println();
            System.out.println("sum of legal moves          : " + sumOfLegalMoves);
            System.out.println("sum of distinct legal moves : " + distinctLegalMoves.size());
            System.out.println();

        }

        System.out.println("================================8<================================");

    }
}