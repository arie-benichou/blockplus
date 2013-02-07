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

import blockplus.move.Move;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;
import blockplus.player.Player;

// TODO extract interface
public final class ContextMutationHandler {

    // TODO delate part of this in future player.apply() methode
    private Player getNewPlayer(final Context context, final Move move) {
        final Player player = context.getPlayer();
        final int pieceId = move.getPiece().getId();
        final PiecesBag pieces = player.getPieces();
        final PiecesBag newBagOfPieces = pieces.remove(Pieces.get(pieceId));
        return player.apply(newBagOfPieces);
    }

    public Context apply(final Context context, final Move move) {
        final ContextBuilder contextBuilder = new ContextBuilder();
        contextBuilder.setSide(context.getSide());
        contextBuilder.setAdversity(context.getAdversity());
        contextBuilder.setPlayers(context.getPlayers().update(this.getNewPlayer(context, move)));
        contextBuilder.setBoard(context.getBoard().apply(move));
        return contextBuilder.build();
    }

}