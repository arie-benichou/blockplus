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

package game.blockplus.option;

import game.blockplus.polyomino.projection.PieceInstances.PieceInstance;
import game.interfaces.IOption;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import components.cells.Positions.Position;

public final class Option implements IOption {

    private final static Iterable<Integer> NULL = ImmutableList.of();

    private static Iterable<Integer> computePositions(final PieceInstance piece) {
        if (piece == null) return NULL;
        final Builder<Integer> builder = ImmutableList.builder();
        for (final Position position : piece)
            builder.add(position.id());
        return builder.build();
    }

    private final PieceInstance piece;

    private volatile Iterable<Integer> positions;

    public Option(final PieceInstance piece) {
        this.piece = piece;
    }

    @Override
    public Iterable<Integer> get() {
        Iterable<Integer> positions = this.positions;
        if (positions == null) {
            synchronized (this) {
                if ((positions = this.positions) == null) this.positions = positions = computePositions(this.piece);
            }
        }
        return positions;
    }

    // TODO à revoir    
    @Override
    public boolean isNull() {
        return this.piece == null;
    }

    // TODO à revoir
    @Override
    public int hashCode() {
        return this.get().hashCode();
    }

    // TODO à revoir    
    @Override
    public boolean equals(final Object object) {
        return this.get().equals(object);
    }

    // TODO à revoir
    /*
    @Override
    public String toString() {
        return this.piece.toString();
    }
    */

}