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

package demo.arbitration;

import static blockplus.board.BoardBuilder.parse;
import static blockplus.board.BoardRenderer.render;

import java.util.List;
import java.util.Random;
import java.util.Set;

import blockplus.arbitration.Referee;
import blockplus.board.Board;
import blockplus.board.BoardRenderer;
import blockplus.color.ColorInterface;
import blockplus.io.MainView;
import blockplus.move.Move;
import blockplus.move.MoveHandler;
import blockplus.piece.Piece;
import blockplus.piece.PieceComponent;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Random game generator.
 */
public class RefereeDemo11 {

    public static void main(final String[] args) {

        //Board<Color> board = BoardBuilder.from(20, 20);

        final String[][] data = {
                { "o..................o" },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "...................." },
                { "o..................o" }
        };

        Board<ColorInterface> board = parse(data);

        /////////////////////////////////////////////////////////
        final Random random = new Random();
        final MoveHandler moveHandler = new MoveHandler();
        final Referee referee = new Referee();
        /////////////////////////////////////////////////////////        
        final Set<ColorInterface> playerColors = Sets.newLinkedHashSet();
        playerColors.add(ColorInterface.BLUE);
        playerColors.add(ColorInterface.YELLOW);
        playerColors.add(ColorInterface.RED);
        playerColors.add(ColorInterface.GREEN);
        /////////////////////////////////////////////////////////
        final List<Player> remainingPlayers = Lists.newArrayList();
        for (final ColorInterface color : playerColors) {
            remainingPlayers.add(new Player(color, PiecesBag.from(Pieces.set())));
        }
        /////////////////////////////////////////////////////////
        final List<Player> noMorePlayers = Lists.newArrayList();
        /////////////////////////////////////////////////////////
        render(board);
        BoardRenderer.debug(board);
        System.out.println("-----------------------------8<-----------------------------");
        /////////////////////////////////////////////////////////
        while (!remainingPlayers.isEmpty()) {

            /////////////////////////////////////////////////////////
            final List<Player> players = Lists.newArrayList(remainingPlayers);
            remainingPlayers.clear();
            /////////////////////////////////////////////////////////

            for (final Player player : players) {
                /////////////////////////////////////////////////////////
                try {
                    Thread.sleep(300);
                }
                catch (final InterruptedException e) {}
                /////////////////////////////////////////////////////////
                final List<Move> legalMoves = referee.getOrderedLegalMoves(board, player);
                /////////////////////////////////////////////////////////
                if (legalMoves.isEmpty()) {
                    System.out.println(player + " has no more move.");
                    noMorePlayers.add(player);
                }
                else {
                    /////////////////////////////////////////////////////////
                    final int numberOfLegalMoves = legalMoves.size();
                    //System.out.println(numberOfLegalMoves);
                    final Move randomLegalMove = legalMoves.get(random.nextInt(numberOfLegalMoves));
                    /////////////////////////////////////////////////////////
                    board = moveHandler.handle(board, randomLegalMove);
                    /////////////////////////////////////////////////////////
                    // TODO faire Move(Piece, Position, rotationOrdinal)
                    // TODO ? définir les pièces avec référentiel à (0,0)
                    //final PieceInterface piece = Piece(randomLegalMove.getPiece().getId());
                    final Piece piece = Pieces.get(randomLegalMove.getPiece().getId());
                    final PiecesBag remainingPieces = player.getAvailablePieces().remove(piece);
                    final Player p = new Player(player.getColor(), remainingPieces); // TODO ? NullPlayer
                    remainingPlayers.add(p);
                    /////////////////////////////////////////////////////////
                }
                /////////////////////////////////////////////////////////
                render(board);
                BoardRenderer.debug(board);
                MainView.render(board);
                /////////////////////////////////////////////////////////
            }
        }
        /////////////////////////////////////////////////////////        
        //MainView.render(board);
        System.out.println("-----------------------------8<-----------------------------");
        System.out.println(PieceComponent.FACTORY);
        System.out.println(PieceComposite.FACTORY);
        System.out.println(Piece.FACTORY);
        System.out.println("-----------------------------8<-----------------------------");
        /////////////////////////////////////////////////////////
        for (final Player player : noMorePlayers) {
            System.out.println(player);
            final PiecesBag availablePieces = player.getAvailablePieces();
            for (final PieceInterface remainingPiece : availablePieces) {
                System.out.println(remainingPiece);
            }
            System.out.println("-----------------------------8<-----------------------------");
        }
        /////////////////////////////////////////////////////////
    }
}