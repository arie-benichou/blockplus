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

package blockplus.model.move;

import static blockplus.model.piece.Piece.*;
import blockplus.model.board.Board;
import blockplus.model.game.GameContext;
import blockplus.model.piece.PiecesBag;
import blockplus.model.player.DeadPlayer;
import blockplus.model.player.Player;
import blockplus.model.player.PlayerInterface;
import blockplus.model.player.PlayersInterface;

public class MoveHandler {

    private Board getNewBoard(final Board board, final Move move) {
        if (move.getPiece().getId() == 0) return board; // TODO Move.isNull()
        return board.apply(move.getColor(), move.getPiece());
    }

    private PlayerInterface getNewPlayer(final PlayerInterface player, final Move move) {
        if (move.getPiece().getId() == 0) return new DeadPlayer(player);
        final PiecesBag newBagOfPieces = player.getPieces().remove(Piece(move.getPiece().getId()));
        return new Player(player.getColor(), newBagOfPieces, player.getOpponentColor(), player.getStrategy());
    }

    public GameContext handle(final GameContext gameContext, final Move move) {
        final Board newBoard = this.getNewBoard(gameContext.getBoard(), move);
        final PlayerInterface newPlayer = this.getNewPlayer(gameContext.getPlayer(), move);
        final PlayersInterface newPlayers = gameContext.getPlayers().update(newPlayer);
        //final ColorInterface newColorToPlay = newPlayer.getOpponentColor(); // TODO newPlayerToPlay
        return new GameContext(newBoard, newPlayers, move.getColor());
    }

}