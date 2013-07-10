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

package blockplus.model.entity;

import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import components.cells.Directions.Direction;
import components.cells.Positions.Position;

public final class Node {

    public enum Type {

        ISLAND(0),
        DEAD_END(1),
        STREET(2),
        CORNER(2),
        CROSSROADS(3),
        ROUNDABOUT(4);

        private int numberOfOptions;

        public static Type from(final Set<Direction> data) {
            Preconditions.checkNotNull(data);
            final int numberOfOptions = data.size();
            Preconditions.checkArgument(numberOfOptions >= 0 && numberOfOptions <= 4);
            if (numberOfOptions == 0) return Type.ISLAND;
            if (numberOfOptions == 1) return Type.DEAD_END;
            if (numberOfOptions == 3) return Type.CROSSROADS;
            if (numberOfOptions == 4) return Type.ROUNDABOUT;
            int sumDx = 0, sumDy = 0;
            for (final Direction direction : data) {
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

    private final Position id;
    private final Type type;
    private final Set<Direction> data;

    public static Node from(final Position id, final ImmutableSet<Direction> options) {
        return new Node(id, options);
    }

    private Node(final Position id, final ImmutableSet<Direction> data) {
        this.id = id;
        this.type = Type.from(data);
        this.data = data;
    }

    private Node(final Position id, final Set<Direction> options) {
        this(id, ImmutableSortedSet.copyOf(options));
    }

    public Position id() {
        return this.id;
    }

    public Type type() {
        return this.type;
    }

    public Set<Direction> getData() {
        return this.data;
    }

    public boolean is(final Type type) {
        return this.type().equals(type);
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public boolean equals(final Object object) {
        if (object == null) return false;
        if (object == this) return true;
        if (!(object instanceof Node)) return false;
        final Node that = (Node) object;
        final boolean isEqal = this.id().equals(that.id()) && this.type().equals(that.type());
        return isEqal;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(this.id())
                .addValue(this.type())
                .toString();
    }

}