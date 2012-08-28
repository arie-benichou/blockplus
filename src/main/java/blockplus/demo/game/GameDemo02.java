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

package blockplus.demo.game;

import static blockplus.model.board.State.*;
import static blockplus.model.color.Colors.*;

import java.util.Map;
import java.util.Set;

import blockplus.model.board.BoardLayer;
import blockplus.model.color.ColorInterface;
import blockplus.model.game.Game;
import blockplus.model.game.GameContext;
import blockplus.model.game.GameContextBuilder;
import blockplus.model.piece.Pieces;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.Player;
import blockplus.model.player.PlayerInterface;
import blockplus.model.strategy.FirstOptionStrategy;

import com.google.common.collect.ImmutableMap;
import components.board.rendering.StringRendering;

/**
 * First option game generator.
 */
public class GameDemo02 {

    public static void main(final String[] args) {

        final PiecesBag bagOfPieces = PiecesBag.from(Pieces.set());

        final PlayerInterface bluePlayer = new Player(Blue, bagOfPieces, Yellow, new FirstOptionStrategy());
        final PlayerInterface yellowPlayer = new Player(Yellow, bagOfPieces, Red, new FirstOptionStrategy());
        final PlayerInterface redPlayer = new Player(Red, bagOfPieces, Green, new FirstOptionStrategy());
        final PlayerInterface greenPlayer = new Player(Green, bagOfPieces, Blue, new FirstOptionStrategy());

        final GameContext initialContext = new GameContextBuilder()
                .setPlayers(bluePlayer, yellowPlayer, redPlayer, greenPlayer)
                .build();

        final Game game = new Game(initialContext);
        final GameContext finalContext = game.start();

        System.out.println("-----------------------------8<-----------------------------");
        System.out.println("Game should be over by now :p");
        //TODO ? let Blokus.Board implements BoardInterface
        final Map<?, Character> layerSymbols = ImmutableMap.of(Self, 'O', Other, 'X', Light, '.', Shadow, '#', None, ' ');
        final StringRendering stringRendering = new StringRendering(layerSymbols);
        final Set<ColorInterface> colors = finalContext.getBoard().getColors();
        for (final ColorInterface color : colors) {
            final BoardLayer layer = finalContext.getBoard().getLayer(color);
            System.out.println("-----------------------------8<-----------------------------");
            System.out.println(color);
            System.out.println(stringRendering.apply(layer.get()));
            final PlayerInterface deadPlayer = finalContext.getPlayers().get(color);
            final PiecesBag pieces = deadPlayer.getPieces();
            if (pieces.isEmpty()) System.out.println("No remaining piece.");
            else {
                System.out.println(pieces.getWeight());
            }
            for (final Pieces remainingPiece : pieces) {
                System.out.println(" * " + remainingPiece);
            }
        }
        System.out.println("-----------------------------8<-----------------------------");

    }
}