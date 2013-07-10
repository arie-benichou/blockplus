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

package blockplus.model.player;


import blockplus.model.entity.Polyomino;

import com.google.common.base.Objects;

public final class Player {

    public static Player from(final RemainingPieces remainingPieces) {
        return new Player(remainingPieces);
    }

    private final RemainingPieces remainingPieces;

    public RemainingPieces remainingPieces() {
        return this.remainingPieces;
    }

    private Player(final RemainingPieces remainingPieces) {
        this.remainingPieces = remainingPieces;
    }

    public Player apply(final Polyomino polyomino) {
        return new Player(this.remainingPieces().withdraw(polyomino));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("pieces", this.remainingPieces()).toString();
    }

}