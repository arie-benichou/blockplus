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

package blockplus.piece2.matching;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import components.direction.DirectionInterface;
import components.position.PositionInterface;

public final class PieceNode {

    public enum Type {

        ISLAND(0),
        DEAD_END(1),
        STREET(2),
        CORNER(2),
        CROSSROADS(3),
        ROUNDABOUT(4);

        private int numberOfOptions;

        public static Type from(final Set<DirectionInterface> data) {
            Preconditions.checkNotNull(data);
            final int numberOfOptions = data.size();
            Preconditions.checkArgument(numberOfOptions >= 0 && numberOfOptions <= 4);
            if (numberOfOptions == 0) return Type.ISLAND;
            if (numberOfOptions == 1) return Type.DEAD_END;
            if (numberOfOptions == 3) return Type.CROSSROADS;
            if (numberOfOptions == 4) return Type.ROUNDABOUT;
            int sumDx = 0, sumDy = 0;
            for (final DirectionInterface direction : data) {
                sumDx += direction.columnDelta();
                sumDy += direction.rowDelta();
            }
            return sumDx == 0 && sumDy == 0 ? Type.STREET : Type.CORNER;
        }

        private Type(final int numberOfOptions) {
            this.numberOfOptions = numberOfOptions;
        }

        public int getNumberOfOptions() {
            return this.numberOfOptions;
        }

    }

    private final PositionInterface id;
    private final Type type;
    private final Set<DirectionInterface> data;

    public static PieceNode from(final PositionInterface id, final ImmutableSet<DirectionInterface> options) {
        return new PieceNode(id, options);
    }

    // TODO add extra data (cf Piece)
    private PieceNode(final PositionInterface id, final ImmutableSet<DirectionInterface> data) {
        this.id = id;
        this.type = Type.from(data);
        this.data = data;
    }

    private PieceNode(final PositionInterface id, final Set<DirectionInterface> options) {
        this(id, ImmutableSortedSet.copyOf(options));
    }

    public PositionInterface getId() {
        return this.id;
    }

    public Type getType() {
        return this.type;
    }

    public Set<DirectionInterface> getData() {
        return this.data;
    }

    public boolean is(final Type type) {
        return this.getType().equals(type);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof PieceNode)) return false;
        final PieceNode that = (PieceNode) object;
        final boolean isEqal = this.getId().equals(that.getId()) && this.getType().equals(that.getType());
        return isEqal;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.getId())
                .addValue(this.getType())
                .toString();
    }

}