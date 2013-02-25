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

package blockplus.player;

import static blockplus.piece.PieceType.PIECE0;
import interfaces.move.MoveInterface;
import interfaces.player.PlayerInterface;
import blockplus.Color;
import blockplus.move.Move;
import blockplus.piece.PieceType;
import blockplus.piece.Pieces;

import com.google.common.base.Equivalences;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

public final class Player implements PlayerInterface {

    private final Color color;

    public Color getColor() {
        return this.color;
    }

    private final Pieces pieces;

    public Pieces getPieces() {
        return this.pieces;
    }

    public Player(final Color color, final Pieces pieces) {
        this.color = color;
        this.pieces = pieces;
    }

    @Override
    public boolean isAlive() {
        return this.pieces.contains(PIECE0); // TODO memoize
    }

    @Override
    public Player apply(final MoveInterface moveInterface) {
        final Move move = (Move) moveInterface;
        final PieceType piece = PieceType.get(move.getPiece().getId()); // TODO getType()
        final Pieces remainingPieces = this.getPieces().withdraw(piece);
        return new Player(this.getColor(), remainingPieces);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getColor(), this.getPieces());
    }

    @Override
    public boolean equals(final Object object) {
    	if(object==null) return false;
        Preconditions.checkArgument(object instanceof Player);
        final Player that = (Player) object;
        return this.getColor().equals(that.getColor())
                && Equivalences.equals().equivalent(this.getPieces(), that.getPieces());
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("alive", this.isAlive())
                .add("color", this.getColor())
                .add("pieces", this.getPieces())
                .toString();
    }

}