/*
 * Copyright 2012-2013 Arie Benichou
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

package blockplus.move;

import blockplus.board.Board;
import blockplus.game.BlockplusGameContext;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.DeadPlayer;
import blockplus.player.Player;
import blockplus.player.PlayerInterface;
import blockplus.player.PlayersInterface;

// TODO ! à revoir
public class MoveHandler {

    private Board getNewBoard(final Board board, final Move move) {
        if (move.getPiece().getId() == 0) return board; // TODO Move.isNull()
        return board.apply(move.getColor(), move.getPiece());
    }

    private PlayerInterface getNewPlayer(final PlayerInterface player, final Move move) {
        if (move.getPiece().getId() == 0) return new DeadPlayer(player);
        final PiecesBag newBagOfPieces = player.getPieces().remove(Pieces.get(move.getPiece().getId()));
        return new Player(player.getColor(), newBagOfPieces, player.getOpponentColor(), player.getStrategy());
    }

    public BlockplusGameContext handle(final BlockplusGameContext gameContext, final Move move) {
        final Board newBoard = this.getNewBoard(gameContext.getBoard(), move);
        final PlayerInterface newPlayer = this.getNewPlayer(gameContext.getPlayer(), move);
        final PlayersInterface newPlayers = gameContext.getPlayers().update(newPlayer);
        //final ColorInterface newColorToPlay = newPlayer.getOpponentColor(); // TODO newPlayerToPlay
        return new BlockplusGameContext(newBoard, newPlayers, move.getColor());
    }

}