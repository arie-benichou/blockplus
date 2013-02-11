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

import interfaces.move.MoveInterface;
import interfaces.player.PlayerInterface;
import blockplus.Color;
import blockplus.move.Move;
import blockplus.piece.PieceType;
import blockplus.piece.PiecesBag;

import com.google.common.base.Objects;

// TODO hashcode, equals
public final class Player implements PlayerInterface {

    private final Color color;

    public Color getColor() {
        return this.color;
    }

    private final PiecesBag pieces;

    public PiecesBag getPieces() {
        return this.pieces;
    }

    public Player(final Color color, final PiecesBag pieces) {
        this.color = color;
        this.pieces = pieces;
    }

    @Override
    public boolean isAlive() {
        return this.pieces.contains(PieceType.get(0)); // TODO Scala lazy
    }

    @Override
    public Player apply(final MoveInterface moveInterface) {
        final Move move = (Move) moveInterface;
        final PieceType piece = PieceType.get(move.getPiece().getId());
        final PiecesBag remainingPieces = this.getPieces().withdraw(piece);
        return new Player(this.getColor(), remainingPieces);
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