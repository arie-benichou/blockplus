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

import blockplus.color.ColorInterface;
import blockplus.piece.Pieces;
import blockplus.piece.PiecesBag;

import com.google.common.base.Objects;

public class Player implements PlayerInterface {

    private final ColorInterface color;
    private final PiecesBag bagOfPieces;

    public Player(final ColorInterface color, final PiecesBag bagOfPieces) {
        this.color = color;
        this.bagOfPieces = bagOfPieces;
    }

    @Override
    public PlayerInterface apply(final PiecesBag bagOfPieces) {
        return new Player(this.getColor(), bagOfPieces);
    }

    @Override
    public ColorInterface getColor() {
        return this.color;
    }

    @Override
    public PiecesBag getPieces() {
        return this.bagOfPieces;
    }

    @Override
    public boolean isAlive() {
        return this.bagOfPieces.contains(Pieces.get(0));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("alive", this.isAlive())
                .add("color", this.getColor())
                .add("pieces", this.bagOfPieces)
                .toString();
    }

}