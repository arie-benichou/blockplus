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

package blockplus.context;

import blockplus.board.Board;
import blockplus.move.Move;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.PlayerInterface;
import blockplus.player.Players;

public class ContextMutationHandler {

    private Board getNewBoard(final Context context, final Move move) {
        final Board board = context.getBoard();
        if (move.isNull()) return board; // TODO à mettre dans board.apply()
        return board.apply(move.getColor(), move.getPiece());
    }

    private PlayerInterface getNewPlayer(final Context context, final Move move) {
        final PlayerInterface player = context.getPlayer();
        final int pieceId = move.getPiece().getId();
        final PiecesBag pieces = player.getPieces();
        final PiecesBag newBagOfPieces = pieces.remove(Pieces.get(pieceId));
        return player.apply(newBagOfPieces);
    }

    private Players getNewPlayers(final Context context, final PlayerInterface player) {
        return context.getPlayers().update(player);
    }

    // TODO utiliser le ContextBuilder
    public Context apply(final Context context, final Move move) {
        final ContextBuilder contextBuilder = new ContextBuilder();
        contextBuilder.setSide(context.getSide());
        contextBuilder.setAdversity(context.getAdversity());
        final Players newPlayers = this.getNewPlayers(context, this.getNewPlayer(context, move));
        contextBuilder.setPlayers(newPlayers);
        contextBuilder.setBoard(this.getNewBoard(context, move));
        return contextBuilder.build();
    }

}