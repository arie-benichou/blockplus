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

package demo.game;

import static blockplus.board.BoardBuilder.parse;

import java.util.Set;

import blockplus.board.Board;
import blockplus.color.ColorInterface;
import blockplus.game.Game;
import blockplus.game.Game.GameResult;
import blockplus.game.GameConfiguration;
import blockplus.io.MainView;
import blockplus.piece.Piece;
import blockplus.piece.PieceComponent;
import blockplus.piece.PieceComposite;
import blockplus.piece.PieceInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;

import com.google.common.collect.Sets;

/**
 * Random game generator.
 */
public class GameDemo02 {

    public static void main(final String[] args) {

        final String[][] data = {
                { "o...........o" },
                { "............." },
                { "............." },
                { "............." },
                { "............." },
                { "............." },
                { "............." },
                { "............." },
                { "............." },
                { "............." },
                { "............." },
                { "............." },
                { "o...........o" }
        };
        final Board<ColorInterface> board = parse(data);

        final Set<ColorInterface> colors = Sets.newHashSet(ColorInterface.BLUE, ColorInterface.GREEN);
        final Set<Piece> setOfPieces = Pieces.set();

        final GameConfiguration.Builder builder = new GameConfiguration.Builder(colors, setOfPieces);

        final PiecesBag bagOfPieces = PiecesBag.from(setOfPieces);
        final Player player1 = new Player(ColorInterface.BLUE, bagOfPieces);
        final Player player2 = new Player(ColorInterface.GREEN, bagOfPieces);

        final GameConfiguration gameConfiguration = builder
                .setPlayers(player1, player2)
                .setBoard(board)
                .build();

        final Game game = new Game(gameConfiguration);
        final GameResult gameResult = game.start();

        /////////////////////////////////////////////////////////

        MainView.render(gameResult.getLoopResult().getLastRoundResult().getBoard()); // TODO

        System.out.println("-----------------------------8<-----------------------------");
        System.out.println(PieceComponent.FACTORY);
        System.out.println(PieceComposite.FACTORY);
        System.out.println(Piece.FACTORY);
        System.out.println("-----------------------------8<-----------------------------");

        for (final Player player : gameResult.getLoopResult().getLastRoundResult().getPlayers().getDeadPlayers()) {
            System.out.println(player);
            final PiecesBag availablePieces = player.getAvailablePieces();
            for (final PieceInterface remainingPiece : availablePieces) {
                System.out.println(remainingPiece);
            }
            System.out.println("-----------------------------8<-----------------------------");
        }

    }
}